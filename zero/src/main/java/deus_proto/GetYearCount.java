package deus_proto;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.YearCount;
import sim.Util;

public class GetYearCount {

	public static void main(String[] args) {

		Util.startTransaction();
		try {
			GetYearCount count = new GetYearCount();
			count.execute();

		} finally {
			Util.endTransaction();;
		}

	}

	private void execute() {
		// TODO 自動生成されたメソッド・スタブ
		SqlSession session = Util.getSqlSession();
		List<YearCount> result = null;
		result = session.selectList("selectAllMemberYearCount");

        for (YearCount count : result) {
        	System.out.println(count.getYear() + "," + count.getCount());
        }
	}

}
