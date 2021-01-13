package deus_proto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
//	est.getData();
//	est.makeData();
//est.getBetaSize()
//est.delta(nowPMat)
//est.L
//
//est.setCovariatesValue(nowPMat);




	public int getBetaSize() {
		return betaSize;
	}

	public void setBetaSize(int betaSize) {
		this.betaSize = betaSize;
	}


	List<YearCovariatesInfo> convList = null;

	int yearRange = 0;

    int betaSize = 0;

    List<List<YearCovariatesInfo>> yearConvList = null;


    TreeMap<String, List<YearCovariatesInfo>> convTreeMap = null;

    Map<YearCovariatesInfo,List<ConvariateData>> convariateDataListMap = null;

	public void getData() {
		MemberHistInfoDAO dao = new MemberHistInfoDAO();

		// 何か年か？
		this.yearRange = dao.selectMaxLastYear() + 1;

		YearCovariatesInfoParam param = new YearCovariatesInfoParam();
		param.setSex(true);
        convList = dao.selectMemberHistYearCovariatesInfoMap(param);


        logger.debug(convList.size());
	}

	public void makeData() {

		// 何か年か？
		int convariateDataListSize =
				OneHotMaker.getConvariateDataList(convList.get(0).getCovariatesMap()).size();

		// 共変量リスト
		for (YearCovariatesInfo info :convList) {
			convariateDataListMap.put(info, OneHotMaker.getConvariateDataList(info.getCovariatesMap()));
		}

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

			yearConvList.get(info.getYears().intValue()).add(info);

			// 共変量リスト
			 List<ConvariateData> cvDataList = convariateDataListMap.get(info);

			 for (ConvariateData cdata: cvDataList) {
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
//		for (Map<YearEstimateInfo, Double> map :this.pdCacheList) {
//			map.clear();
//		}
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
				double pd  = culcPD(betaArr, year, yearRange, convariateDataListMap.get(info));
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
				double pd  = culcPD(betaArr, info.getYears(), yearRange, convariateDataListMap.get(info));
				double v = (pd - info.getEvent()) * info.getCovariatesMap().get(entry.getKey()).intValue() * info.getCount();
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
				double pd  = culcPD(betaArr, year, yearRange, convariateDataListMap.get(info));
				//				double v = (pd - info.getEvent())  * info.getCount();
				//=∑i=1N(log(PDi)+(1−Yi)・(−β0−∑p=1KβpXi,p)
				double v = (Math.log(pd) - (1 - info.getEvent()) * culcZ(betaArr, year, yearRange, convariateDataListMap.get(info))) * info.getCount() ;
				tc.set(v);
			}
		}

		// 下に凸関数にしたい
		return -tc.getTotal();
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

		List<ConvariateData> convariateDataList = OneHotMaker.getConvariateDataList(convList.get(0).getCovariatesMap());

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
	}

	private double culcPD(double[] betaArr, int year, int yearRange, List<ConvariateData> convariateDataList) {
		return 1.0 / ( 1.0  +  Math.exp(-culcZ(betaArr, year, yearRange, convariateDataList)));

	}

	private double culcZ(double[] betaArr, int year, int yearRange, List<ConvariateData> convariateDataList) {

		TotalCounter tc = new TotalCounter();
		tc.set(betaArr[year]);
		for (int i = 0; i < convariateDataList.size(); i++) {
			double v = betaArr[yearRange + i] * convariateDataList.get(i).getValue().intValue();
			tc.set(v);
		}

		return tc.getTotal();
	}






}



