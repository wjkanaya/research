package deus_proto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import deus_proto.util.OneHotMaker;
import mybaits.vo.ConvariateData;
import mybaits.vo.CovariatesEffectiveInfo;
import mybaits.vo.CovariatesInfo;
import mybaits.vo.YearEstimateInfo;
import mybatis.dao.CovariatesEffectiveInfoDAO;
import mybatis.dao.CovariatesInfoDAO;
//import mybaits.vo.YearEstimateInfoPre;
import mybatis.dao.MemberHistInfoDAO;

public class GeneSurvDataEstimate {


	public List<YearEstimateInfo> getList() {
		return list;
	}

	public void setList(List<YearEstimateInfo> list) {
		this.list = list;
	}

	public List<Map<YearEstimateInfo, Integer>> getRetireCountMeisaiList() {
		return retireCountMeisaiList;
	}

	public void setRetireCountMeisaiList(List<Map<YearEstimateInfo, Integer>> retireCountMeisaiList) {
		this.retireCountMeisaiList = retireCountMeisaiList;
	}

	public List<Map<YearEstimateInfo, Integer>> getNotRetireCountMeisaiList() {
		return notRetireCountMeisaiList;
	}

	public void setNotRetireCountMeisaiList(List<Map<YearEstimateInfo, Integer>> notRetireCountMeisaiList) {
		this.notRetireCountMeisaiList = notRetireCountMeisaiList;
	}

	public int getYearRange() {
		return yearRange;
	}

	public void setYearRange(int yearRange) {
		this.yearRange = yearRange;
	}

	public int getBetaSize() {
		return betaSize;
	}

	public void setBetaSize(int betaSize) {
		this.betaSize = betaSize;
	}

	public List<Map<YearEstimateInfo, Double>> getPdCacheList() {
		return pdCacheList;
	}

	public void setPdCacheList(List<Map<YearEstimateInfo, Double>> pdCacheList) {
		this.pdCacheList = pdCacheList;
	}

	List<YearEstimateInfo> list = null;
	List<YearEstimateInfo> list2 = null;

	public List<YearEstimateInfo> getMapList() {
		return mapList;
	}

	public void setMapList(List<YearEstimateInfo> mapList) {
		this.mapList = mapList;
	}

	List<YearEstimateInfo> mapList = null;


	List<Map<YearEstimateInfo, Integer>> retireCountMeisaiList = null;
	List<Map<YearEstimateInfo, Integer>> notRetireCountMeisaiList = null;
    int yearRange = 0;
    int betaSize = 0;
    List<Map<YearEstimateInfo, Double>> pdCacheList = null;

    List<ConvariateData> convariateDataList = null;

	public void getData() {
		MemberHistInfoDAO dao = new MemberHistInfoDAO();
		list = dao.selectMemberHistYearEstimateInfoMap();
		list2 = dao.selectMemberHistYearEstimateInfoMap2();

		System.out.println(list.size());
	}

	public void makeData() {
		//	List<Integer> retireCountList = new ArrayList<Integer>();
		List<Map<YearEstimateInfo, Integer>> retireCountMeisaiList
		= new ArrayList<Map<YearEstimateInfo, Integer>>();
//		List<Integer> notRetireCountList = new ArrayList<Integer>();
		List<Map<YearEstimateInfo, Integer>> notRetireCountMeisaiList
		= new ArrayList<Map<YearEstimateInfo, Integer>>();

		for (YearEstimateInfo info :list) {
			int years = info.getYears();
			while (retireCountMeisaiList.size() <= years) {
				retireCountMeisaiList.add(new TreeMap<YearEstimateInfo, Integer>());
				notRetireCountMeisaiList.add(new TreeMap<YearEstimateInfo, Integer>());
			}

			for (int i = 0; i < years; i++) {
				Integer addCount = Integer.valueOf(info.getCount().intValue()
						+ info.getCensored().intValue());
				notRetireCountMeisaiList.get(i).put(info, addCount);
			}
			retireCountMeisaiList.get(years).put(info, info.getCount().intValue());
		}

		// 冗長削除
		for (int i = retireCountMeisaiList.size()-1; i >=0; i--) {
			if (!retireCountMeisaiList.get(i).isEmpty() ||
				!notRetireCountMeisaiList.get(i).isEmpty()) {
				break;
			}

			retireCountMeisaiList.remove(i);
			notRetireCountMeisaiList.remove(i);
		}

		this.retireCountMeisaiList  = retireCountMeisaiList;
		this.notRetireCountMeisaiList  = notRetireCountMeisaiList;

		// 何か年か？
		this.yearRange = retireCountMeisaiList.size();

		// 共変量リスト
		this.convariateDataList = OneHotMaker.getConvariateDataList(list.get(0).getCovariatesMap());


		// beta0は0と仮定
		//int betaSize = 1 + retireCountList.size() + getXList(list.get(0)).size();
		this.betaSize = this.yearRange  + convariateDataList.size();

		// PD計算キャッシュ
		// betaArrの値が変わるたびに更新してください。
		List<Map<YearEstimateInfo, Double>> pdCacheList =
				new ArrayList<Map<YearEstimateInfo, Double>>();
		for (int year = 0; year < yearRange; year++) {
			pdCacheList.add(new HashMap<YearEstimateInfo, Double>());
		}

		this.pdCacheList = pdCacheList;
	}


