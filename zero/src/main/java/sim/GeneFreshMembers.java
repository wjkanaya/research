package sim;

import java.util.Random;
import java.util.Set;

import deus_proto.Member;


// 新人生成
public class GeneFreshMembers {
	public  int getFreshMembers(Set<Member> doukiSet ,Random random, int freshnum, int namecnt,int j) {
		if (j%12 == 0) {


			  Member member = null;
			  for (int i=0; i< freshnum;i++) {


				  namecnt++;

				  member = new Member();

				//  double kojinsa = random.nextGaussian() ;

				  if (random.nextDouble() < 0.1) {
					  member.name = namecnt + "子";
					  member.sex = 1;
				  } else {
					  member.name = namecnt + "郎";
					  member.sex = 0;
				  }
				  member.memberId = String.format("%04d", namecnt);
				  member.retire = 0;
				  member.yameritu = random.nextGaussian() ;
				  member.nouryoku = random.nextGaussian() ;

				  member.eikyoudo = MakePoasonRandom.senkeiNormalToInto( random.nextGaussian(), 0,1);
				  // 楽しさに影響の受けやすさ [0-1] 正規分布

				  member.seityoudo = MakePoasonRandom.senkeiNormalToInto( random.nextGaussian(), 0,1);
				  // 成長しやすさ [0-1] 正規分布

				  member.entT = j; // 入社時期
				  System.out.println(member.name + "君が入社しました");
				  doukiSet.add(member);
			  }

		  }
		return namecnt;
	}
}
