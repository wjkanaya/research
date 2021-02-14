package mybatis.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import deus.enums.RetirementType;
import mybaits.vo.CovariatesInfoParam;
import mybaits.vo.MemberHistInfo;
import mybaits.vo.MemberHistPriceInfo;
import mybaits.vo.YearCount;
import mybaits.vo.YearCovariatesInfo;
import mybaits.vo.YearEstimateInfo;
import mybaits.vo.YearEstimateInfoPre;
import mybaits.vo.YearMonthsCensorCount;
import sim.Util;

public class MemberHistInfoDAO {

    public static final String TRUNCATE_MEMBER_HIST_INFO = "truncateMemberHistInfo";

    public static final String CLEAN_MEMBER_HIST_INFO_TBL_ID = "cleanMemberHistInfoTblId";

    public static final String SELECT_ALL_MEMBER_HIST_INFO = "selectAllMemberHistInfo";

    public static final String SELECT_ALL_MEMBER_YEAR_COUNT = "selectAllMemberYearCount";

    public static final String SELECT_ALL_MEMBER_HIST_INFO_COUNT = "selectAllMemberHistInfoCount";

    public static final String SELECT_ALL_MEMBER_HIST_YEAR_MONTHS_COUNT_INFO = "selectAllMemberHistYearMonthsCountInfo";

    public static final String INSERT_MEMBER_HIST_INFO = "insertMemberHistInfo";

    public static final String INSERT_MANY_MEMBER_HIST_INFO = "insertManyMemberHistInfo";

    public static final String UPDATE_MEMBER_HIST_INFO = "updateMemberHistInfo";

    public static final String SELECT_ALL_MEMBER_PROJECT_ENROLLED_HIST_AND_PRICE
        = "selectAllMemberProjectEnrolledHistAndPrice";

    public static final String SELECT_MEMBER_HIST_YEAR_ESTIMATE_INFO = "selectMemberHistYearEstimateInfo";

    public static final String SELECT_MEMBER_HIST_YEAR_ESTIMATE_INFO_MAP = "selectMemberHistYearEstimateInfoMap";

    public static final String SELECT_MEMBER_HIST_YEAR_ESTIMATE_INFO_MAP2 = "selectMemberHistYearEstimateInfoMap2";

    public static final String  SELECT_MEMBER_HIST_YEAR_COVARIATES_INFO_CLIENT_MAP
    	= "selectMemberHistYearCovariatesInfoClientMap";

    public static final String SELECT_MEMBER_HIST_YEAR_COVARIATES_INFO_MAP = "selectMemberHistYearCovariatesInfoMap";

    public static final String  SELECT_MAX_LAST_YEAR = "selectMaxLastYear";

    public void trancateMemberHistInfo() {
    	SqlSession session = Util.getSqlSession();
    	session.delete(TRUNCATE_MEMBER_HIST_INFO);
    	session.delete(CLEAN_MEMBER_HIST_INFO_TBL_ID);
    }

	public int selectMaxLastYear() {
		SqlSession session = Util.getSqlSession();
		Integer year = session.selectOne(SELECT_MAX_LAST_YEAR);
		return year.intValue();
	}

	public int selectAllMemberHistInfoCount() {
		SqlSession session = Util.getSqlSession();
		Integer count = session.selectOne(SELECT_ALL_MEMBER_HIST_INFO_COUNT);
		return count.intValue();
	}

	public List<YearEstimateInfoPre> selectMemberHistYearEstimateInfo() {
		SqlSession session = Util.getSqlSession();
		List<YearEstimateInfoPre> list = session.selectList(SELECT_MEMBER_HIST_YEAR_ESTIMATE_INFO);
		return list;
	}

	public List<YearCovariatesInfo> selectMemberHistYearCovariatesInfoMap(CovariatesInfoParam target) {
		SqlSession session = Util.getSqlSession();
		List<YearCovariatesInfo> list = session.selectList(SELECT_MEMBER_HIST_YEAR_COVARIATES_INFO_MAP, target);
		return list;
	}

	public List<YearCovariatesInfo> selectMemberHistYearCovariatesInfoClientMap(CovariatesInfoParam target) {
		SqlSession session = Util.getSqlSession();
		List<YearCovariatesInfo> list = session.selectList(SELECT_MEMBER_HIST_YEAR_COVARIATES_INFO_CLIENT_MAP, target);
		return list;
	}


	public List<YearEstimateInfo> selectMemberHistYearEstimateInfoMap() {
		SqlSession session = Util.getSqlSession();
		List<YearEstimateInfo> list = session.selectList(SELECT_MEMBER_HIST_YEAR_ESTIMATE_INFO_MAP);
		return list;
	}

	public List<YearEstimateInfo> selectMemberHistYearEstimateInfoMap2() {
		SqlSession session = Util.getSqlSession();
		List<YearEstimateInfo> list = session.selectList(SELECT_MEMBER_HIST_YEAR_ESTIMATE_INFO_MAP2);
		return list;
	}



	public List<YearMonthsCensorCount> selectAllMemberHistYearMonthsCountInfo() {
		SqlSession session = Util.getSqlSession();
		List<YearMonthsCensorCount> list = session.selectList(SELECT_ALL_MEMBER_HIST_YEAR_MONTHS_COUNT_INFO);
		return list;
	}

    public List<MemberHistPriceInfo> selectAllMemberProjectEnrolledHistAndPrice() {
    	SqlSession session = Util.getSqlSession();
    	List<MemberHistPriceInfo> result = session.selectList(SELECT_ALL_MEMBER_PROJECT_ENROLLED_HIST_AND_PRICE);
    	return result;
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
    	int count = session.insert(INSERT_MEMBER_HIST_INFO, info);
    	return count;
    }

    public int insertManyMemberHistInfo(List<MemberHistInfo> list) {
    	SqlSession session = Util.getSqlSession();
    	int count = session.insert(INSERT_MANY_MEMBER_HIST_INFO, list);
    	return count;
    }

    public int updateMemberHistInfo(String memberId,RetirementType retirementType,Date retirementDate) {
    	MemberHistInfo info = new MemberHistInfo();
    	info.setMemberId(memberId);
    	info.setRetirementType(retirementType.getInteger());
    	info.setStatus(1); // 1:退社
    	info.setRetirementDate(retirementDate);
    	SqlSession session = Util.getSqlSession();
    	int count = session.insert(UPDATE_MEMBER_HIST_INFO, info);
    	return count;
    }




}
