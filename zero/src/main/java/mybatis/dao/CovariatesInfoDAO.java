package mybatis.dao;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.CovariatesInfo;
import sim.Util;

public class CovariatesInfoDAO {

	public static final String DELETE_COVARIATES_INFO = "deleteCovariatesInfo";
	public static final String INSERT_COVARIATES_INFO ="insertCovariatesInfo";
	public static final String TRUNCATE_COVARIATES_INFO = "truncateCovariatesInfo";
	public static final String CLEAN_COVARIATES_INFO_TBL_ID = "cleanCovariatesInfoTblId";

	public int deleteCovariatesInfo(
			String culcTargetCode, // 計算対象コード
			String covariatesCode  // 共変量コード
			) {
		CovariatesInfo info = new CovariatesInfo();
		info.setCulcTargetCode(culcTargetCode);
		info.setCovariatesCode(covariatesCode);
		SqlSession session = Util.getSqlSession();
		int count = session.insert(DELETE_COVARIATES_INFO, info);
		return count;
	}

	public int insertCovariatesInfo(CovariatesInfo info) {
		SqlSession session = Util.getSqlSession();
		int count = session.insert(INSERT_COVARIATES_INFO, info);
		return count;
	}

	public void truncateCovariatesInfo() {
		SqlSession session = Util.getSqlSession();
		session.delete(TRUNCATE_COVARIATES_INFO);
		session.delete(CLEAN_COVARIATES_INFO_TBL_ID);
	}
}
