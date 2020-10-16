package sim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AkiPool {

	static Logger logger = LogManager.getLogger(AkiPool.class);


	public List<Set<AkiMember>> akiList = new LinkedList<Set<AkiMember>>();


	public void setAkiMember(AkiMember mem, int itukara) {

		if (mem.member.retT > 0) {
			logger.debug(mem.member.name + "はやめるので空プールには入れない！！");
			return;
		}

		delete(mem); // 既存の設定を削除
		;

		while (akiList.size() < itukara + 1) {
			akiList.add(new TreeSet<AkiMember>());
		}
		akiList.get(itukara).add(mem);
	}

	public void inc() {

		if (akiList.size() > 1) {
			Set<AkiMember> oneSet = akiList.get(1);
			akiList.get(0).addAll(oneSet);
			akiList.remove(1);
		}
	}


	public List<AkiMember> getList(int itukara) {
		List<AkiMember> list = new ArrayList<AkiMember>();

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


	public void delete(AkiMember m) {

		for (Set<AkiMember> set :akiList) {
			if (set.contains(m)) {
				set.remove(m);
			}

		}

	}



}
