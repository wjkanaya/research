package sim;

import java.util.Iterator;

import deus_proto.Member;

public class MakeHazard {

    // proラムダ
	public static final double PRO_LAMBDA = 0.1;

    // メンバーラムダ
	public static final double MEM_LAMBDA = 150;

	// メンバー✖プロジェクトラムダ
	public static final double PRO_MEM_LAMBDA = 0.01;

    //S(t) = e−(λt)p = 5年と6月　5*12+6 で 0.5 0.25
    // メンバーp
	public static final double MEM_P = 0.9;


    // プロジェクト内で何かがおこる。
	public void  makeStress(Project pro) {
		// プロジェクト内で何かがおこる。
		// プロジェクトによるストレス。
		// プロジェクトによる成長。
		for (Iterator<Member> memitr = pro.memberSet.iterator();memitr.hasNext();) {
			Member mem = memitr.next();

			// やめやすさに関連する
			mem.omosiroido += mem.eikyoudo * pro.kyaku.tanosi;

			//  仕事
			mem.nouryokukoujyoudo += mem.seityoudo * pro.kyaku.sodatu;

		}

		// お客さんの好感度の変化。
        // TODDO

	}

	// メンバーがプロジェクトから抜けたい
	public double culcTaiProhazard(Member mem, Project pro) {


		double h = PRO_MEM_LAMBDA * Math.exp(- mem.tanosisajyuusi * mem.eikyoudo * pro.kyaku.tanosi
				                                 - mem.seityousitai * pro.kyaku.sodatu);
		return h;

	}



    // プロジェクトが終わる
	public double culcprohzard(Project pro) {

		// 参画人数
		int memberNum = pro.memberSet.size();

		double nouryoku = 0;

		for (Iterator<Member> memitr =pro.memberSet.iterator();memitr.hasNext();) {
			Member mem = memitr.next();
			nouryoku += mem.nouryokukoujyoudo;
		}

		// 能力平均
		double nouryokuHeikin = nouryoku / memberNum;


		double h = PRO_LAMBDA*Math.exp((1/3)*(-0.2*pro.kyaku.nagai-0.5*pro.nagai-0.05 * nouryokuHeikin));

		return  h;
	}




	public double culcMemHzard(int allcnt, int sennpaicnt, Member mem, int keika) {
		double h = (MEM_P/(Math.pow(MEM_LAMBDA, MEM_P))) * Math.pow(keika,MEM_P-1); //ハザード値


		  double yammeritu =h * Math.exp (
				  mem.yameritu   * HazardConst.YAMERITU_CONST
		        + mem.omosiroido * HazardConst.OMOSIROIDO_CONST   // 仕事面白いと思っていたらやめない
		        // + mem.sex * 10
				  + HazardConst.SENPAIUZAI_CONST  * (
						  2* (((double)sennpaicnt)/ allcnt-0.5) // 後輩の比率が大きければやめにくい
						  )
				  ) ;
		return yammeritu;
		// return 1.0;
	}
}
