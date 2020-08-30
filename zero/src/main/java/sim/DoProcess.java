package sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import deus_proto.Member;

public class DoProcess {

	public static void main(String[] args) {

		List<Set<Member>> memberList = new ArrayList<Set<Member>>();

		List<Set<Member>> akiList = new LinkedList<Set<Member>>();





		// TODO 自動生成されたメソッド・スタブ
		// 名前カウント
		int namecnt = 0;
		// -- 一番最初
		// とりあえず新人を雇おう



		Set<Member> memberSet = new HashSet<Member>();
		GeneFreshMembers gene = new GeneFreshMembers();

		// 新人の人数
		int freshnum = 5;
		Random random = new Random(1234);

		int jiki = 0;
		namecnt = gene.getFreshMembers(memberSet,random, freshnum, namecnt, jiki);

		// 社員リストに格納しよう。
		memberList.add(memberSet);
		// 空き社員リストに格納しよう。
		akiList.add(memberSet);

		// とりあえず引き合い情報を確認しよう

		GeneHikiai gen = new GeneHikiai();
		List<Project> proList = gen.makeHikiai(random, jiki);


		// 時期ごとに分けたプロジェクト
		LinkedList<Set<Project>> sankouList = new LinkedList<Set<Project>>();

		// 始まりの時期を確認
        Collections.sort(
        		proList,
                new Comparator<Project>() {
                    public int compare(Project obj1, Project obj2) {
                        return obj1.nannkagetugo - obj2.nannkagetugo;
                    }
                }
            );
		GeneSyuha syuha= new GeneSyuha();



		int nowSankouIdx = 0;

		for (Project pro : proList) {


			if (pro.nannkagetugo > nowSankouIdx) {
				int sa = pro.nannkagetugo - nowSankouIdx;
				for (int i = 0; i < sa; i++) {
					if (sankouList.size() < (nowSankouIdx + 1)) {
						Set<Project> setPro = new HashSet<Project>();
						sankouList.add(setPro);
					}
					nowSankouIdx++;
				}
			}


			if (sankouList.size() < (nowSankouIdx + 1)) {
				Set<Project> setPro = new HashSet<Project>();
				sankouList.add(setPro);
			}
			sankouList.get(nowSankouIdx).add(pro);



		//	int n1 = syuha.nowNinzu(jiki, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
	    //		int n2 = syuha.nowNinzu(jiki+6, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);

		//	System.out.println(jiki + ":" + pro.name + ":" + pro.nannkagetugo + "月後:" + n1 + "人(半年後"+ n2 +"):" + pro.tankin );

		}



		// 直近のプロジェクトから一番売上が上がるプロジェクトを充てる
		// 12か月後一番トータルで儲かるのはどこ？


		List<Project> yuusenJunList = new ArrayList<Project>();

		for (Set<Project> proSet :sankouList){
			System.out.println("---");
			for (Iterator<Project> itr = proSet.iterator();itr.hasNext();) {
				Project pro = itr.next();
				int n1 = syuha.nowNinzu(jiki, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
				int n2 = syuha.nowNinzu(jiki+6, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);

				System.out.println(jiki + ":" + pro.name + ":" + pro.nannkagetugo + "月後:" + n1 + "人(半年後"+ n2 +"):" +
				pro.tankin +":" + (12 - pro.nannkagetugo) *  pro.tankin);
				yuusenJunList.add(pro);

			}
			System.out.println("---!");

		}

		// 儲かる順でソート
        Collections.sort(
        		yuusenJunList,
                new Comparator<Project>() {
                    public int compare(Project obj1, Project obj2) {
                        return (12 - obj2.nannkagetugo)  *  obj2.tankin -
                        		(12 - obj1.nannkagetugo)  *  obj1.tankin;
                    }
                }
            );

		System.out.println("-儲かる順--");
        for (Project pro : yuusenJunList) {
			int n1 = syuha.nowNinzu(jiki, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
			int n2 = syuha.nowNinzu(jiki+6, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);


			System.out.println(jiki + ":" + pro.name + ":" + pro.nannkagetugo + "月後:" + n1 + "人(半年後"+ n2 +"):" +
			pro.tankin +":" + (12 - pro.nannkagetugo) *  pro.tankin);

        }

       // 一か月経過

      // 
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

      MakeHazad mh = new MakeHazad();

  	  int sennpaicnt = 0;
        for (int i = 0; i < memberList.size(); i++) {
  		  int doukicont = 0; // 同期の人数

		  for (Iterator<Member> memberItr = memberList.get(i).iterator(); memberItr.hasNext();) {


			  Member mem = memberItr.next();

			  if (mem.retire == 0) {
				  doukicont ++;
				  int keika = jiki - mem.entT;
				  if (keika > 0) {
					  double yammeritu = mh.culcMemHzard(allcnt, sennpaicnt, mem, keika);

					  if (random.nextDouble() < yammeritu) { // やめる確率
						  System.out.println(mem.name + "君がやめました ");
						  mem.retire = 1;
						  mem.retT = jiki; // 退社時期

					  }

				  }
			  }
		  }
		  sennpaicnt += doukicont;

        }



	}

}
