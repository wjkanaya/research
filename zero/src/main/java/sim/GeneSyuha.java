package sim;

import java.util.Random;

public class GeneSyuha {


	public static void main(String[] args) {

		System.out.println((5/2)*2);
	}
	public int nowNinzu(Random random,int jiki, int syuki,int maxnumin,double isouRan,int kizamiHaba){
		double ran = random.nextDouble();
		int kezuruMax =(int)Math.round(ran*(maxnumin-1));

		double result = kezuruMax *((Math.sin(2*Math.PI*(jiki/syuki)+ 2*Math.PI * isouRan)) + 1)/2;
		// 階段状に増減する場合を考慮
		// int型同士の商の性質を利用

		return (((int) Math.round(result))/kizamiHaba)* kizamiHaba;
	}


	public int makeSyukiRandom(Random random, int min, int max) {
		double ran = random.nextDouble();

		double logmin = Math.log(min);
		double logmax = Math.log(max);
		double logtarget = logmin + ran * (logmax - logmin);

		return (int)Math.round(Math.exp(logtarget));

	}

}
