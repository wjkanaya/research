package deus_proto;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.ibatis.session.SqlSession;

import mybaits.vo.MemberHistInfo;
import sim.Util;

public class GeneSurvData {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		Util.startTransaction();
		try {
			GeneSurvData gene = new GeneSurvData();
			gene.excute();

		} finally {
			Util.endTransaction();;
		}

	}



	private void excute() {
		SqlSession session = Util.getSqlSession();
        List<MemberHistInfo> result = null;

        // 社員数
        int memberNum = 0;


        if(session != null) {

        	result = session.selectList("selectAllMemberHistInfo");
        	System.out.println(result.size());
        	memberNum = result.size();
        }

		  // 今日の日付
		 Date nowDate = new Date();


        TreeMap<Integer,Integer[]> tm = new TreeMap<Integer,Integer[]>();

        for (MemberHistInfo info: result)  {
        	if (info.getStatus() == 0){  // 在籍

        		Integer keikaTuki = dateMonthSa(info.getStartDate(),nowDate);

        		if (tm.get(keikaTuki) == null) {
        			Integer[] count = new Integer[2];
        			count[0] = 0;
        			count[1] = 1;
        			tm.put(keikaTuki, count);

        		} else {
        			Integer[] count = tm.get(keikaTuki);
        			count[1] += 1;
        		}

        	} else { // 退社
        		//今日の日付を整数値に変換
        		Integer keikaTuki = dateMonthSa(info.getStartDate(),info.getRetirementDate());

        		if (tm.get(keikaTuki) == null) {
        			Integer[] count = new Integer[2];
        			count[0] = 1;
        			count[1] = 0;
        			tm.put(keikaTuki, count);

        		} else {
        			Integer[] count = tm.get(keikaTuki);
        			count[0] += 1;
        		}
        	}

        }

        int wa = memberNum;
        double S = 1; // 生存割合
        for (Iterator<Entry<Integer,Integer[]>> memberItr = tm.entrySet().iterator(); memberItr.hasNext();) {
        	Entry<Integer,Integer[]> ent = memberItr.next();
        	double nowS = 1 -((double)ent.getValue()[0])/wa;
        	S *= nowS;
        	System.out.println(ent.getKey() + "," + ent.getValue()[0] + ","
        	+ ent.getValue()[1] + "," +String.format("%.3f", S) );
        	wa -= ent.getValue()[1];
        }
	}



	private static int dateMonthSa(Date from, Date to) {

		  Calendar calFrom = Calendar.getInstance();

		    calFrom.setTime(from);

		    calFrom.set(Calendar.DATE, 1);



		    Calendar calTo = Calendar.getInstance();

		    calTo.setTime(to);

		    calTo.set(Calendar.DATE, 1);



		    int count = 0;

		    while (calFrom.before(calTo)) {

		        calFrom.add(Calendar.MONTH, 1);

		        count++;

		    }

		    return count;
	}

}
