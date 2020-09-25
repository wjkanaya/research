package deus_proto;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.YearCount;
import sim.Util;

public class GetYearCount {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		SqlSession session = Util.getSqlSession();
		List<YearCount> result = null;

        if(session != null) {

        	try {
                result = session.selectList("selectAllMemberYearCount");

        	} finally {
        		session.close();
        	}
        }

        for (YearCount count : result) {
        	System.out.println(count.getYear() + "," + count.getCount());
        }


	}

}
