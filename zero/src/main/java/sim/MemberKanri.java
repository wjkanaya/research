package sim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import deus_proto.Member;

public class MemberKanri {

	public List<Set<Member>> memberList = new ArrayList<Set<Member>>();

	public void inc(Random random,int jiki,EigyouKanri eigyouKanri,SankakuRidatuYoteiKanri yoteikanri,MakeHazad mh) {


		// 退職チェック
		for (Set<Member> set :memberList) {
			for (Member m :set) {
				if (m.retT == jiki) {
					m.retire = 1;
					System.out.println(m.name + "君がやめました。 ");
				}
			}
		}

		// 全体数計算
		int max = this.memberList.size();
		int allcnt = 0;
		for (int k = 0; k < max;k++) {
			for (Iterator<Member> memberItr = this.memberList.get(k).iterator(); memberItr.hasNext();) {
				Member mem = memberItr.next();
				if (mem.retire == 0) {
					allcnt++;
				}
			}
		}

		int sennpaicnt = 0;
		for (int i = 0; i < this.memberList.size(); i++) {
			int doukicont = 0; // 同期の人数

			for (Iterator<Member> memberItr = this.memberList.get(i).iterator(); memberItr.hasNext();) {


				Member mem = memberItr.next();

				if (mem.retire == 0 && mem.retT < 0) {
					doukicont ++;
					int keika = jiki - mem.entT;
					if (keika > 0) {
						double yammeritu = mh.culcMemHzard(allcnt, sennpaicnt, mem, keika);

						if (random.nextDouble() < yammeritu) { // やめる確率
							System.out.println(mem.name + "君がやめます。 ");
							// mem.retire = 1;
							Project pro = eigyouKanri.getMemberProject(mem);
							int ituowaru =  MakePoasonRandom.getPoisson(random,
									(double)MakePoasonRandom.senkeiNormalToInto(random.nextGaussian(), 1, 3));
							System.out.println(mem.name + "プロジェクト終了!! ：" + pro.name + ":" + ituowaru);

							mem.retT = jiki + ituowaru; // 退社時期

							yoteikanri.lnyoteiHimozukiNow(mem,pro,ituowaru, false);

						}
					}
				}
			}
			sennpaicnt += doukicont;

		}



	}


}
