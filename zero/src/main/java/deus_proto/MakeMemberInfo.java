package deus_proto;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import mybaits.vo.MemberHistInfo;
import sim.GeneFreshMembers;

public class MakeMemberInfo {

	public static void main(String[] args) {

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

		 Random random = new Random();

	     //get two double numbers
	      double x = 5;
	      double y = 0.5;

	      // ワイブル分布
	      ///・・
	      //S(t) = e−(λt)p = 5年と6月　5*12+6 で 0.5 0.25
	      double lamda = 150;
	      double p = 0.9;

	      double t = 0;

	      int yamecount = 0;

	      // 新人数
	      int freshnum = 20;

	      // 期間
	      int kikan = 360;

	      System.out.println("S)=" + Math.exp(-Math.pow(t/lamda,p)));
	      //System.out.println("haa:" + lamda*p*Math.pow(lamda * t,p-1));
	     // System.out.println("h():" + (p/(Math.pow(lamda, p))) * Math.pow(t,p-1));
	      System.out.println("h(0):" + (p/(Math.pow(lamda, p))) * Math.pow(1,p-1));
	      System.out.println("-----------------------");
// mはpでnyuはlambdaだ
	      // 名前カウント
	      int namecnt = 0;

	      double b= lamda;
	      double a = p;

	      //System.out.println("haa:" + a*t^(a-1)*b^(-a);
	      //System.out.println("haa:" + a*Math.pow(t, a-1)*Math.pow(b,-a));


	      LinkedList<Set<Member>> memberList = new  LinkedList<Set<Member>>();

	      for (int j=0;j<kikan;j++) {
	    	  int max = memberList.size();
	    	  int allcnt = 0;
	    	  for (int k = 0; k < max;k++) {
	    		  for (Iterator<Member> memberItr = memberList.get(k).iterator(); memberItr.hasNext();) {
	    			  Member mem = memberItr.next();
	    			  if (mem.retire == 0) {
	    				  allcnt++;
	    			  }
	    		  }
	    	  }

              Set<Member> memberSet = new HashSet<Member>();
              GeneFreshMembers gene = new GeneFreshMembers();
	    	  namecnt = gene.getFreshMembers(memberSet,random, freshnum, namecnt, j);
	    	  memberList.add(memberSet);
	    	  max = memberList.size();


	    	  //System.out.println("h(0):" + (p/(Math.pow(lamda, p))) * Math.pow(1,p-1));




	    	  int sennpaicnt = 0;
	    	  for (int k = 0; k < max;k++) {

	    		  int doukicont = 0; // 同期の人数

	    		  for (Iterator<Member> memberItr = memberList.get(k).iterator(); memberItr.hasNext();) {


	    			  Member mem = memberItr.next();

	    			  if (mem.retire == 0) {
	    				  doukicont ++;
	    				  int keika = j - mem.entT;
	    				  if (keika > 0) {

	    					  double yammeritu = makeh(lamda, p, allcnt, sennpaicnt, mem, keika);

	    					  if (random.nextDouble() < yammeritu) { // やめる確率
	    						  yamecount++;
	    						  System.out.println(mem.name + "君がやめました " +yamecount);
	    						  mem.retire = 1;
	    						  mem.retT = j; // 退社時期

	    					  }

	    				  }
	    			  }
	    		  }

	    		  sennpaicnt += doukicont;


	    	  }

	      }
	      int max = memberList.size();
	      int nenme = 1;
	      for (int k = max -1 ; k >=0;k--) {
	    	  int doukicount = 0;
	    	  for (Iterator<Member> memberItr = memberList.get(k).iterator(); memberItr.hasNext();) {
	    		  Member mem = memberItr.next();
    			  if (mem.retire == 0) {
    				  doukicount++;
    			  }
	    	  }

	    	  System.out.println(nenme +"年目:" +doukicount);
	    	  nenme++;
	      }
	      nenme = 1;
	      for (int k = max -1 ; k >=0;k--) {
	    	  int doukicount = 0;
	    	  for (Iterator<Member> memberItr = memberList.get(k).iterator(); memberItr.hasNext();) {
	    		  Member mem = memberItr.next();
    			  if (mem.retire == 0) {

    				  doukicount++;
    			  }
	    	  }

	    	  StringBuffer  bar  = new StringBuffer();


	    	  for (int l = 0;l < doukicount;l++ ){
	    		  bar.append("※");
	    	  }
	    	  System.out.println(nenme +"年目:" +bar);
	    	  nenme++;

	      }

			// TODO 自動生成されたメソッド・スタブ

		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
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

          System.out.println("最初の日：" + day1);



// データ作成

//	      //	社員ID	member_id	text
//	      private String memberId;
//	      //	開始日付	start_date	date
//	      private Date startDate;
//	      //	名前	name	text
//	      private String name;
//	      //	入社日付	enter_date	date
//	      private Date enterDate;
//	      //	入社時年齢	enter_old	smallint
//	      private Integer enterOld;
//	      //	在籍状態	status	smallint 	 0:在籍、1:退社
//	      private Integer status;
//	      //	退職日日付	retirement_date	date
//	      private Date retirementDate;
//	      //	退職種別	retirement_type	smallint	  0:自己都合、1:会社判断
//	      private Integer retirementType;
//	      //	性別	sex	smallint	  0:男、1:女
//	      private Integer sex;
//	      //	新卒、既卒	flesh_or_not	smallint	  0:新卒、1:既卒
//	      private Integer fleshOrNot;
//	      //	所属部署	department	smallint	  部署カテゴリコード
//	      private Integer department;
//	      //	役職	position	smallint	  役職カテゴリコード
//	      private Integer position;



          List<MemberHistInfo> list = new ArrayList<MemberHistInfo>();
	      for (int k = max -1 ; k >=0;k--) {
	    	  for (Iterator<Member> memberItr = memberList.get(k).iterator(); memberItr.hasNext();) {
	    		  Member mem = memberItr.next();

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

	      }
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

	private static double makeh(double lamda, double p, int allcnt, int sennpaicnt, Member mem, int keika) {
		double h = (p/(Math.pow(lamda, p))) * Math.pow(keika,p-1); //ハザード値

		  //System.out.println ("うざい：" +0.1  * (sennpaicnt/ allcnt));

		  double yammeritu =h * Math.exp (mem.yameritu* 0.0000001
				  + 0.000001  * (
						  2* (((double)sennpaicnt)/ allcnt-0.5)
						  )
				  ) ;
		return yammeritu;
	}



}
