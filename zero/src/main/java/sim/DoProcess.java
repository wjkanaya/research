package sim;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import deus_proto.Member;

public class DoProcess {

	public static void main(String[] args) {

		// 社員リスト
		//List<Set<Member>> memberList = new ArrayList<Set<Member>>();
		MemberKanri memKanri = new MemberKanri();

		// 空き要員リスト
		AkiPool akiPool = new AkiPool();

		// 参画予定管理クラス
		SankakuRidatuYoteiKanri yoteikanri = new SankakuRidatuYoteiKanri(akiPool);


		//引き合プロジェクトプール
		ProPool proPool = new ProPool();

		// 営業管理
		EigyouKanri eigyouKanri = new EigyouKanri();

		// 取引あり顧客リスト
		Set<Kyaku> toriKyakuList = new HashSet<Kyaku>();

		// ハザード計算
		MakeHazad mh = new MakeHazad();


		// TODO 自動生成されたメソッド・スタブ
		// 名前カウント
		int namecnt = 0;

		// 新人の人数
		int freshnum = 5;
		Random random = new Random(1234);



		int jiki = 0;
		GeneSyuha syuha= new GeneSyuha();

		int nenUri = 0; // 年間売上

		while (jiki <= 240){
			System.out.println("時期：" + jiki);

			int jikiUri = 0;


			// 売上を確認しよう。
			for (Project pro : eigyouKanri.eigyoutyuProList) {
				if (pro.memberSet.size() > 0) {
					int proUriSum = 0;
					for (Member mem :pro.memberSet) {
						System.out.println(mem.name + "売上:" + pro.tankin);
						proUriSum += pro.tankin;
					}
					System.out.println(pro.name + "売上:" + proUriSum);
					jikiUri += proUriSum;
				}
			}
			System.out.println("時期:" + jiki + " 売上:" + jikiUri);

			nenUri += jikiUri;

			if (jiki % 12 == 0) {

				System.out.println(jiki / 12 + "年度売上:" + nenUri);
				nenUri = 0;
			}





			Set<Member> memberSet = new HashSet<Member>();
			GeneFreshMembers gene = new GeneFreshMembers();
			// -- 一番最初
			// とりあえず新人を雇おう

			namecnt = gene.getFreshMembers(memberSet,random, freshnum, namecnt, jiki);

			// 社員リストに格納しよう。
			memKanri.memberList.add(memberSet);
			// 空き社員リストに格納しよう。

			for (Iterator<Member> itr = memberSet.iterator(); itr.hasNext();) {
				Member m  = itr.next();
				akiPool.setAkiMember(m, 0);

			}

			// プロジェクトが終わるか確認しよう

			for (Iterator<Project> itr = eigyouKanri.eigyoutyuProList.iterator();itr.hasNext();){
				Project pro = itr.next();

				// 参画中
				if (pro.memberSet.size() > 0) {

					double proh =  mh.culcprohzard(pro);
					if (random.nextDouble() < proh) { // プロジェクト終了する確率
						int ituowaru =  MakePoasonRandom.getPoisson(random,
								(double)MakePoasonRandom.senkeiNormalToInto(random.nextGaussian(), 1, 12));
						System.out.println("プロジェクト終了!! ：" + pro.name + ":時期=" + (jiki + ituowaru));

						for (Iterator<Member> mitr = pro.memberSet.iterator(); mitr.hasNext();) {
							Member mem = mitr.next();

							if (!yoteikanri.containLNYotei(pro, mem)){
								yoteikanri.lnyoteiHimozukiNow(mem,pro,ituowaru, false);
							}
						}
					}
				}
			}

			// 個人的にプロジェクト辞めたい
			for (Iterator<Project> itr = eigyouKanri.eigyoutyuProList.iterator();itr.hasNext();){
				Project pro = itr.next();

				// 参画中
				if (pro.memberSet.size() > 0) {
					for (Iterator<Member> memitr = pro.memberSet.iterator();memitr.hasNext();){

						Member mem = memitr.next();

						if (!yoteikanri.containLNYotei(pro, mem)){
							double h = mh.culcTaiProhazard(mem, pro);
							if (random.nextDouble() < h) { // プロジェクト終了する確率
								int ituowaru =  MakePoasonRandom.getPoisson(random,
										(double)MakePoasonRandom.senkeiNormalToInto(random.nextGaussian(), 1, 3));
								System.out.println(mem.name + "プロジェクトやめたい!! ：" + pro.name + ":" + (jiki + ituowaru) + "に終了");
								yoteikanri.lnyoteiHimozukiNow(mem,pro,ituowaru, false);
							}
						}
					}
				}
			}


			// とりあえず引き合い情報を確認しよう

			GeneHikiai gen = new GeneHikiai();
			List<Project> proList = gen.makeHikiai(random, jiki);

			proPool.setProjectList(proList, jiki);


			// 直近のプロジェクトから一番売上が上がるプロジェクトを充てる
			// 12か月後一番トータルで儲かるのはどこ？
			List<Project> yuusenJunList = proPool.moukaruJun(jiki, 6);

			System.out.println("-儲かる順--");
			for (int i = 0; i < 5 ; i++) {
				Project pro = yuusenJunList.get(i);
				int n1 = syuha.nowNinzu(pro.jiki+ pro.nannkagetugo, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
				int n2 = syuha.nowNinzu(pro.jiki+ pro.nannkagetugo+6, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);


				System.out.println(jiki + ":" + pro.name + ":" + pro.getItukara(jiki) + "月後:" + n1 + "人(半年後"+ n2 +"):" +
						pro.tankin +":" + (6 - pro.getItukara(jiki)) *  pro.tankin);

			}

			// 一番儲かる順に空き要員を入れる
			for (Project pro : yuusenJunList) {

				// 開始時期人数
				int kaisijikiN = syuha.nowNinzu(pro.jiki+ jiki+ pro.nannkagetugo, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);

				List<Member> akiList = akiPool.getList(pro.getItukara(jiki));

				for (int i = 0 ; i < akiList.size(); i++) {
					if (kaisijikiN == 0) {
						break;
					}

					// 参画内定
					System.out.println(pro.jiki + pro.nannkagetugo +"に"+ akiList.get(i).name+ "を"+ pro.name + "に参画予定");
					yoteikanri.snyoteiHimozukiNow(akiList.get(i), pro, pro.nannkagetugo);

					// 営業中リスト
					if (!eigyouKanri.eigyoutyuProList.contains(pro)) {
						eigyouKanri.eigyoutyuProList.add(pro);

					}
					// 取引あり顧客リスト
					if (!toriKyakuList.contains(pro.kyaku)) {
						toriKyakuList.add(pro.kyaku);

					}

					// 空き要員から減らす。
					//akiList.remove(i);
					akiPool.delete(akiList.get(i));


					// 開始時期人数を減らす
					kaisijikiN--;
				}

			}


			// 一か月経過

			memKanri.inc(random, jiki, eigyouKanri, yoteikanri, mh);
			akiPool.inc();
			yoteikanri.inc();
			proPool.inc();

			jiki++;
		}



	}

}
