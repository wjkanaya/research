package mybatis.dao;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.ProjectInfo;
import sim.Util;

public class ProjectInfoDAO {

	public static final String INSERT_PROJECT_INFO = "insertProjectInfo";

	public static final String TRUNCATE_PROJECT_INFO = "truncateProjectInfo";

	public static final String CLEAN_PROJECT_INFO_TBL_ID = "cleanProjectInfoTblId";

	public static final String UPDATE_PROJECT_INFO = "updateProjectInfo";

	public static final String SELECT_PROJECT_INFO = "selectProjectInfo";

	public ProjectInfo selectProjectInfo(String projectId) {
		ProjectInfo param = new ProjectInfo();
		param.setProjectId(projectId);
		SqlSession session = Util.getSqlSession();
		ProjectInfo info = session.selectOne(SELECT_PROJECT_INFO, param);
		return info;
	}

	public int insertProjectInfo(ProjectInfo info) {
		SqlSession session = Util.getSqlSession();
		int id = session.insert(INSERT_PROJECT_INFO, info);
		return id;
	}

	public void truncateProjectInfo() {
		SqlSession session = Util.getSqlSession();
		session.delete(TRUNCATE_PROJECT_INFO);
		session.delete(CLEAN_PROJECT_INFO_TBL_ID);
	}

	public int updateProjectInfo(String projectId,Integer projectStatus, Date endDate) {
		ProjectInfo info = new ProjectInfo();
		info.setProjectId(projectId);
		info.setProjectStatus(projectStatus);
		info.setEndDate(endDate);
		SqlSession session = Util.getSqlSession();
		int result = session.update(UPDATE_PROJECT_INFO,info);
		return result;
	}

}
