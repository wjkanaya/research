package deus_proto;

import java.util.List;

import mybaits.vo.YearMonthsCensorCount;
import mybatis.dao.ProjectEnrolledHistInfoDAO;
import sim.Util;

// プロジェクト継続率曲線グラフ情報
public class GetYearMonthsCensorCount {

	public static void main(String[] args) {

		Util.startTransaction();
		try {
			GetYearMonthsCensorCount count = new GetYearMonthsCensorCount();
			count.execute();

		} finally {
			Util.endTransaction();;
		}

	}

	private void execute() {
		ProjectEnrolledHistInfoDAO dao = new ProjectEnrolledHistInfoDAO();
		int allCount = dao.selectAllProjectEnrolledHistInfoCount();

		List<YearMonthsCensorCount> list = dao.selectAllProjectEnrolledHistYearMonthsCountInfo();

		int nowLiveCount = allCount;
		double savePoint = 1.0;
		for (YearMonthsCensorCount info :list) {
			// 生存時間
			double q = ((double)info.getCount())/ nowLiveCount;
			System.out.println("" + info.getYearMonths() + ":" + nowLiveCount +":" + q +":" + savePoint );
			savePoint = savePoint * (1 - q );
			nowLiveCount -= (info.getCount() + info.getCensored());
		}
	}

}
