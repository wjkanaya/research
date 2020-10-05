package sim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import deus.enums.RetirementType;
import deus.enums.StopType;
import deus_proto.Member;
import mybatis.dao.MemberHistInfoDAO;

public class MemberKanri {

	static Logger logger = LogManager.getLogger(MemberKanri.class);

	public List<Set<Member>> memberList = new ArrayList<Set<Member>>();


	public int getAllCnt() {
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

		return allcnt;
	}


	public List<Member> getList() {
		List<Member> list = new ArrayList<Member>();
		for (Set<Member> set : memberList) {
			list.addAll(set);
		}
		return list;
	}


	public void inc(Random random,int jiki,EigyouKanri eigyouKanri,SankakuRidatuYoteiKanri yoteikanri,
			MakeHazad mh,MemberHistInfoDAO dao,SimCalendar simcal) {


		// 退職チェック
		for (Set<Member> set :memberList) {
			for (Member m :set) {
				if (m.retT == jiki) {
					m.retire = 1;
					logger.debug(m.name + "君がやめました。 ");
					dao.updateMemberHistInfo(m.memberId, RetirementType.JIKO, simcal.getJikiDate(jiki));

				}
			}
		}

		// 全体数計算
		int allcnt = getAllCnt();

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
							logger.debug(mem.name + "君がやめます。 ");
							// mem.retire = 1;
							Project pro = eigyouKanri.getMemberProject(mem);

							int ituowaru =  MakePoasonRandom.getPoisson(random,
									(double)MakePoasonRandom.senkeiNormalToInto(random.nextGaussian(), 1, 3));

							if (pro != null) {
								logger.debug(mem.name + "プロジェクト終了!! ：" + pro.name + ":" + ituowaru);

								AkiMember akiMember = new AkiMember();
								akiMember.member = mem;
								akiMember.itukaraDate =  simcal.getJikiDate(jiki + ituowaru);


								yoteikanri.lnyoteiHimozukiNow(akiMember,pro,ituowaru,StopType.KOJIN, true);
							}

							mem.retT = jiki + ituowaru; // 退社時期



						}
					}
				}
			}
			sennpaicnt += doukicont;

		}



	}


}
