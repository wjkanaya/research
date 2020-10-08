package mybatis.dao;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.PriceTransitionInfo;
import sim.Util;

public class PriceTransitionInfoDAO {

	public static final String INSERT_PRICE_TRANSITION_INFO ="insertPriceTransitionInfo";

	public static final String TRUNCATE_PRICE_TRANSITION_INFO = "truncatePriceTransitionInfo";

	public static final String CLEAN_PRICE_TRANSITION_INFO_TBL_ID = "cleanPriceTransitionInfoTblId";

	public static final String SELECT_NOW_PRICE = "selectNowPrice";

	public static final String SELECT_COUNT_PRICE_TRANSITION_INFO = "selectCountPriceTransitionInfo";

	public Integer selectCountPriceTransitionInfo(String enrolledHistId) {
		PriceTransitionInfo info = new PriceTransitionInfo();
		info.setEnrolledHistId(enrolledHistId);
		SqlSession session = Util.getSqlSession();
		Integer count = session.selectOne(SELECT_COUNT_PRICE_TRANSITION_INFO, info);
		return count;
	}

	public Integer selectNowPrice(String enrolledHistId) {
		PriceTransitionInfo info = new PriceTransitionInfo();
		info.setEnrolledHistId(enrolledHistId);
		SqlSession session = Util.getSqlSession();
		Integer price = session.selectOne(SELECT_NOW_PRICE, info);
		return price;
	}

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
