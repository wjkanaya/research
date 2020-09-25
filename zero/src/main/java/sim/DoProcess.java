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

		int jinkenhi = 52;

		Random random = new Random(1234);



		int jiki = 0;
		GeneSyuha syuha= new GeneSyuha();

		int nenUri = 0; // 年間売上

		while (jiki <= 360){
			System.out.println("時期：" + jiki + "人数:" + memKanri.getAllCnt());

			int jikiUri = 0;

			// 売上を確認しよう。
			for (Project pro : eigyouKanri.eigyoutyuProList) {
				if (pro.memberSet.size() > 0) {
					int proUriSum = 0;
					for (Member mem :pro.memberSet) {

						int uri = pro.tankin;
						if (jiki -mem.entT <= 12) {
							uri = pro.tankin / 2;
						}
						System.out.println(mem.name + "売上:" + uri);
						proUriSum += uri;
					}
					System.out.println(pro.name + "売上:" + proUriSum);
					jikiUri += proUriSum;
				}
			}
			System.out.println("時期:" + jiki + " 売上:" + jikiUri);

			nenUri += jikiUri;

			if (jiki % 12 == 0) {

				System.out.println(jiki / 12 + "年度売上:" + nenUri);

				int rieki  = nenUri - (int)Math.ceil(memKanri.getAllCnt() * 1.1) * jinkenhi * 12;
				System.out.println(jiki / 12 + "利益:" + rieki);


				freshnum = (int)Math.floor( rieki * 0.97 / (jinkenhi  * 12));

				if (freshnum == 0) {
					freshnum = 1;
				}

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
						pro.endJiki = jiki + ituowaru;
						for (Iterator<Member> mitr = pro.memberSet.iterator(); mitr.hasNext();) {
							Member mem = mitr.next();

							if (!yoteikanri.containLNYotei(pro, mem)){
								yoteikanri.lnyoteiHimozukiNow(mem,pro,ituowaru, false);
							}
						}
					}
				}
			}



			// プロジェクトの人数増減
			List<Transaction> list = eigyouKanri.getTransaction(jiki);
			List<Transaction> addList = new ArrayList<Transaction>();
			for (Transaction tran :list) {
				if (tran.genFlg) {
					Util.gensyoMember(random, yoteikanri, tran);
				} else {
					addList.add(tran);

				}
			}
			proPool.setProjectList(addList, jiki);


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
			List<Transaction> proList = gen.makeHikiai(random, jiki);

			proPool.setProjectList(proList, jiki);


			// 直近のプロジェクトから一番売上が上がるプロジェクトを充てる
			// 12か月後一番トータルで儲かるのはどこ？
			List<Transaction> yuusenJunList = proPool.moukaruJun(jiki, 6);

			System.out.println("-儲かる順--");
			for (int i = 0; i < 5 ; i++) {
				Transaction tran = yuusenJunList.get(i);
				Project pro = tran.pro;
				int n1 = tran.nannin;

				System.out.println(jiki + ":" + pro.name + ":" + tran.getItukara(jiki) + "月後:" + n1 + "人:" +
						pro.tankin +":" + (6 - tran.getItukara(jiki)) *  pro.tankin);
			}

			// 一番儲かる順に空き要員を入れる
			for (Transaction tran : yuusenJunList) {

				Project pro = tran.pro;

				// 開始時期人数
				List<Member> akiList = akiPool.getList(tran.getItukara(jiki));

				for (int i = 0 ; i < akiList.size(); i++) {
					if (tran.nannin - tran.juutounin == 0) {
						break;
					}

					// 参画内定
					System.out.println(tran.jiki + tran.nannkagetugo +"に"+ akiList.get(i).name+ "を"+ pro.name + "に参画予定");
					yoteikanri.snyoteiHimozukiNow(akiList.get(i), pro, tran.nannkagetugo);
					tran.juutounin += 1;

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


				}

			}

			// 一か月経過
			memKanri.inc(random, jiki, eigyouKanri, yoteikanri, mh);
			akiPool.inc();
			yoteikanri.inc();
			proPool.inc();

			jiki++;
		}


		Util.makeMemberHIstInfo(jiki,memKanri.getList());
	}

}
