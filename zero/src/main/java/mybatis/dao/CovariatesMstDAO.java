package mybatis.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.CovariatesMst;
import sim.Util;

public class CovariatesMstDAO {

	public static final String SELECT_COVARIATES_MST = "selectCovariatesMst";

	public static final String SELECT_ALL_COVARIATES_MST = "selectAllCovariatesMst";

	public CovariatesMst selectCovariatesMst(String covariatesCode) {
		CovariatesMst param = new CovariatesMst();
		param.setCovariatesCode(covariatesCode);
		SqlSession session = Util.getSqlSession();
		CovariatesMst info = session.selectOne(SELECT_COVARIATES_MST, param);
		return info;
	}

	public List<CovariatesMst> selectAllCovariatesMst() {
		SqlSession session = Util.getSqlSession();
		List<CovariatesMst> list = session.selectList(SELECT_ALL_COVARIATES_MST);
		return list;
	}

}
