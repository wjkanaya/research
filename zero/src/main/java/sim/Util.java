package sim;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import deus_proto.Member;
import mybaits.vo.MemberHistInfo;

public class Util {

	public static void gensyoMember(Random random, SankakuRidatuYoteiKanri yoteikanri,Transaction tran) {

		Set<Member> delSet = new HashSet<Member>();

		List<Member> targetList = new LinkedList<Member>();
		targetList.addAll(tran.pro.memberSet);

		selectGensyoMember(random, delSet,tran.nannin, targetList);

		for (Member mem : delSet) {
			yoteikanri.lnyoteiHimozukiNow(mem,tran.pro,tran.nannkagetugo, false);
		}
	}

	public static void selectGensyoMember(Random random, Set<Member> resultSet, int delnin, List<Member> targetList) {

		if (delnin == 0) {
			return;
		}

		List<Double> gykuList = new ArrayList();

		double sum = 0;
		for (Member mem :targetList) {
			double gyakusu = 1/mem.nouryokukoujyoudo;
			gykuList.add(gyakusu);
			sum += gyakusu;
		}

		List<Double> hirituList = new ArrayList<Double>();

		double hiritu = 0;
		for (int i =0; i < gykuList.size() -1 ; i++) {
			hiritu += gykuList.get(i) / sum;
			hirituList.add(hiritu);
		}

		double ransu = random.nextDouble();

		int targetIdx = -1;
		for (int i = 0; i < hirituList.size();i++) {
			if ( ransu <  hirituList.get(i) ) {
				targetIdx = i;
				break;
			}
		}
		if (targetIdx < 0) {
			targetIdx =  hirituList.size();
		}

		resultSet.add(targetList.remove(targetIdx));

		selectGensyoMember(random, resultSet,delnin -1, targetList);
	}


	public static String getGeneProName(Random random,String kyakuName, int jiki) {

		return kyakuName +"_" + jiki + "_" + random.nextInt(10);
	}




	public static void makeMemberHIstInfo(int kikan,List<Member> memberList) {

		Date day1 = null;

		//kikan
		Calendar calendar = Calendar.getInstance();

		// 今日の日付
		Date nowDate = new Date();

		calendar.setTime(nowDate);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int date = 1;

		calendar.set(year, month, date, 0, 0, 0); // 今月の月初
		calendar.add(Calendar.MONTH, -kikan); // 計算期間分マイナス
		day1 = calendar.getTime();

		//	System.out.println("最初の日：" + day1);

		String resource = "mybatis-config.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		int size = memberList.size();

		List<MemberHistInfo> list = new ArrayList<MemberHistInfo>();
		for (Member mem : memberList) {
			//	    	      //	社員ID	member_id	text
			//	    	      private String memberId;
			//	    	      //	開始日付	start_date	date
			//	    	      private Date startDate;
			//	    	      //	名前	name	text
			//	    	      private String name;
			//	    	      //	入社日付	enter_date	date
			//	    	      private Date enterDate;
			//	    	      //	入社時年齢	enter_old	smallint
			//	    	      private Integer enterOld;
			//	    	      //	在籍状態	status	smallint 	 0:在籍、1:退社
			//	    	      private Integer status;
			//	    	      //	退職日日付	retirement_date	date
			//	    	      private Date retirementDate;
			//	    	      //	退職種別	retirement_type	smallint	  0:自己都合、1:会社判断
			//	    	      private Integer retirementType;
			//	    	      //	性別	sex	smallint	  0:男、1:女
			//	    	      private Integer sex;
			//	    	      //	新卒、既卒	flesh_or_not	smallint	  0:新卒、1:既卒
			//	    	      private Integer fleshOrNot;
			//	    	      //	所属部署	department	smallint	  部署カテゴリコード
			//	    	      private Integer department;
			//	    	      //	役職	position	smallint	  役職カテゴリコード
			//	    	      private Integer position;

			MemberHistInfo inf = new MemberHistInfo();
			inf.setMemberId(mem.memberId);

			calendar.setTime(day1);
			calendar.add(Calendar.MONTH, mem.entT);
			inf.setStartDate(calendar.getTime());
			inf.setName(mem.name);
			inf.setEnterDate(calendar.getTime());
			inf.setEnterOld(20);
			inf.setStatus(0);
			inf.setSex(mem.sex);
			if (mem.retire == 1) {
				inf.setStatus(1);
				calendar.setTime(day1);
				calendar.add(Calendar.MONTH, mem.retT);
				inf.setRetirementDate(calendar.getTime());
				inf.setRetirementType(0);
			}
			list.add(inf);


		}

		SqlSession session = null;
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		session = sqlSessionFactory.openSession();

		session.delete("truncateMemberHistInfo");
		session.delete("cleanMemberHistInfoTblId");

		List<MemberHistInfo> result = null;
		if(session != null) {

			try {
				int id = session.insert("insertManyMemberHistInfo", list);
				System.out.println(id);
				session.commit();
				result = session.selectList("selectAllMemberHistInfo");
				System.out.println(result.size());

			} finally {
				session.close();
			}
		}
	}


	public static SqlSession getSqlSession() {
		String resource = "mybatis-config.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

        SqlSession session = null;
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
		return session;
	}

}
