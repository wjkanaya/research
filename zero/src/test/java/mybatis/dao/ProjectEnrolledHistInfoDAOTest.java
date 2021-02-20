package mybatis.dao;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import mybaits.vo.ClientInfo;
import mybaits.vo.CovariatesInfoParam;
import mybaits.vo.MemberHistInfo;
import mybaits.vo.QuarterCovariatesInfo;
import sim.Util;

public class ProjectEnrolledHistInfoDAOTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		Util.startTransaction();
	}

	protected void tearDown() throws Exception {
		Util.endTransaction();

		super.tearDown();
	}

	public void testSelectProjectEnrolledHistQuarterCovariatesInfoMap() {
		CovariatesInfoParam param = new CovariatesInfoParam();

		ClientInfo info = new ClientInfo();
		info.setClientId("K08031");
		List<ClientInfo>  clist = new ArrayList<ClientInfo>();
		clist.add(info);
		param.setClientList(clist);
		List<MemberHistInfo> mlist = new ArrayList<MemberHistInfo>();
		param.setMemberList(mlist);


		ProjectEnrolledHistInfoDAO mhiDao =new ProjectEnrolledHistInfoDAO();
		List<QuarterCovariatesInfo> list = mhiDao.selectProjectEnrolledHistQuarterCovariatesInfoMap(param);

		MemberHistInfo minfo = new MemberHistInfo();
		minfo.setMemberId("M0001");
		mlist.add(minfo);
		param.setMemberList(mlist);
		clist.clear();
		param.setClientList(clist);

		list = mhiDao.selectProjectEnrolledHistQuarterCovariatesInfoMap(param);

		mlist.clear();
		param.setMemberList(mlist);
		clist.clear();
		param.setClientList(clist);

		list = mhiDao.selectProjectEnrolledHistQuarterCovariatesInfoMap(param);

	}

}
