package sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// 営業引き合い
public class GeneHikiai {

	// お客さんの数
	public static int KYAKKU_SU = 10000;

	// 直近人数数

	public List<Kyaku> list = null;

	public static void main(String[] args) {
		 Random random = new Random(1234);


//		// TODO 自動生成されたメソッド・スタブ
//		public String name;

//
//
//		// 提案時期 1
//		public int jiki;
//
//		// 名前
//		public String name;
//
//		// 客コード
//		public int kyakuCd;
//
//	   // いつから
//	    public Date itukara;
//
//	    // いつまで
//	    public Date itumade;
//
//	    // のびる可能性
//	    public Integer nobirukamo;


//
//	    // 一人当たりの単金
//	    public int tankin;

		List<Project> prolist = new ArrayList<Project>();

		GeneHikiai gen = new GeneHikiai();

		for (int j=0;j < 1;j++) {
			gen.makeHikiai(random, j);
		}

	}

	// 引き合いデータ作成
	private  List<Project> makeHikiai(Random random, int jiki) {
		if (list == null) {


			List<Kyaku> list = new ArrayList<Kyaku>();
			for (int i=0; i<KYAKKU_SU;i++) {
				Kyaku k = new Kyaku();

				k.name = "株式会社"+ i + "興業";
				k.kyakuCd = i;
				k.heiiDo = random.nextGaussian();
				k.ooi = random.nextGaussian();
				k.maxooi = random.nextGaussian();
				k.takai = random.nextGaussian();
				k.maxtakai = random.nextGaussian();
				k.nagai = random.nextGaussian();
				k.tanosi = random.nextGaussian();
				k.sodatu =  random.nextGaussian();
				list.add(k);
				//			random.nextGaussian()

			}
			this.list = list;

		}


		List<Project> prolist = new ArrayList<Project>();
		for (int i=0; i<KYAKKU_SU;i++) {
			if  (random.nextDouble() < 0.0015) {
				int nannin = MakePoasonRandom.getPoisson(random,
						(double)MakePoasonRandom.senkeiNormalToInto(list.get(i).ooi, 1, 15));
				if (nannin > 0) {
					//　来客
					Project pro = new Project();
					pro.jiki = jiki;
					pro.nannkagetugo = MakePoasonRandom.getPoisson(random,
							(double)MakePoasonRandom.senkeiNormalToInto(random.nextGaussian(), 1, 12));
					pro.name = list.get(i).name;
					pro.kyaku= list.get(i);
					pro.nannin = nannin;
					pro.tankin =  MakePoasonRandom.getPoisson(random,
							(double)MakePoasonRandom.senkeiNormalToInto(list.get(i).takai, 40, 40));
					prolist.add(pro);

//				    // 増減周期
//				    public int syuuki;
					GeneSyuha syuha= new GeneSyuha();
					pro.syuuki = syuha.makeSyukiRandom(random);
//
//				    // 増減範囲
//				    public double range;
					pro.range = random.nextDouble();

//				    // 刻み幅比率
//				    public double kizamihiritu;
					pro.kizamihiritu =  random.nextDouble();

					// 周期の位相
					pro.syukiisou = random.nextDouble();
//					double ran = random.nextDouble();
					int n1 = syuha.nowNinzu(jiki, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
					int n2 = syuha.nowNinzu(jiki+6, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
					//public int nowNinzu(int jiki, int syuki,int maxnumin,double isouRan,int kizamiHaba,double kizamiRan){

					System.out.println(jiki + ":" + pro.name + ":" + pro.nannkagetugo + "月後:" + n1 + "人(半年後"+ n2 +"):" + pro.tankin );
				}

			}
		}
		return prolist;
	}

}
