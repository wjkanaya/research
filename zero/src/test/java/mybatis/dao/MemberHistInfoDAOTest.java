package mybatis.dao;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import mybaits.vo.ClientInfo;
import mybaits.vo.YearCovariatesInfo;
import mybaits.vo.YearCovariatesInfoParam;
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
		YearCovariatesInfoParam param = new YearCovariatesInfoParam();
		param.setSex(true);

		MemberHistInfoDAO mhiDao =new MemberHistInfoDAO();
		List<YearCovariatesInfo> list = mhiDao.selectMemberHistYearCovariatesInfoMap(param);

		param.setSex(false);
		list = mhiDao.selectMemberHistYearCovariatesInfoMap(param);

	}

	public void testSelectMemberHistYearCovariatesInfoClientMap() {
		YearCovariatesInfoParam param = new YearCovariatesInfoParam();

		ClientInfo info = new ClientInfo();
		info.setClientId("K08031");
		List<ClientInfo>  clist = new ArrayList<ClientInfo>();
		clist.add(info);
		param.setClientList(clist);


		MemberHistInfoDAO mhiDao =new MemberHistInfoDAO();
		List<YearCovariatesInfo> list = mhiDao.selectMemberHistYearCovariatesInfoClientMap(param);

		info = new ClientInfo();
		info.setClientId("K05994");
		clist.add(info);
		list = mhiDao.selectMemberHistYearCovariatesInfoClientMap(param);


	}

}
