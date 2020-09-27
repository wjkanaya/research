package mybatis.dao;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.PriceTransitionInfo;
import sim.Util;

public class PriceTransitionInfoDAO {

	public static final String INSERT_PRICE_TRANSITION_INFO ="insertPriceTransitionInfo";

	public static final String TRUNCATE_PRICE_TRANSITION_INFO = "truncatePriceTransitionInfo";

	public static final String CLEAN_PRICE_TRANSITION_INFO_TBL_ID = "cleanPriceTransitionInfoTblId";

	public int insertPriceTransitionInfo(PriceTransitionInfo info) {
		SqlSession session = Util.getSqlSession();
		int count = session.insert(INSERT_PRICE_TRANSITION_INFO, info);
		return count;
	}
	
	public void truncatePriceTransitionInfo() {
		SqlSession session = Util.getSqlSession();
		session.delete(TRUNCATE_PRICE_TRANSITION_INFO);
		session.delete(CLEAN_PRICE_TRANSITION_INFO_TBL_ID);	
	}

}
