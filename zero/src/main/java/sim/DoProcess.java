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

		// 社員リスト
		List<Set<Member>> memberList = new ArrayList<Set<Member>>();

		// 空き要員リスト
		List<Member> akiList = new LinkedList<Member>();

		// 参画予定管理クラス
		SankakuRidatuYoteiKanri yoteikanri = new SankakuRidatuYoteiKanri();

		// 営業中プロジェクトリスト
		Set<Project> eigyoutyuProList = new HashSet<Project>();

		// 取引あり顧客リスト
		Set<Kyaku> toriKyakuList = new HashSet<Kyaku>();


		// TODO 自動生成されたメソッド・スタブ
		// 名前カウント
		int namecnt = 0;

		// 新人の人数
		int freshnum = 5;
		Random random = new Random(1234);

		// -- 一番最初
		// とりあえず新人を雇おう

		int jiki = 0;
		GeneSyuha syuha= new GeneSyuha();

		while (jiki <= 0){

			Set<Member> memberSet = new HashSet<Member>();
			GeneFreshMembers gene = new GeneFreshMembers();

			namecnt = gene.getFreshMembers(memberSet,random, freshnum, namecnt, jiki);

			// 社員リストに格納しよう。
			memberList.add(memberSet);
			// 空き社員リストに格納しよう。

			for (Iterator<Member> itr = memberSet.iterator(); itr.hasNext();) {
				Member m  = itr.next();
				akiList.add(m);

			}

			MakeHazad mh = new MakeHazad();


			// プロジェクトが終わるか確認しよう

			for (Iterator<Project> itr = eigyoutyuProList.iterator();itr.hasNext();){
				Project pro = itr.next();

				// 参画中
				if (pro.memberSet.size() > 0) {

					double proh =  mh.culcprohzard(pro);
					if (random.nextDouble() < proh) { // プロジェクト終了する確率
						int ituowaru =  MakePoasonRandom.getPoisson(random,
									(double)MakePoasonRandom.senkeiNormalToInto(random.nextGaussian(), 1, 12));
						System.out.println("プロジェクト終了!! ：" + pro.name + ":" + ituowaru);

						for (Iterator<Member> mitr = pro.memberSet.iterator(); mitr.hasNext();) {
							Member mem = mitr.next();


							yoteikanri.lnyoteiHimozukiNow(mem,pro,ituowaru);
						}
					}
				}
			}

			// プロジェクトの人数の増減を確認しよう
			// 増減は後で
			//			for (Iterator<Project> itr = eigyoutyuProList.iterator();itr.hasNext();){
//				Project pro = itr.next();
//
//				if (pro.memberSet.size() > 0) {
//					int n1 = syuha.nowNinzu(jiki + pro.nannkagetugo, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
//
//
//				}
//			}


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
							return (obj1.jiki + obj1.nannkagetugo) - (obj2.jiki + obj2.nannkagetugo);
						}
					}
					);



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
					int n1 = syuha.nowNinzu(jiki + pro.nannkagetugo, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
					int n2 = syuha.nowNinzu(jiki + pro.nannkagetugo+6, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);

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

			// 一番儲かる順に空き要員を入れる
			for (Project pro : yuusenJunList) {

				// 開始時期人数
				int kaisijikiN = syuha.nowNinzu(jiki, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);

				for (int i = akiList.size() -1 ; i >= 0; i--) {
					if (kaisijikiN == 0) {
						break;
					}

					// 参画内定
					System.out.println(pro.nannkagetugo+"か月後に"+ akiList.get(i).name+ "を"+ pro.name + "に参画予定");
					yoteikanri.snyoteiHimozukiNow(akiList.get(i), pro, pro.nannkagetugo);

					// 営業中リスト
					if (!eigyoutyuProList.contains(pro)) {
						eigyoutyuProList.add(pro);

					}
					// 取引あり顧客リスト
					if (!toriKyakuList.contains(pro.kyaku)) {
						toriKyakuList.add(pro.kyaku);

					}

					// 空き要員から減らす。
					akiList.remove(i);


					// 開始時期人数を減らす
					kaisijikiN--;
				}

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

}
