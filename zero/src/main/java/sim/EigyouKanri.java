package sim;

import java.util.HashSet;
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



}
