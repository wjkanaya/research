package mybatis.dao;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.ProjectMemberNumInfo;
import sim.Util;

public class ProjectMemberNumInfoDAO {

	public static final String INSERT_PROJECT_MEMBER_NUM_INFO = "insertProjectMemberNumInfo";

	public static final String TRUNCATE_PROJECT_MEMBER_NUM_INFO = "truncateProjectMemberNumInfo";

	public static final String CLEAN_PROJECT_MEMBER_NUM_INFO_TBL_ID = "cleanProjectMemberNumInfoTblId";

	public static final String SELECT_COUNT_PROJECT_MEMBER_NUM_INFO = "selectCountProjectMemberNumInfo";

	public static final String INSERT_SABUN_PROJECT_MEMBER_NUM_INFO = "insertSabunProjectMemberNumInfo";

	public static final String  SELECT_LAST_PROJECT_MEMBER_NUM_INFO = "selectLastProjectMemberNumInfo";

	public ProjectMemberNumInfo selectLastProjectMemberNumInfo(String projectId) {
		ProjectMemberNumInfo param = new ProjectMemberNumInfo();
		param.setProjectId(projectId);
		SqlSession session = Util.getSqlSession();
		ProjectMemberNumInfo info = session.selectOne(SELECT_LAST_PROJECT_MEMBER_NUM_INFO, param);
		return info;
	}

	public int selectCountProjectMemberNumInfo(String projectId) {
		ProjectMemberNumInfo info = new ProjectMemberNumInfo();
		info.setProjectId(projectId);
		SqlSession session = Util.getSqlSession();
		Integer count = session.selectOne(SELECT_COUNT_PROJECT_MEMBER_NUM_INFO, info);
		return count.intValue();
	}

	public int insertSabunProjectMemberNumInfo(ProjectMemberNumInfo info) {
		SqlSession session = Util.getSqlSession();
		int count = session.insert(INSERT_SABUN_PROJECT_MEMBER_NUM_INFO, info);
		return count;
	}

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


