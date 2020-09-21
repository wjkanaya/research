package sim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import deus_proto.Member;

public class Util {

	public static void gensyoMember(Random random, SankakuRidatuYoteiKanri yoteikanri,Transaction tran) {

		Set<Member> delSet = new HashSet<Member>();

		List<Member> targetList = new LinkedList<Member>();
		targetList.addAll(tran.pro.memberSet);

		selectGensyoMember(random, delSet,tran.nannin, targetList);

		for (Member mem : delSet) {
			yoteikanri.lnyoteiHimozukiNow(mem,tran.pro,tran.nannkagetugo, false);
		}
	}

	public static void selectGensyoMember(Random random, Set<Member> resultSet, int delnin, List<Member> targetList) {

		if (delnin == 0) {
			return;
		}

		List<Double> gykuList = new ArrayList();

		double sum = 0;
		for (Member mem :targetList) {
			double gyakusu = 1/mem.nouryokukoujyoudo;
			gykuList.add(gyakusu);
			sum += gyakusu;
		}

		List<Double> hirituList = new ArrayList<Double>();

		double hiritu = 0;
		for (int i =0; i < gykuList.size() -1 ; i++) {
			hiritu += gykuList.get(i) / sum;
			hirituList.add(hiritu);
		}

		double ransu = random.nextDouble();

		int targetIdx = -1;
		for (int i = 0; i < hirituList.size();i++) {
			if ( ransu <  hirituList.get(i) ) {
				targetIdx = i;
				break;
			}
		}
		if (targetIdx < 0) {
			targetIdx =  hirituList.size();
		}

		resultSet.add(targetList.remove(targetIdx));

		selectGensyoMember(random, resultSet,delnin -1, targetList);
	}



}
