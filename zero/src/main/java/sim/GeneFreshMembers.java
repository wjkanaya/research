package sim;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import deus_proto.Member;


// 新人生成
public class GeneFreshMembers {

	static Logger logger = LogManager.getLogger(GeneFreshMembers.class);

	public  int getFreshMembers(Set<Member> doukiSet ,SimRandom random, int freshnum, int namecnt,int j) {
		if (j%12 == 0) {


			  Member member = null;
			  for (int i=0; i< freshnum;i++) {


				  namecnt++;

				  member = new Member();

				//  double kojinsa = random.nextGaussian() ;

				  if (random.nextDouble() < HazardConst.FEMALE_RITU_CONST) {
					  member.name = namecnt + "子";
					  member.sex = 1;
				  } else {
					  member.name = namecnt + "郎";
					  member.sex = 0;
				  }
				  member.memberId = String.format("M%04d", namecnt);
				  member.retire = 0;
				  member.yameritu = random.nextGaussian() ;
				  member.kisonouryoku = random.nextGaussian() ;

				  // 楽しくなかったらプロジェクトを辞めたい
				  member.tanosisajyuusi = MakePoasonRandom.senkeiNormalToInt( random.nextGaussian(), 0,1);

				  // 成長できなかったらプロジェクトを辞めたい
				  member.seityousitai = MakePoasonRandom.senkeiNormalToInt( random.nextGaussian(), 0,1);


				  member.eikyoudo = MakePoasonRandom.senkeiNormalToInt( random.nextGaussian(), 0,1);
				  // 楽しさに影響の受けやすさ [0-1] 正規分布

				  member.seityoudo = MakePoasonRandom.senkeiNormalToInt( random.nextGaussian(), 0,1);
				  // 成長しやすさ [0-1] 正規分布

				  member.entT = j; // 入社時期
				  logger.debug(member.name + "君が入社しました");
				  doukiSet.add(member);
			  }

		  }
		return namecnt;
	}
}
