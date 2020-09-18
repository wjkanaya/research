	package sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ProPool {

	List<Set<Project>> proList = new LinkedList<Set<Project>>();


	public void setProjectList(List<Project> proList, int nowJiki) {

		for (Project pro :proList) {
			setProject(pro, nowJiki);
		}

	}

	public void setProject(Project pro, int nowJiki) {

		while (proList.size() < pro.getItukara(nowJiki) + 1) {
			proList.add(new HashSet<Project>());
		}

		proList.get(pro.getItukara(nowJiki)).add(pro);

	}

	public void inc() {
		proList.remove(0);
	}


	public List<Project> moukaruJun(int nowJiki, int span) {

		List<Project> list = new ArrayList<Project>();

		for (Set<Project> set :proList) {
			list.addAll(set);
		}

		final int nowJikiFinal = nowJiki;
		final int spanFinal = span;

		// 儲かる順でソート
		Collections.sort(
				list,
				new Comparator<Project>() {
					public int compare(Project obj1, Project obj2) {
						return (spanFinal - obj2.getItukara(nowJikiFinal))  *  obj2.tankin -
								(spanFinal - obj1.getItukara(nowJikiFinal))  *  obj1.tankin;
					}
				}
				);

		return list;
	}

}
