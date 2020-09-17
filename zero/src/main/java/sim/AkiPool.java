package sim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import deus_proto.Member;

public class AkiPool {

	public List<Set<Member>> akiList = new LinkedList<Set<Member>>();


	public void setAkiMember(Member mem, int itukara) {

		delete(mem); // 既存の設定を削除
		;

		while (akiList.size() < itukara + 1) {
			akiList.add(new HashSet<Member>());
		}
		akiList.get(itukara).add(mem);
	}

	public void inc() {

		if (akiList.size() > 1) {
			Set<Member> oneSet = akiList.get(1);
			akiList.get(0).addAll(oneSet);
			akiList.remove(1);
		}
	}


	public List<Member> getList(int itukara) {
		List list = new ArrayList();

		int max =0;

		if (akiList.size() > itukara + 1) {
			max = itukara +1;
		} else {
			max  = akiList.size();
		}

		for (int i = 0; i < max; i++) {
			list.addAll(akiList.get(i));
		}

		return list;

	}


	public void delete(Member m) {

		for (Set<Member> set :akiList) {
			if (set.contains(m)) {
				set.remove(m);
			}

		}

	}



}