	public void clearCache() {
		for (Map<YearEstimateInfo, Double> map :this.pdCacheList) {
			map.clear();
		}
	}

	public RealMatrix delta(RealMatrix betaMat) {
		clearCache();

		double[][] betaData = betaMat.getData();

		double[] betaArr = new double[betaData.length];

		for (int i = 0; i < betaData.length; i++) {
			betaArr[i] = betaData[i][0];
		}

		// 1階微分

		//double[] deltaBetaArr = new double[betaSize];
		List<Double> deltaBetaList = new ArrayList<Double>(betaArr.length);

		// deltaBetaArr[0] = − （iが1からNまでの総和）(PDi−Yi)*1

		List<DDouble> sumList = new ArrayList<DDouble>();

//			// パネルデータの総和部分
		double sigma = 0;

		// 経過年数それぞれのデルタbeta
		for (int year = 0; year < yearRange; year++) {
			sumList.clear();
			Map<YearEstimateInfo, Integer> map = null;
			// 継続中のデータなのでY=0
			int Y = 0;
			// 継続
			map = notRetireCountMeisaiList.get(year);
			makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, -1);

			// 退職
			Y = 1;
			map = retireCountMeisaiList.get(year);
			makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, -1);

			// 昇順ソート
			Collections.sort(sumList);
			// パネルデータの総和部分
			sigma = 0;

			for (DDouble dd :sumList) {
				sigma += dd.getValue();
			}

