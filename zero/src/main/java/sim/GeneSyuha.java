package sim;

import java.util.Random;

public class GeneSyuha {


	// プロジェクト周期
	// 最小
	public static int PRO_SYUKI_MIN = 6;
	// 最大
	public static int PRO_SYUKI_MAX = 120;



	public static void main(String[] args) {

		System.out.println((5/2)*2);
	}

	/**
	 *
	 */
	public int nowNinzu(int jiki, int syuki,int maxnumin,double isouRan,double kizamihiritu,double zougenRan){

		int kezuruMax =(int)Math.round(zougenRan*(maxnumin-1));

        int kizamihaba = (int)Math.ceil((kezuruMax / 2) * kizamihiritu);

        if (kizamihaba == 0) {
        	kizamihaba = 1;
        }


		double result = kezuruMax *((Math.sin(2*Math.PI*(jiki/syuki)+ 2*Math.PI * isouRan)) + 1)/2;
		// 階段状に増減する場合を考慮
		// int型同士の商の性質を利用

		return (((int) Math.round(result))/kizamihaba)* kizamihaba;
	}


	public int makeSyukiRandom(Random random) {
		double ran = random.nextDouble();
		double logmin = Math.log(PRO_SYUKI_MIN);
		double logmax = Math.log(PRO_SYUKI_MAX);
		double logtarget = logmin + ran * (logmax - logmin);

		return (int)Math.round(Math.exp(logtarget));

	}



}
