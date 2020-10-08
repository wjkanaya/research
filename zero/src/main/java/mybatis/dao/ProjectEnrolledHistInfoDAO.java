package mybatis.dao;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.ProjectEnrolledHistInfo;
import sim.Util;

public class ProjectEnrolledHistInfoDAO {

	public static final String INSERT_PROJECT_ENROLLED_HIST_INFO = "insertProjectEnrolledHistInfo";

	public static final String SELECT_PROJECT_ENROLLED_HIST_INFO_BRANCH_NUM = "selectProjectEnrolledHistInfoBranchNum";

	public static final String UPDATE_PROJECT_ENROLLED_HIST_INFO = "updateProjectEnrolledHistInfo";

	public static final String TRUNCATE_PROJECT_ENROLLED_HIST_INFO ="truncateProjectEnrolledHistInfo";

	public static final String CLEAN_PROJECT_ENROLLED_HIST_INFO_TBL_ID = "cleanProjectEnrolledHistInfoTblId";

	public static final String SELECT_COUNT_PROJECT_ENROLLED_HIST_INFO = "selectCountProjectEnrolledHistInfo";

	public static final String SELECT_PROJECT_ENROLLED_HIST_ID =  "selectProjectEnrolledHistId";


	public int insertProjectEnrolledHistInfo(ProjectEnrolledHistInfo info) {
		SqlSession session = Util.getSqlSession();
		int count = session.insert(INSERT_PROJECT_ENROLLED_HIST_INFO, info);
		return count;
	}

	public String selectProjectEnrolledHistId(String memberId, String projectId) {
		ProjectEnrolledHistInfo info = new ProjectEnrolledHistInfo();
		info.setMemberId(memberId);
		info.setProjectId(projectId);
		SqlSession session = Util.getSqlSession();
		String id = session.selectOne(SELECT_PROJECT_ENROLLED_HIST_ID, info);
		return id;
	}


	public Integer selectCountProjectEnrolledHistInfo(String memberId, String projectId) {
		ProjectEnrolledHistInfo info = new ProjectEnrolledHistInfo();
		info.setMemberId(memberId);
		info.setProjectId(projectId);
		SqlSession session = Util.getSqlSession();
		Integer count = session.selectOne(SELECT_COUNT_PROJECT_ENROLLED_HIST_INFO, info);
		return count;
	}


	public Integer selectProjectEnrolledHistInfoBranchNum(String memberId, String projectId) {
		ProjectEnrolledHistInfo info = new ProjectEnrolledHistInfo();
		info.setMemberId(memberId);
		info.setProjectId(projectId);
		SqlSession session = Util.getSqlSession();
		Integer branchNum = session.selectOne(SELECT_PROJECT_ENROLLED_HIST_INFO_BRANCH_NUM, info);
		return branchNum;
	}

	public int updateProjectEnrolledHistInfo(String memberId, String projectId,Date stopDate,
			Integer stopType,Integer enrolledStatus) {
		ProjectEnrolledHistInfo info = new ProjectEnrolledHistInfo();
		info.setMemberId(memberId);
		info.setProjectId(projectId);
		info.setStopDate(stopDate);
		info.setStopType(stopType);
		info.setEnrolledStatus(enrolledStatus);
		SqlSession session = Util.getSqlSession();
		int count = session.update(UPDATE_PROJECT_ENROLLED_HIST_INFO, info);
		return count;
	}

	public void truncateProjectEnrolledHistInfo() {
		SqlSession session = Util.getSqlSession();
		session.delete(TRUNCATE_PROJECT_ENROLLED_HIST_INFO);
		session.delete(CLEAN_PROJECT_ENROLLED_HIST_INFO_TBL_ID);
	}
}
