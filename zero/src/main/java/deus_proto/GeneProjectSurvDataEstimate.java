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
import mybaits.vo.ConvariateData;
import mybaits.vo.CovariatesEffectiveInfo;
import mybaits.vo.CovariatesInfo;
import mybaits.vo.PeriodCovariatesInfo;
import mybatis.dao.CovariatesEffectiveInfoDAO;
import mybatis.dao.CovariatesInfoDAO;


// 期間生存情報
public class GeneProjectSurvDataEstimate implements Estimate {

	static Logger logger = LogManager.getLogger(GeneProjectSurvDataEstimate.class);


	// 取得対象生存データ
	GetSurvData targetSurvData = null;

	public GeneProjectSurvDataEstimate(GetSurvData targetSurveData ) {
		this.targetSurvData = targetSurveData;
	}

	List<PeriodCovariatesInfo> convList = null;

	int periodsRange = 0;

    int betaSize = 0;

    List<List<PeriodCovariatesInfo>> quarterConvList = null;

    TreeMap<String, List<PeriodCovariatesInfo>> convTreeMap = null;

    Map<PeriodCovariatesInfo,Map<String,ConvariateData>> convariateDataListMap = null;

    List<Integer> searchPeriodsList = null;

	Map<PeriodCovariatesInfo, Double> zCacheMap = new HashMap<PeriodCovariatesInfo, Double>();

	// β0
	double beta0 = 0;

	public void clear() {
		// β0以外初期化
		this.convList = null;
		this.periodsRange= 0;
		this.betaSize = 0;
		this.quarterConvList = null;
		this.convTreeMap = null;
		this.convariateDataListMap = null;
		this.searchPeriodsList = null;
		this.zCacheMap = new HashMap<PeriodCovariatesInfo, Double>();
	}

	public int getBetaSize() {
		return betaSize;
	}

	public void setBetaSize(int betaSize) {
		this.betaSize = betaSize;
	}

	public void getData(List<Integer> searchPeriodsList,List<String> targetCodeList) {
		this.searchPeriodsList = searchPeriodsList;
		convList = targetSurvData.getData(targetCodeList);

		this.periodsRange = targetSurvData.selectMaxLastPeriod() + 1;

		if (searchPeriodsList != null) {

			this.periodsRange = searchPeriodsList.size();

		} 
	}

	private int getPeriodConvListIdx(int quarter) {
		if (searchPeriodsList == null) {
			return quarter;
		} else {
			for (int i = searchPeriodsList.size() - 1 ; i >= 0; i--) {
				if (searchPeriodsList.get(i).intValue() <= quarter) {
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
		convariateDataListMap = new TreeMap<PeriodCovariatesInfo,Map<String, ConvariateData>>();


		// beta0は0と仮定
		//int betaSize = 1 + retireCountList.size() + getXList(list.get(0)).size();
		this.betaSize = this.periodsRange  + convariateDataListSize;

		//  年毎にデータを分ける
		quarterConvList = new ArrayList<List<PeriodCovariatesInfo>>();
		for (int i = 0;i < periodsRange; i++) {
			quarterConvList.add(new ArrayList<PeriodCovariatesInfo>());
		}

	    convTreeMap =  new TreeMap<String, List<PeriodCovariatesInfo>>();

		for (PeriodCovariatesInfo info :convList) {

			quarterConvList.get(getPeriodConvListIdx(info.getPeriods().intValue())).add(info);

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
						 convTreeMap.put(key, new ArrayList<PeriodCovariatesInfo>());
					 }
					 convTreeMap.get(key).add(info);
				 }
			 }
		}
	}


