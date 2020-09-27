package mybatis.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.MemberHistInfo;
import mybaits.vo.YearCount;
import sim.Util;

public class MemberHistInfoDAO {

    public static final String TRUNCATE_MEMBER_HIST_INFO = "truncateMemberHistInfo";

    public static final String CLEAN_MEMBER_HIST_INFO_TBL_ID = "cleanMemberHistInfoTblId";

    public static final String SELECT_ALL_MEMBER_HIST_INFO = "selectAllMemberHistInfo";

    public static final String SELECT_ALL_MEMBER_YEAR_COUNT = "selectAllMemberYearCount";

    public static final String INSERT_MEMBER_HIST_INFO = "insertMemberHistInfo";

    public static final String INSERT_MANY_MEMBER_HIST_INFO = "insertManyMemberHistInfo";

    public void trancateMemberHistInfo() {
    	SqlSession session = Util.getSqlSession();
    	session.delete(TRUNCATE_MEMBER_HIST_INFO);
    	session.delete(CLEAN_MEMBER_HIST_INFO_TBL_ID);
    }

    public List<MemberHistInfo> selectAllMemberHistInfo() {
    	SqlSession session = Util.getSqlSession();
    	List<MemberHistInfo> result = session.selectList(SELECT_ALL_MEMBER_HIST_INFO);
    	return result;
    }

    public List<YearCount> selectAllMemberYearCount() {
    	SqlSession session = Util.getSqlSession();
    	List<YearCount> result = session.selectList(SELECT_ALL_MEMBER_YEAR_COUNT);
    	return result;
    }

    public int insertMemberHistInfo(MemberHistInfo info) {
    	SqlSession session = Util.getSqlSession();
    	int id = session.insert(INSERT_MEMBER_HIST_INFO, info);
    	return id;
    }

    public int insertManyMemberHistInfo(List<MemberHistInfo> list) {
    	SqlSession session = Util.getSqlSession();
    	int id = session.insert(INSERT_MEMBER_HIST_INFO, list);
    	return id;
    }


}
