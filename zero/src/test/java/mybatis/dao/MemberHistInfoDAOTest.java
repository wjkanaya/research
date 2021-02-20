package mybatis.dao;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import mybaits.vo.ClientInfo;
import mybaits.vo.CovariatesInfoParam;
import mybaits.vo.MemberHistInfo;
import mybaits.vo.YearCovariatesInfo;
import sim.Util;

public class MemberHistInfoDAOTest extends TestCase {
	protected void setUp() throws Exception {

		super.setUp();
		Util.startTransaction();
	}

	protected void tearDown() throws Exception {

		Util.endTransaction();;

		super.tearDown();
	}
	public void testSelectMemberHistYearCovariatesInfoMap() {
		CovariatesInfoParam param = new CovariatesInfoParam();
		param.setSex(true);

		MemberHistInfoDAO mhiDao =new MemberHistInfoDAO();
		List<YearCovariatesInfo> list = mhiDao.selectMemberHistYearCovariatesInfoMap(param);

		param.setSex(false);
		list = mhiDao.selectMemberHistYearCovariatesInfoMap(param);

	}

	public void testSelectMemberHistYearCovariatesInfoClientMap() {
		CovariatesInfoParam param = new CovariatesInfoParam();

		ClientInfo info = new ClientInfo();
		info.setClientId("K08031");
		List<ClientInfo>  clist = new ArrayList<ClientInfo>();
		clist.add(info);
		param.setClientList(clist);
		List<MemberHistInfo> mlist = new ArrayList<MemberHistInfo>();
		param.setMemberList(mlist);

		MemberHistInfoDAO mhiDao =new MemberHistInfoDAO();
		List<YearCovariatesInfo> list = mhiDao.selectMemberHistYearCovariatesInfoClientMap(param);

		info = new ClientInfo();
		info.setClientId("K05994");
		clist.add(info);
		list = mhiDao.selectMemberHistYearCovariatesInfoClientMap(param);


	}

}
