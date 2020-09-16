package sim;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import deus_proto.Member;

public class AkiPool {

	public List<Set<Member>> akiList = new LinkedList<Set<Member>>();


	public void setAkiMember(Member mem, int itukara) {

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



}
