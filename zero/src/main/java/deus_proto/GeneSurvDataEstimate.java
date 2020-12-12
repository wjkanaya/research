package deus_proto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import mybaits.vo.YearEstimateInfo;
import mybatis.dao.MemberHistInfoDAO;
import sim.HazardConst;
import sim.Util;

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

	List<Map<YearEstimateInfo, Integer>> retireCountMeisaiList = null;
	List<Map<YearEstimateInfo, Integer>> notRetireCountMeisaiList = null;
    int yearRange = 0;
    int betaSize = 0;
    List<Map<YearEstimateInfo, Double>> pdCacheList = null;


//	public static void main(String[] args) {
//		// TODO 自動生成されたメソッド・スタブ
//
//		Util.startTransaction();
//		try {
//			GeneSurvDataEstimate2Jun gene = new GeneSurvDataEstimate2Jun();
//			gene.excute();
//
//		} finally {
//			Util.endTransaction();;
//		}
//
//	}



	public void getData() {
		Util.startTransaction();

		try {
		MemberHistInfoDAO dao = new MemberHistInfoDAO();

		this.list = dao.selectMemberHistYearEstimateInfo();
		} finally {
			Util.endTransaction();;
		}
//	}

		System.out.println(list.size());
	}

	public void makeData() {
		//	List<Integer> retireCountList = new ArrayList<Integer>();
		List<Map<YearEstimateInfo, Integer>> retireCountMeisaiList
		= new ArrayList<Map<YearEstimateInfo, Integer>>();


//		List<Integer> notRetireCountList = new ArrayList<Integer>();
		List<Map<YearEstimateInfo, Integer>> notRetireCountMeisaiList
		= new ArrayList<Map<YearEstimateInfo, Integer>>();

		for (YearEstimateInfo info :list ) {

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

		// beta0は0と仮定
		//int betaSize = 1 + retireCountList.size() + getXList(list.get(0)).size();
		this.betaSize = retireCountMeisaiList.size() + getXList(list.get(0)).size();

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


	private void excute() {

		getData() ;
		makeData() ;

		double[] betaArr = new double[betaSize];
		for (int i = 0; i < betaArr.length; i++) {
			betaArr[i] = 1;
		}

		double sa = 1000;
		int num = 0;
		int max = 100000;
		do {

			delta(null);


			num++;
		} while (sa >= 1);

		System.out.println("なんと1よりさがったよ！");

		//		for (int i = 0; i < retireCountList.size(); i++) {
		//			System.out.println("" + i+":" + notRetireCountList.get(i) +
		//					":" + retireCountList.get(i));
		//		}

	}

	public RealMatrix delta(RealMatrix betaMat) {

		double[][] betaData = betaMat.getData();

		double[] betaArr = new double[betaData.length];

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

		// 経過年数それぞれのデルタbeta
		for (int xIndex = 0; xIndex < HazardConst.RETIRE_X_INDEX_NUM; xIndex++) {
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

	public double L(double[] betaArr) {
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
		List<Integer> result = new ArrayList<Integer>();
		if (info.getX0() != null) {
			result.add(info.getX0());
		}
		if (info.getX1() != null) {
			result.add(info.getX1());
		}
		if (info.getX2() != null) {
			result.add(info.getX2());
		}
		if (info.getX3() != null) {
			result.add(info.getX3());
		}
		if (info.getX4() != null) {
			result.add(info.getX4());
		}

		return result;
	}



	private double culcPD(double[] betaArr, int year, int yearRange, List<Integer> xlist ) {
		return 1 / ( 1  +  Math.exp(-culcZ(betaArr, year, yearRange, xlist)));

	}

	private double culcZ(double[] betaArr, int year, int yearRange, List<Integer> xlist) {

		double result = 0;

		// 絶対値が小さいほうから足す
		List<DDouble> resultList = new ArrayList<DDouble>();

		//resultList.add(new DDouble(betaArr[0]));
//		resultList.add(new DDouble(betaArr[year + 1]));
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



