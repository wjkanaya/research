	package sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ProPool {

	List<Set<Transaction>> proList = new LinkedList<Set<Transaction>>();

	public void deleteTransaction(Project pro) {

		for (Set<Transaction> set :proList) {
			List<Transaction> delList = new ArrayList<Transaction>();
			for (Transaction tran :set) {
				if (tran.pro.equals(pro)) {
					delList.add(tran);
				}
			}
			for (Transaction target :delList) {
				set.remove(target);
			}
		}
	}


	public void setProjectList(List<Transaction> proList, int nowJiki) {

		for (Transaction pro :proList) {
			setProject(pro, nowJiki);
		}

	}

	public void setProject(Transaction pro, int nowJiki) {

		while (proList.size() < pro.getItukara(nowJiki) + 1) {
			proList.add(new TreeSet<Transaction>());
		}

		proList.get(pro.getItukara(nowJiki)).add(pro);

	}

	public void inc() {

		if (proList.size() > 0) {
			proList.remove(0);
		}

	}


	public List<Transaction> moukaruJun(int nowJiki, int span) {

		List<Transaction> list = new ArrayList<Transaction>();

		for (Set<Transaction> set :proList) {
			list.addAll(set);
		}

		final int nowJikiFinal = nowJiki;
		final int spanFinal = span;

		// 儲かる順でソート
		Collections.sort(
				list,
				new Comparator<Transaction>() {
					public int compare(Transaction obj1, Transaction obj2) {
						return (spanFinal - obj2.getItukara(nowJikiFinal))  *  obj2.pro.tankin -
								(spanFinal - obj1.getItukara(nowJikiFinal))  *  obj1.pro.tankin;
					}
				}
				);

		return list;
	}

}
