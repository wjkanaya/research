package sim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import deus_proto.Member;

public class DoProcess {

	public static void main(String[] args) {

		// 社員リスト
		List<Set<Member>> memberList = new ArrayList<Set<Member>>();

		// 空き要員リスト
		AkiPool akiPool = new AkiPool();

		// 参画予定管理クラス
		SankakuRidatuYoteiKanri yoteikanri = new SankakuRidatuYoteiKanri(akiPool);

		// 営業中プロジェクトリスト
		Set<Project> eigyoutyuProList = new HashSet<Project>();

		//引き合プロジェクトプール
		ProPool proPool = new ProPool();

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
				akiPool.setAkiMember(m, 0);

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

			proPool.setProjectList(proList, jiki);


			// 直近のプロジェクトから一番売上が上がるプロジェクトを充てる
			// 12か月後一番トータルで儲かるのはどこ？
			List<Project> yuusenJunList = proPool.moukaruJun(jiki, 12);

			System.out.println("-儲かる順--");
			for (Project pro : yuusenJunList) {
				int n1 = syuha.nowNinzu(jiki+ pro.nannkagetugo, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
				int n2 = syuha.nowNinzu(jiki+ pro.nannkagetugo+6, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);


				System.out.println(jiki + ":" + pro.name + ":" + pro.getItukara(jiki) + "月後:" + n1 + "人(半年後"+ n2 +"):" +
						pro.tankin +":" + (12 - pro.getItukara(jiki)) *  pro.tankin);

			}

			// 一番儲かる順に空き要員を入れる
			for (Project pro : yuusenJunList) {

				// 開始時期人数
				int kaisijikiN = syuha.nowNinzu(jiki+ pro.nannkagetugo, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);

				List<Member> akiList = akiPool.getList(pro.getItukara(jiki));

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
