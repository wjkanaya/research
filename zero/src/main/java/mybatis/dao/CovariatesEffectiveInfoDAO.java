package mybatis.dao;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.CovariatesEffectiveInfo;
import sim.Util;

public class CovariatesEffectiveInfoDAO {

	public static final String DELETE_COVARIATES_EFFECTIVE_INFO = "deleteCovariatesEffectiveInfo";
	public static final String INSERT_COVARIATES_EFFECTIVE_INFO ="insertCovariatesEffectiveInfo";
	public static final String TRUNCATE_COVARIATES_EFFECTIVE_INFO = "truncateCovariatesEffectiveInfo";
	public static final String CLEAN_COVARIATES_EFFECTIVE_INFO_TBL_ID = "cleanCovariatesEffectiveInfoTblId";

	public int deleteCovariatesEffectiveInfo(
			String culcTargetCode, // 計算対象コード
			String covariatesCode  // 共変量コード
			) {
		CovariatesEffectiveInfo info = new CovariatesEffectiveInfo();
		info.setCulcTargetCode(culcTargetCode);
		info.setCovariatesCode(covariatesCode);
		SqlSession session = Util.getSqlSession();
		int count = session.insert(DELETE_COVARIATES_EFFECTIVE_INFO, info);
		return count;
	}

	public int insertCovariatesEffectiveInfo(CovariatesEffectiveInfo info) {
		SqlSession session = Util.getSqlSession();
		int count = session.insert(INSERT_COVARIATES_EFFECTIVE_INFO, info);
		return count;
	}

	public void truncateCovariatesEffectiveInfo() {
		SqlSession session = Util.getSqlSession();
		session.delete(TRUNCATE_COVARIATES_EFFECTIVE_INFO);
		session.delete(CLEAN_COVARIATES_EFFECTIVE_INFO_TBL_ID);
	}
}
