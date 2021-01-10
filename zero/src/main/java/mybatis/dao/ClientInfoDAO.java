package mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.ClientInfo;
import sim.Util;

public class ClientInfoDAO {

	public static final String INSERT_CLIENT_INFO = "insertClientInfo";

	public static final String TRUNCATE_CLIENT_INFO = "truncateClientInfo";

	public static final String CLEAN_CLIENT_INFO_TBL_ID = "cleanClientInfoTblId";

	public static final String SELECT_GREY_CLIENT_INFO = "selectGreyClientInfo";



	public List<Map> selectGreyClientInfo() {
		SqlSession session = Util.getSqlSession();
		List<Map> list = session.selectList(SELECT_GREY_CLIENT_INFO);
		return list;
	}

	public int insertClientInfo(ClientInfo info) {
		SqlSession session = Util.getSqlSession();
		int count = session.insert(INSERT_CLIENT_INFO, info);
		return count;
	}

	public void truncateClientInfo() {
		SqlSession session = Util.getSqlSession();
		session.delete(TRUNCATE_CLIENT_INFO);
		session.delete(CLEAN_CLIENT_INFO_TBL_ID);
	}
}