			// 下に凸関数にしたいので-から+
			deltaBetaList.add(Double.valueOf(sigma));
		}

		int xRange = betaArr.length - yearRange;

		// 経過年数それぞれのデルタbeta
		for (int xIndex = 0; xIndex < xRange; xIndex++) {
			sumList.clear();
			// 年数
			for (int year = 0; year < yearRange; year++) {
				// 継続中のデータなのでY=0
				int Y = 0;
				Map<YearEstimateInfo, Integer> map = null;
				// 継続
				map = notRetireCountMeisaiList.get(year);
				makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, xIndex);

				// 退職
				Y = 1;
				map = retireCountMeisaiList.get(year);
				//	set = map.entrySet();
				makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, xIndex);
			}

			// 昇順ソート
			Collections.sort(sumList);
			// パネルデータの総和部分
			sigma = 0;
			for (DDouble dd :sumList) {
				sigma += dd.getValue();
			}

			// 下に凸関数にしたいので-から+
			deltaBetaList.add(Double.valueOf(sigma));

		}

		double[][] resultbeta = new double[deltaBetaList.size()][1];

		for (int i = 0; i < resultbeta.length; i++) {
			resultbeta[i][0] = deltaBetaList.get(i);

		}

		return MatrixUtils.createRealMatrix(resultbeta);

	}

	public double L(double[][] beta) {
		clearCache();

		double[] betaArr = new double[beta.length];
		for (int i = 0; i < beta.length; i++) {
			betaArr[i] =  beta[i][0];
		}

		// 1階微分

		List<DDouble> sumList = new ArrayList<DDouble>();

     	// パネルデータの総和部分
		double sigma = 0;

		// 経過年数それぞれのデルタbeta
		for (int year = 0; year < yearRange; year++) {

			Map<YearEstimateInfo, Integer> map = null;
			// 継続中のデータなのでY=0
			int Y = 0;
			// 継続
			map = notRetireCountMeisaiList.get(year);
			makeSumListL(yearRange, betaArr, pdCacheList, sumList, map, year, Y);

			// 退職
			Y = 1;
			map = retireCountMeisaiList.get(year);
			makeSumListL(yearRange, betaArr, pdCacheList, sumList, map, year, Y);
		}

		// 昇順ソート
		Collections.sort(sumList);
		// パネルデータの総和部分
		sigma = 0;

		for (DDouble dd :sumList) {
			sigma += dd.getValue();
		}

		// 下に凸関数にしたい
		return -sigma;
	}

	public void setCovariatesValue(RealMatrix lastBetaMat) {

		CovariatesEffectiveInfoDAO covariatesEffectiveInfoDAO = new CovariatesEffectiveInfoDAO();
		CovariatesInfoDAO covariatesInfoDAO = new CovariatesInfoDAO();

		Date nowDate = new Date();

		// 経過年
		// 共変量有効情報
		covariatesEffectiveInfoDAO.deleteCovariatesEffectiveInfo(DeusConst.CT0001, DeusConst.C00001);

		CovariatesEffectiveInfo ceInfo = new CovariatesEffectiveInfo();
		// 退職
		ceInfo.setCulcTargetCode(DeusConst.CT0001);
		// 経過年数
		ceInfo.setCovariatesCode(DeusConst.C00001);
		ceInfo.setEffectStartTime(nowDate);
		ceInfo.setEffectFlg(Boolean.valueOf(true));
		covariatesEffectiveInfoDAO.insertCovariatesEffectiveInfo(ceInfo);

		covariatesInfoDAO.deleteCovariatesInfo(DeusConst.CT0001, DeusConst.C00001);

		List<CovariatesInfo> list = new ArrayList<CovariatesInfo>();


		for (int i =0; i < yearRange;i++) {
			CovariatesInfo covInfo = new CovariatesInfo();
			covInfo.setCulcTargetCode(DeusConst.CT0001);
			covInfo.setCovariatesCode(DeusConst.C00001);
			covInfo.setEffectStartTime(nowDate);
			covInfo.setCovariatesLabelNum(Integer.valueOf(i));
			covInfo.setCovariatesValue(BigDecimal.valueOf(lastBetaMat.getEntry(i, 0)));

			covariatesInfoDAO.insertCovariatesInfo(covInfo);

			//list.add(covInfo);
		}



		String nowCovariatesCode = "";

		int i = yearRange;
		// 共変量情報
		for (ConvariateData convData :convariateDataList) {
			if (!nowCovariatesCode.equals(convData.getConvariateCode())) {
				nowCovariatesCode = convData.getConvariateCode();
				covariatesEffectiveInfoDAO.deleteCovariatesEffectiveInfo(DeusConst.CT0001, nowCovariatesCode);
				ceInfo = new CovariatesEffectiveInfo();

				// 退職
				ceInfo.setCulcTargetCode(DeusConst.CT0001);
				// 経過年数
				ceInfo.setCovariatesCode(nowCovariatesCode);
				ceInfo.setEffectStartTime(nowDate);
				ceInfo.setEffectFlg(Boolean.valueOf(true));
				covariatesEffectiveInfoDAO.insertCovariatesEffectiveInfo(ceInfo);

				covariatesInfoDAO.deleteCovariatesInfo(DeusConst.CT0001, nowCovariatesCode);

			}


			CovariatesInfo covInfo = new CovariatesInfo();
			covInfo.setCulcTargetCode(DeusConst.CT0001);
			covInfo.setCovariatesCode(convData.getConvariateCode());
			covInfo.setEffectStartTime(nowDate);
			covInfo.setCovariatesLabelNum(Integer.valueOf(convData.getConvariateLabel()));
			covInfo.setCovariatesValue(BigDecimal.valueOf(lastBetaMat.getEntry(i, 0)));
			covariatesInfoDAO.insertCovariatesInfo(covInfo);

			i++;
		}

		//covariatesInfoDAO.insertManyCovariatesInfo(list);


	}


	private void makeSumList(int yearRange, double[] betaArr, List<Map<YearEstimateInfo, Double>> pdCacheList,
			List<DDouble> sumList, Map<YearEstimateInfo, Integer> map, int year, int Y, int xIndex) {
		Set<Entry<YearEstimateInfo, Integer>> set;
		set = map.entrySet();
		for (Entry<YearEstimateInfo, Integer> entry  :set) {

			List<Integer> xList = getXList(entry.getKey());

			if (xIndex >=0 && xList.get(xIndex).intValue() == 0) {
				continue;
			}

			double pd = 0;
			if (pdCacheList.get(year).get(entry.getKey()) == null) {
				// キャッシュに値がない
				// PD計算
				pd  = culcPD(betaArr, year, yearRange, xList);
				pdCacheList.get(year).put(entry.getKey(), Double.valueOf(pd));
			} else {
				try {
				pd = pdCacheList.get(year).get(entry.getKey()).doubleValue();
				}catch (Exception e) {

					e.printStackTrace();
					System.out.println("year = " + year);
					System.out.println("key = " + entry.getKey());
				}
			}

			//  共変量(経過年の場合は1)
			int x = 1;

			if (xIndex >=0) {
				//
				x = xList.get(xIndex).intValue();
			}


			double v = (pd - Y) * x * entry.getValue().intValue();
			// 今日
			sumList.add(new DDouble(v));
		}
	}


	private void makeSumListL(int yearRange, double[] betaArr, List<Map<YearEstimateInfo, Double>> pdCacheList,
			List<DDouble> sumList, Map<YearEstimateInfo, Integer> map, int year, int Y) {
		Set<Entry<YearEstimateInfo, Integer>> set;
		set = map.entrySet();
		for (Entry<YearEstimateInfo, Integer> entry  :set) {

			List<Integer> xList = getXList(entry.getKey());

			double pd = 0;
			if (pdCacheList.get(year).get(entry.getKey()) == null) {
				// キャッシュに値がない
				// PD計算
				pd  = culcPD(betaArr, year, yearRange, xList);
				pdCacheList.get(year).put(entry.getKey(), Double.valueOf(pd));
			} else {
				try {
				pd = pdCacheList.get(year).get(entry.getKey()).doubleValue();
				}catch (Exception e) {

					e.printStackTrace();
					System.out.println("year = " + year);
					System.out.println("key = " + entry.getKey());
					//Throw new RuntimeException("fadfa");
				}
			}

			//=∑i=1N(log(PDi)+(1−Yi)・(−β0−∑p=1KβpXi,p)
			double v = (Math.log(pd) - (1 - Y) * culcZ(betaArr, year, yearRange, xList)) * entry.getValue().intValue() ;

			// 今日
			sumList.add(new DDouble(v));
		}
	}


	private List<Integer> getXList(YearEstimateInfo info) {

		List<ConvariateData> list = OneHotMaker.getConvariateDataList(info.getCovariatesMap());

		List<Integer> resultList = new ArrayList<Integer>();

		for (ConvariateData data :list) {
			resultList.add(data.getValue().intValue());
		}

		return resultList;


	}



	private double culcPD(double[] betaArr, int year, int yearRange, List<Integer> xlist ) {
		return 1 / ( 1  +  Math.exp(-culcZ(betaArr, year, yearRange, xlist)));

	}

	private double culcZ(double[] betaArr, int year, int yearRange, List<Integer> xlist) {

		double result = 0;

		// 絶対値が小さいほうから足す
		List<DDouble> resultList = new ArrayList<DDouble>();

		resultList.add(new DDouble(betaArr[year]));

		for (int i = 0; i < xlist.size(); i++) {
//			double v = betaArr[yearRange + 1 + i] * xlist.get(i).intValue();
			double v = betaArr[yearRange + i] * xlist.get(i).intValue();
			resultList.add(new DDouble(v));
		}

		// 昇順ソート
		Collections.sort(resultList);

		for (DDouble data :resultList) {
			result += data.getValue();
		}

		return result;
	}

}



