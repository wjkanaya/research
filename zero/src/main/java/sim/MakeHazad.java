package sim;

import deus_proto.Member;

public class MakeHazad {

    // proラムダ
	public static final double PRO_LAMBDA = 0.1;

    // メンバーラムダ
	public static final double MEM_LAMBDA = 150;
    //S(t) = e−(λt)p = 5年と6月　5*12+6 で 0.5 0.25
    // メンバーp
	public static final double MEM_P = 0.9;

    // プロジェクトが終わる
	public double culcProHzard(Project pro,Member mem) {
        // expのなかはトータルで-2から2ぐらいが望ましい
		double h = PRO_LAMBDA*Math.exp((1/3)*(-0.2*pro.kyaku.nagai-0.5*pro.nagai-0.3*mem.nouryoku));

		return  h;
	}

	public double culcMemHzard(int allcnt, int sennpaicnt, Member mem, int keika) {
		double h = (MEM_P/(Math.pow(MEM_LAMBDA, MEM_P))) * Math.pow(keika,MEM_P-1); //ハザード値


		  double yammeritu =h * Math.exp (mem.yameritu* 0.0000001
				  + 0.000001  * (
						  2* (((double)sennpaicnt)/ allcnt-0.5)
						  )
				  ) ;
		return yammeritu;
	}
}
