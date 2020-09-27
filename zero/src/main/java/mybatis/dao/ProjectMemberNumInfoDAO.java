package mybatis.dao;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.ProjectMemberNumInfo;
import sim.Util;

public class ProjectMemberNumInfoDAO {

	public static final String INSERT_PROJECT_MEMBER_NUM_INFO = "insertProjectMemberNumInfo";

	public static final String TRUNCATE_PROJECT_MEMBER_NUM_INFO = "truncateProjectMemberNumInfo";

	public static final String CLEAN_PROJECT_MEMBER_NUM_INFO_TBL_ID = "cleanProjectMemberNumInfoTblId";

	public int insertProjectMemberNumInfo(ProjectMemberNumInfo info) {
		SqlSession session = Util.getSqlSession();
		int count = session.insert(INSERT_PROJECT_MEMBER_NUM_INFO, info);
		return count;
	}

	public void truncateProjectMemberNumInfo() {
		SqlSession session = Util.getSqlSession();
		session.delete(TRUNCATE_PROJECT_MEMBER_NUM_INFO);
		session.delete(CLEAN_PROJECT_MEMBER_NUM_INFO_TBL_ID);
	}

}


