package deus_proto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import deus_proto.util.OneHotMaker;
import mybaits.vo.ClientInfo;
import mybaits.vo.ConvariateData;
import mybaits.vo.CovariatesEffectiveInfo;
import mybaits.vo.CovariatesInfo;
import mybaits.vo.YearCovariatesInfo;
import mybaits.vo.YearCovariatesInfoParam;
import mybatis.dao.CovariatesEffectiveInfoDAO;
import mybatis.dao.CovariatesInfoDAO;
//import mybaits.vo.YearEstimateInfoPre;
import mybatis.dao.MemberHistInfoDAO;

public class GeneSurvDataEstimate2 {

	static Logger logger = LogManager.getLogger(GeneSurvDataEstimate2.class);



	List<YearCovariatesInfo> convList = null;

	int yearRange = 0;

    int betaSize = 0;

    List<List<YearCovariatesInfo>> yearConvList = null;

    TreeMap<String, List<YearCovariatesInfo>> convTreeMap = null;

    Map<YearCovariatesInfo,Map<String,ConvariateData>> convariateDataListMap = null;


    List<Integer> searchYearsList = null;


	Map<YearCovariatesInfo, Double> zCacheMap = new HashMap<YearCovariatesInfo, Double>();

	// β0
	double beta0 = 0;

	public void clear() {
		// β0以外初期化
		this.convList = null;
		this.yearRange= 0;
		this.betaSize = 0;
		this.yearConvList = null;
		this.convTreeMap = null;
		this.convariateDataListMap = null;
		this.searchYearsList = null;
		this.zCacheMap = new HashMap<YearCovariatesInfo, Double>();
	}

	public int getBetaSize() {
		return betaSize;
	}

	public void setBetaSize(int betaSize) {
		this.betaSize = betaSize;
	}


	public void getData(List<String> targetCodeList) {

		YearCovariatesInfoParam param = new YearCovariatesInfoParam();
		param.setClientList(new ArrayList<ClientInfo>());

		// 性別判定コード:"C00002";


		for (String code :targetCodeList) {
			if ("C00002".equals(code)) {
				param.setSex(true);
			} else {
				ClientInfo info = new ClientInfo();
				info.setClientId(code);
				param.getClientList().add(info);
			}
		}

		MemberHistInfoDAO dao = new MemberHistInfoDAO();

		// 何か年か？
		this.yearRange = dao.selectMaxLastYear() + 1;

        if (param.getClientList().size() > 0) {
        	convList = dao.selectMemberHistYearCovariatesInfoClientMap(param);
        } else {
        	convList = dao.selectMemberHistYearCovariatesInfoMap(param);
        }

        logger.debug(convList.size());
	}

	public void getData(List<Integer> searchYearsList,List<String> targetCodeList) {
		this.searchYearsList = searchYearsList;
		getData(targetCodeList);

		if (searchYearsList != null) {

			this.yearRange = searchYearsList.size();

		} // searchYearsListがnull： 1年づつ共変量に

	}

	private int getyearConvListIdx(int year) {
		if (searchYearsList == null) {
			return year;
		} else {
			for (int i = searchYearsList.size() - 1 ; i >= 0; i--) {
				if (searchYearsList.get(i).intValue() <= year) {
					return i;
				}
			}
		}
		return 0;
	}

	public void makeData() {

		// 何か年か？
		int convariateDataListSize =
				OneHotMaker.getConvariateDataListMap(convList.get(0).getCovariatesMap()).size();
		convariateDataListMap = new TreeMap<YearCovariatesInfo,Map<String, ConvariateData>>();


		// beta0は0と仮定
		//int betaSize = 1 + retireCountList.size() + getXList(list.get(0)).size();
		this.betaSize = this.yearRange  + convariateDataListSize;

		//  年毎にデータを分ける
		yearConvList = new ArrayList<List<YearCovariatesInfo>>();
		for (int i = 0;i < yearRange; i++) {
			yearConvList.add(new ArrayList<YearCovariatesInfo>());
		}

	    convTreeMap =  new TreeMap<String, List<YearCovariatesInfo>>();

		for (YearCovariatesInfo info :convList) {

			yearConvList.get(getyearConvListIdx(info.getYears().intValue())).add(info);

			 if (convariateDataListMap.get(info) == null) {
			 // 共変量リスト
					convariateDataListMap.put(info, OneHotMaker.getConvariateDataListMap(info.getCovariatesMap()));
			 }


			// 共変量リスト
			 Collection<ConvariateData> cvDataListMap = convariateDataListMap.get(info).values();

			 for (ConvariateData cdata: cvDataListMap) {
				 if (!BigDecimal.valueOf(0).equals(cdata.getValue())) {
					 String key = cdata.getConvariateCode() + "_" + cdata.getConvariateLabel();
					 if (convTreeMap.get(key) == null) {
						 convTreeMap.put(key, new ArrayList<YearCovariatesInfo>());
					 }
					 convTreeMap.get(key).add(info);
				 }
			 }
		}


	}


	public void clearCache() {
		zCacheMap.clear();
	}

