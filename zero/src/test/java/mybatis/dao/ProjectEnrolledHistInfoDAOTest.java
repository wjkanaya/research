package mybatis.dao;

import junit.framework.TestCase;
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

}
