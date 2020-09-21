package sim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import deus_proto.Member;

public class EigyouKanri {

	// 営業中プロジェクトリスト
	public Set<Project> eigyoutyuProList = new HashSet<Project>();

	public Project getMemberProject(Member mem) {

		Project result = null;

		for (Project pro : eigyoutyuProList) {
			if (pro.memberSet.contains(mem)) {

				result = pro;
				break;
			}
		}

		return result;

	}

	public void inc(){
		for (Project pro : eigyoutyuProList) {
			pro.doOneMonKeika();
		}

	}

	public List<Transaction> getTransaction(int nowJiki) {

		List<Transaction> list = new ArrayList<Transaction>();

		GeneSyuha syuha = new GeneSyuha();

		for (Project pro : eigyoutyuProList) {

			if (pro.endJiki < 0 && pro.nexEigyouJiki == nowJiki) {

				int minnum = pro.maxnannin;

				for (int i = 0; i < GeneHikiai.NINZU_RANGE; i++) {

					int n = syuha.nowNinzu(nowJiki + GeneHikiai.NINZU_RANGE + i, pro.syuuki, pro.maxnannin, pro.syukiisou, pro.kizamihiritu , pro.range);

					if (n < minnum) {
						minnum = n;
					}
				}

				if (minnum > pro.memberSet.size()) {
					// 増える
					Transaction tran = new Transaction();
					tran.pro = pro;
					tran.jiki = nowJiki;
					tran.nannkagetugo = GeneHikiai.NINZU_RANGE;
					tran.nannin = minnum - pro.memberSet.size();
					list.add(tran);

				} else if (minnum < pro.memberSet.size()) {
					// 減る
					Transaction tran = new Transaction();
					tran.genFlg = true; // 減少フラグ
					tran.pro = pro;
					tran.jiki = nowJiki;
					tran.nannkagetugo = GeneHikiai.NINZU_RANGE;
					tran.nannin = pro.memberSet.size() -minnum;
					list.add(tran);

				}

				pro.nexEigyouJiki  += GeneHikiai.NINZU_RANGE;
			}
		}

		return list;
	}



}
