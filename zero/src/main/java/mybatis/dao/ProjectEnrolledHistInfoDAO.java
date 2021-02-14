package mybatis.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.CovariatesInfoParam;
import mybaits.vo.ProjectEnrolledHistInfo;
import mybaits.vo.ProjectEnrolledHistYearMonthsInfo;
import mybaits.vo.QuarterCovariatesInfo;
import mybaits.vo.YearMonthsCensorCount;
import sim.Util;

public class ProjectEnrolledHistInfoDAO {

	public static final String INSERT_PROJECT_ENROLLED_HIST_INFO = "insertProjectEnrolledHistInfo";

	public static final String SELECT_PROJECT_ENROLLED_HIST_INFO_BRANCH_NUM = "selectProjectEnrolledHistInfoBranchNum";

	public static final String UPDATE_PROJECT_ENROLLED_HIST_INFO = "updateProjectEnrolledHistInfo";

	public static final String TRUNCATE_PROJECT_ENROLLED_HIST_INFO ="truncateProjectEnrolledHistInfo";

	public static final String CLEAN_PROJECT_ENROLLED_HIST_INFO_TBL_ID = "cleanProjectEnrolledHistInfoTblId";

	public static final String SELECT_COUNT_PROJECT_ENROLLED_HIST_INFO = "selectCountProjectEnrolledHistInfo";

	public static final String SELECT_PROJECT_ENROLLED_HIST_ID =  "selectProjectEnrolledHistId";

	public static final String SELECT_ALL_PROJECT_ENROLLED_HIST_YEAR_MONTHS_INFO = "selectAllProjectEnrolledHistYearMonthsInfo" ;

	public static final String SELECT_ALL_PROJECT_ENROLLED_HIST_INFO_COUNT = "selectAllProjectEnrolledHistInfoCount";

	public static final String SELECT_ALL_PROJECT_ENROLLED_HIST_YEAR_MONTHS_COUNT_INFO = "selectAllProjectEnrolledHistYearMonthsCountInfo";

	public static final String SELECT_PROJECT_ENROLLED_HIST_QUARTER_COVARIATES_INFO_MAP  = "selectProjectEnrolledHistQuarterCovariatesInfoMap";


	public List<QuarterCovariatesInfo> selectProjectEnrolledHistQuarterCovariatesInfoMap(CovariatesInfoParam target) {
		SqlSession session = Util.getSqlSession();
		List<QuarterCovariatesInfo> list = session.selectList(SELECT_PROJECT_ENROLLED_HIST_QUARTER_COVARIATES_INFO_MAP, target);
		return list;
	}




	public List<YearMonthsCensorCount> selectAllProjectEnrolledHistYearMonthsCountInfo() {
		SqlSession session = Util.getSqlSession();
		List<YearMonthsCensorCount> list = session.selectList(SELECT_ALL_PROJECT_ENROLLED_HIST_YEAR_MONTHS_COUNT_INFO);
		return list;
	}


	public int selectAllProjectEnrolledHistInfoCount() {
		SqlSession session = Util.getSqlSession();
		Integer count = session.selectOne(SELECT_ALL_PROJECT_ENROLLED_HIST_INFO_COUNT);
		return count.intValue();
	}


	public int insertProjectEnrolledHistInfo(ProjectEnrolledHistInfo info) {
		SqlSession session = Util.getSqlSession();
		int count = session.insert(INSERT_PROJECT_ENROLLED_HIST_INFO, info);
		return count;
	}

	public List<ProjectEnrolledHistYearMonthsInfo> selectAllProjectEnrolledHistYearMonthsInfo() {
		SqlSession session = Util.getSqlSession();
		List<ProjectEnrolledHistYearMonthsInfo> list = session.selectList(SELECT_ALL_PROJECT_ENROLLED_HIST_YEAR_MONTHS_INFO);
		return list;
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

	public int updateProjectEnrolledHistInfo(String memberId, String projectId,Date stopDate,Integer stopMemberMonth,
			Integer stopType,Integer enrolledStatus) {
		ProjectEnrolledHistInfo info = new ProjectEnrolledHistInfo();
		info.setMemberId(memberId);
		info.setProjectId(projectId);
		info.setStopDate(stopDate);
		info.setStopMemberMonths(stopMemberMonth);
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
