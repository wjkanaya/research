package sim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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


	public void inc(SimRandom random,int jiki,EigyouKanri eigyouKanri,SankakuRidatuYoteiKanri yoteikanri,
			MakeHazard mh,MemberHistInfoDAO dao,SimCalendar simcal, AkiPool akiPool) {

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

	}

	public void retireCheck(SimRandom random,int jiki,EigyouKanri eigyouKanri,SankakuRidatuYoteiKanri yoteikanri,
			MakeHazard mh,MemberHistInfoDAO dao,SimCalendar simcal, AkiPool akiPool) {

		// 全体数計算
		int allcnt = getAllCnt();

		int sennpaicnt = 0;
		for (int i = 0; i < this.memberList.size(); i++) {
			int doukicont = 0; // 同期の人数

			for (Iterator<Member> memberItr = this.memberList.get(i).iterator(); memberItr.hasNext();) {


				Member mem = memberItr.next();

				if (mem.retire == 0 && mem.retT < 0) {
					doukicont ++;
					int keika = jiki - mem.entT + 1;

					double yammeritu = mh.culcMemHzard(allcnt, sennpaicnt, mem, keika);

					if (random.nextDouble() < yammeritu) { // やめる確率
						// mem.retire = 1;
						Project pro = eigyouKanri.getMemberProject(mem);

						int ituowaru =  MakePoasonRandom.getPoisson(random,
								(double)MakePoasonRandom.senkeiNormalToInt(random.nextGaussian(), 1, 3));

						if (ituowaru == 0) {
							ituowaru = 1;
						}

						AkiMember akiMember = new AkiMember();
						akiMember.member = mem;
						akiMember.itukara = jiki + ituowaru;
						yoteikanri.deleteMember(jiki,akiMember); // 予定管理から削除
						akiPool.delete(akiMember);// 空プールから削除

						logger.debug(jiki +":" +mem.name + "君がやめます。 " + (jiki + ituowaru));

						if (pro != null) {
							logger.debug(mem.name + "退職でプロジェクト終了!! ：" + pro.name + ":退職時期:" + jiki +"+" + ituowaru);

							yoteikanri.lnyoteiHimozukiNow(akiMember,pro,ituowaru,StopType.KOJIN_RT, true);

						}

						mem.retT = jiki + ituowaru; // 退社時期
					}
				}
			}
			sennpaicnt += doukicont;

		}
	}
}
