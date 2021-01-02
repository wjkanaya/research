package sim;

import java.util.ArrayList;
import java.util.List;


// 営業引き合い
public class GeneHikiai {

	// お客さんの数
	public static int KYAKKU_SU = 10000;


	public  static double SIKIITI= 0.0001;
	// 客リスト

	public List<Kyaku> list = null;

	// 半年
	public static int NINZU_RANGE = 6;


	public static void main(String[] args) {
		 SimRandom random = new SimRandom(1234);


//		// TODO 自動生成されたメソッド・スタブ
//		public String name;

//
//
//		// 提案時期 1
//		public int jiki;
//tanosi
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

//		List<Project> prolist = new ArrayList<Project>();


		GeneHikiai gen = new GeneHikiai();

		for (int j=0;j < 1;j++) {
			gen.makeHikiai(random, j);
		}

	}

	// 引き合いデータ作成
	public  List<Transaction> makeHikiai(SimRandom random, int jiki) {
		makeKyaku(random); // 客を生成する

		List<Transaction> prolist = new ArrayList<Transaction>();
		for (int i=0; i<list.size();i++) {

			if  ((list.get(i).torinin ==0  &&  random.nextDouble() < list.get(i).hikiaiDo) ||
					(list.get(i).torinin >0  &&  random.nextDouble() < list.get(i).hikiaiDo*list.get(i).otokuiDo * (1 + list.get(i).torinin/12) ) ) {
				int nannin = MakePoasonRandom.getPoisson(random,
						(double)MakePoasonRandom.senkeiNormalToInt(list.get(i).ooi, 1, 15));
				if (nannin > 0) {
					//　来客

					Transaction tran = new Transaction();

					Project pro = new Project();

					tran.pro = pro;

					tran.jiki = jiki;


					tran.nannkagetugo = MakePoasonRandom.getPoisson(random,
							(double)MakePoasonRandom.senkeiNormalToInt(random.nextGaussian(), 1, 12));

					pro.name = Util.getGeneProName(random, list.get(i).name, jiki);
					pro.nexEigyouJiki = tran.jiki + tran.nannkagetugo +1;

					pro.kyaku= list.get(i);
					pro.maxnannin = nannin;
					pro.tankin =  MakePoasonRandom.getPoisson(random,
							(double)MakePoasonRandom.senkeiNormalToInt(list.get(i).takai, 40, 60));
					prolist.add(tran);

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


					int minnum = pro.maxnannin;

					for (int j= 0; j < NINZU_RANGE; j++) {

						int n = syuha.nowNinzu(tran.jiki+tran.nannkagetugo + j, pro.syuuki, pro.maxnannin, pro.syukiisou, pro.kizamihiritu , pro.range);

						if (n < minnum) {
							minnum = n;
						}

					}

					tran.nannin = minnum;


					//int n1 = syuha.nowNinzu(jiki, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
					//int n2 = syuha.nowNinzu(jiki+6, pro.syuuki, pro.nannin, pro.syukiisou, pro.kizamihiritu , pro.range);
					//public int nowNinzu(int jiki, int syuki,int maxnumin,double isouRan,int kizamiHaba,double kizamiRan){

					//System.out.println(jiki + ":" + pro.name + ":" + pro.nannkagetugo + "月後:" + n1 + "人(半年後"+ n2 +"):" + pro.tankin );
				}

			}
		}
		return prolist;
	}

	private void makeKyaku(SimRandom random) {
		if (list == null) {


			List<Kyaku> list = new ArrayList<Kyaku>();
			for (int i=0; i<KYAKKU_SU;i++) {
				Kyaku k = new Kyaku();


				k.hikiaiDo = SIKIITI * MakePoasonRandom.getPoisson(random,
						(double)MakePoasonRandom.senkeiNormalToInt(random.nextGaussian(), 1, 15));


				k.otokuiDo = MakePoasonRandom.getPoisson(random,
						(double)MakePoasonRandom.senkeiNormalToInt(random.nextGaussian(), 1, 100));

				k.kyakuCd = String.format("K%05d", i);
				k.heiiDo = random.nextGaussian();
				k.ooi = random.nextGaussian();
				k.maxooi = random.nextGaussian();
				k.takai = random.nextGaussian();
				k.maxtakai = random.nextGaussian();
				k.nagai = random.nextGaussian();
				k.tanosi = random.nextGaussian();
				double unmei = 1;
				if (HazardConst.KAKYU_ANGEL_DEVIL) {
					unmei =  random.nextDouble();
				}

				if (unmei < HazardConst.ANGEL_RITU) {
					k.name = "株式会社"+ i + "興業(天使)";
					k.sodatu = 1;
					k.tanosi = 2;

				} else if (unmei < HazardConst.ANGEL_RITU + HazardConst.DEVIL_RITU) {
					k.name = "株式会社"+ i + "興業(悪魔)";
					k.sodatu = 0;
					k.tanosi = -2000;

				} else {
					k.name = "株式会社"+ i + "興業";
					k.sodatu = MakePoasonRandom.senkeiNormalToInt(random.nextGaussian(), 0, 1) ;
					k.tanosi = random.nextGaussian();
				}


				list.add(k);
				//			random.nextGaussian()

			}
			this.list = list;

		}
	}

}