	private void clearCache() {
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

		for (int year = 0 ;  year < quarterConvList.size(); year++) {
			List<PeriodCovariatesInfo> infoList = quarterConvList.get(year);

			TotalCounter tc = new TotalCounter();
			for (PeriodCovariatesInfo info:infoList) {
				// キャッシュに値がない
				// PD計算
				double pd  = culcPD(betaArr, periodsRange, info);
				double v = (pd - info.getEvent())  * info.getCount();
				tc.set(v);
			}
			deltaBetaList.add(tc.getTotal());
		}

		Set<Entry<String, List<PeriodCovariatesInfo>>> set = convTreeMap.entrySet();
		for (Entry<String, List<PeriodCovariatesInfo>> entry : set) {

			List<PeriodCovariatesInfo> infoList = entry.getValue();

			TotalCounter tc = new TotalCounter();
			for (PeriodCovariatesInfo info:infoList) {
				// キャッシュに値がない
				// PD計算
				double pd  = culcPD(betaArr, periodsRange, info);
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

		for (int year = 0 ;  year < quarterConvList.size(); year++) {
			List<PeriodCovariatesInfo> infoList = quarterConvList.get(year);

			for (PeriodCovariatesInfo info:infoList) {
				// キャッシュに値がない
				// PD計算
				double pd  = culcPD(betaArr, periodsRange, info);
				//				double v = (pd - info.getEvent())  * info.getCount();
				//=∑i=1N(log(PDi)+(1−Yi)・(−β0−∑p=1KβpXi,p)
				double v = (Math.log(pd) - (1 - info.getEvent()) * culcZ(betaArr, periodsRange, info)) * info.getCount() ;
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

		// 期間共変量設定
		String periodCovariatesCode =
			DeusConst.PERIOD_PREFIX + String.format("%03d", targetSurvData.getPeriodMonths());

		// 経過年
		// 共変量有効情報
		covariatesEffectiveInfoDAO.deleteCovariatesEffectiveInfo(targetSurvData.getCulcTargetCode(), periodCovariatesCode);

		CovariatesEffectiveInfo ceInfo = new CovariatesEffectiveInfo();
		// 計算対象コード
		ceInfo.setCulcTargetCode(targetSurvData.getCulcTargetCode());
		// 経過年数
		ceInfo.setCovariatesCode(periodCovariatesCode);
		ceInfo.setEffectStartTime(nowDate);
		ceInfo.setEffectFlg(Boolean.valueOf(true));
		covariatesEffectiveInfoDAO.insertCovariatesEffectiveInfo(ceInfo);

		covariatesInfoDAO.deleteCovariatesInfo(targetSurvData.getCulcTargetCode(), null);

		CovariatesInfo covInfo = new CovariatesInfo();
		covInfo.setCulcTargetCode(targetSurvData.getCulcTargetCode());
		covInfo.setCovariatesCode(DeusConst.C00000);
		covInfo.setEffectStartTime(nowDate);
		covInfo.setCovariatesLabelNum(0);
		covInfo.setCovariatesValue(BigDecimal.valueOf(beta0));
		covariatesInfoDAO.insertCovariatesInfo(covInfo);

		List<CovariatesInfo> list = new ArrayList<CovariatesInfo>();

		for (int i =0; i < periodsRange;i++) {
			covInfo = new CovariatesInfo();
			covInfo.setCulcTargetCode(targetSurvData.getCulcTargetCode());
			covInfo.setCovariatesCode(periodCovariatesCode);
			covInfo.setEffectStartTime(nowDate);

			if (searchPeriodsList != null) {
				covInfo.setCovariatesLabelNum(searchPeriodsList.get(i));
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

		int i = periodsRange;
		// 共変量情報
		for (ConvariateData convData :col) {
			if (!nowCovariatesCode.equals(convData.getConvariateCode())) {
				nowCovariatesCode = convData.getConvariateCode();
				covariatesEffectiveInfoDAO.deleteCovariatesEffectiveInfo(targetSurvData.getCulcTargetCode(), nowCovariatesCode);
				ceInfo = new CovariatesEffectiveInfo();

				// 退職
				ceInfo.setCulcTargetCode(targetSurvData.getCulcTargetCode());
				// 経過年数
				ceInfo.setCovariatesCode(nowCovariatesCode);
				ceInfo.setEffectStartTime(nowDate);
				ceInfo.setEffectFlg(Boolean.valueOf(true));
				covariatesEffectiveInfoDAO.insertCovariatesEffectiveInfo(ceInfo);

				covariatesInfoDAO.deleteCovariatesInfo(targetSurvData.getCulcTargetCode(), nowCovariatesCode);

			}

			covInfo = new CovariatesInfo();
			covInfo.setCulcTargetCode(targetSurvData.getCulcTargetCode());
			covInfo.setCovariatesCode(convData.getConvariateCode());
			covInfo.setEffectStartTime(nowDate);
			covInfo.setCovariatesLabelNum(Integer.valueOf(convData.getConvariateLabel()));
			covInfo.setCovariatesValue(BigDecimal.valueOf(lastBetaMat.getEntry(i, 0)));
			covariatesInfoDAO.insertCovariatesInfo(covInfo);

			i++;
		}
	}

	private double culcPD(double[] betaArr, int yearRange, PeriodCovariatesInfo info) {
		return 1.0 / ( 1.0  +  Math.exp(-culcZ(betaArr, yearRange, info)));

	}


	private double culcZ(double[] betaArr, int yearRange, PeriodCovariatesInfo info) {
		if (!zCacheMap.containsKey(info)) {

			Map<String, ConvariateData> map = convariateDataListMap.get(info);
			ConvariateData[] dataArr = map.values().toArray(new ConvariateData[0]);

			int year = getPeriodConvListIdx(info.getPeriods().intValue());

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



