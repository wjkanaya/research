package deus_proto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import mybaits.vo.YearEstimateInfo;

public class GeneSurvDataEstimateTest extends TestCase {

	public void testGetData() {
		GeneSurvDataEstimate est = new GeneSurvDataEstimate();

		est.getData();

		List<YearEstimateInfo> list =  est.getMapList();

		Map<String, BigDecimal> map = list.get(0).getCovariatesMap();
		System.out.println(map);

	}

}
