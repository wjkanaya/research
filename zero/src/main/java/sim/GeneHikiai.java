package sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// 営業引き合い
public class GeneHikiai {

	// お客さんの数
	public static int KYAKKU_SU = 10000;

	public List<Kyaku> list = null;

	public static void main(String[] args) {
		 Random random = new Random();


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
//	    // 最初の人数
//	    public Integer nannin;
//
//	    // 一人当たりの単金
//	    public int tankin;

		List<Project> prolist = new ArrayList<Project>();

		GeneHikiai gen = new GeneHikiai();

		for (int j=0;j < 10;j++) {
			gen.makeHikiai(random, j);
		}

	}

	// 引き合いデータ作成
	private  List<Project> makeHikiai(Random random, int j) {
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
				int nannin = MakePoasonRandom.getPoisson(
						(double)MakePoasonRandom.senkeiNormalToInto(list.get(i).ooi, 1, 5));
				if (nannin > 0) {
					//　来客
					Project pro = new Project();
					pro.jiki = j;
					pro.nannkagetugo = MakePoasonRandom.getPoisson(
							(double)MakePoasonRandom.senkeiNormalToInto(random.nextGaussian(), 1, 12));
					pro.name = list.get(i).name;
					pro.kyaku= list.get(i);
					pro.nannin = nannin;
					pro.tankin =  MakePoasonRandom.getPoisson(
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
//
//				    // 増減刻み幅比率
//				    public double kizamihiritu;
					pro.kizamihiritu =  random.nextDouble();

					// 周期の位相
					pro.syukiisou = random.nextDouble();

					System.out.println(j + ":" + pro.name + ":" + pro.nannkagetugo + "月後:" + pro.nannin + "人:" + pro.tankin );
				}

			}
		}
		return prolist;
	}

}
