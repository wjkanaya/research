package sim;

public class MakePoasonRandom {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

	}

	// ポアソン乱数生成方法
	public static int getPoisson(SimRandom random, double lambda) {

		double L = Math.exp(-lambda);
		double p = 1.0;
		int k = 0;

		do {

			k++;
			p *= random.nextDouble();
		} while (p > L);

		return k - 1;

	}

	public static int senkeiNormalToInt(double nran, int start,int haba) {


		int heikin = (start + (start + haba)) /2;
		double nibunnohaba = ((double)haba)/2;

		// 正規分布の線形性による。
		int result = (int)Math.round(Math.sqrt(nibunnohaba) * nran + heikin);

	//	x = np.vectorize(int)(np.sqrt(2.5) *np.random.normal(0, 1, 1000) + 5)
		return result;
	}


}