	public RealMatrix delta(RealMatrix betaMat) {
		clearCache();

		double[][] betaData = betaMat.getData();

		double[] betaArr = new double[betaData.length];

		for (int i = 0; i < betaData.length; i++) {
			betaArr[i] = betaData[i][0];
		}

		// 1階微分

		List<Double> deltaBetaList = new ArrayList<Double>(betaArr.length);

		for (int year = 0 ;  year < yearConvList.size(); year++) {
			List<YearCovariatesInfo> infoList = yearConvList.get(year);

			TotalCounter tc = new TotalCounter();
			for (YearCovariatesInfo info:infoList) {
				// キャッシュに値がない
				// PD計算
				double pd  = culcPD(betaArr, yearRange, info);
				double v = (pd - info.getEvent())  * info.getCount();
				tc.set(v);
			}
			deltaBetaList.add(tc.getTotal());
		}

		Set<Entry<String, List<YearCovariatesInfo>>> set = convTreeMap.entrySet();
		for (Entry<String, List<YearCovariatesInfo>> entry : set) {

			List<YearCovariatesInfo> infoList = entry.getValue();

			TotalCounter tc = new TotalCounter();
			for (YearCovariatesInfo info:infoList) {
				// キャッシュに値がない
				// PD計算
				double pd  = culcPD(betaArr, yearRange, info);
				double v = (pd - info.getEvent()) * convariateDataListMap.get(info).get(entry.getKey()).getValue().intValue()* info.getCount();
				tc.set(v);
			}
			deltaBetaList.add(tc.getTotal());
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

		TotalCounter tc = new TotalCounter();

		for (int year = 0 ;  year < yearConvList.size(); year++) {
			List<YearCovariatesInfo> infoList = yearConvList.get(year);

			for (YearCovariatesInfo info:infoList) {
				// キャッシュに値がない
				// PD計算
				double pd  = culcPD(betaArr, yearRange, info);
				//				double v = (pd - info.getEvent())  * info.getCount();
				//=∑i=1N(log(PDi)+(1−Yi)・(−β0−∑p=1KβpXi,p)
				double v = (Math.log(pd) - (1 - info.getEvent()) * culcZ(betaArr, yearRange, info)) * info.getCount() ;
				tc.set(v);
			}
		}

		// 下に凸関数にしたい
		return -tc.getTotal();
	}



	public void setCovariatesValueBeta0(RealMatrix lastBetaMat) {
		beta0 = lastBetaMat.getEntry(0, 0);
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

		covariatesInfoDAO.deleteCovariatesInfo(DeusConst.CT0001, null);


		CovariatesInfo covInfo = new CovariatesInfo();
		covInfo.setCulcTargetCode(DeusConst.CT0001);
		covInfo.setCovariatesCode(DeusConst.C00000);
		covInfo.setEffectStartTime(nowDate);
		covInfo.setCovariatesLabelNum(0);
		covInfo.setCovariatesValue(BigDecimal.valueOf(beta0));
		covariatesInfoDAO.insertCovariatesInfo(covInfo);



		List<CovariatesInfo> list = new ArrayList<CovariatesInfo>();


		for (int i =0; i < yearRange;i++) {
			covInfo = new CovariatesInfo();
			covInfo.setCulcTargetCode(DeusConst.CT0001);
			covInfo.setCovariatesCode(DeusConst.C00001);
			covInfo.setEffectStartTime(nowDate);

			if (searchYearsList != null) {
				covInfo.setCovariatesLabelNum(searchYearsList.get(i));
			} else {
				covInfo.setCovariatesLabelNum(Integer.valueOf(i));
			}

			covInfo.setCovariatesValue(BigDecimal.valueOf(lastBetaMat.getEntry(i, 0)));

			covariatesInfoDAO.insertCovariatesInfo(covInfo);

			//list.add(covInfo);
		}

		Map<String, ConvariateData> convariateDataListMap = OneHotMaker.getConvariateDataListMap(convList.get(0).getCovariatesMap());

		String nowCovariatesCode = "";

		Collection<ConvariateData> col =  convariateDataListMap.values();

		int i = yearRange;
		// 共変量情報
		for (ConvariateData convData :col) {
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

			covInfo = new CovariatesInfo();
			covInfo.setCulcTargetCode(DeusConst.CT0001);
			covInfo.setCovariatesCode(convData.getConvariateCode());
			covInfo.setEffectStartTime(nowDate);
			covInfo.setCovariatesLabelNum(Integer.valueOf(convData.getConvariateLabel()));
			covInfo.setCovariatesValue(BigDecimal.valueOf(lastBetaMat.getEntry(i, 0)));
			covariatesInfoDAO.insertCovariatesInfo(covInfo);

			i++;
		}
	}

	private double culcPD(double[] betaArr, int yearRange, YearCovariatesInfo info) {
		return 1.0 / ( 1.0  +  Math.exp(-culcZ(betaArr, yearRange, info)));

	}


	private double culcZ(double[] betaArr, int yearRange, YearCovariatesInfo info) {
		if (!zCacheMap.containsKey(info)) {

			Map<String, ConvariateData> map = convariateDataListMap.get(info);
			ConvariateData[] dataArr = map.values().toArray(new ConvariateData[0]);

			int year = getyearConvListIdx(info.getYears().intValue());

			TotalCounter tc = new TotalCounter();
			tc.set(beta0);
			tc.set(betaArr[year]);
			for (int i = 0; i < dataArr.length; i++) {
				double v = betaArr[yearRange + i] * dataArr[i].getValue().intValue();
				tc.set(v);
			}
			zCacheMap.put(info, Double.valueOf(tc.getTotal()));
		}
		return zCacheMap.get(info);
	}


}



