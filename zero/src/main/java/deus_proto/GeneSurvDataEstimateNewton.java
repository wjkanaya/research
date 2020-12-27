package deus_proto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import mybaits.vo.YearEstimateInfoPre;
import mybatis.dao.MemberHistInfoDAO;
import sim.HazardConst;
import sim.Util;

public class GeneSurvDataEstimateNewton {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		Util.startTransaction();
		try {
			GeneSurvDataEstimateNewton gene = new GeneSurvDataEstimateNewton();
			gene.excute();

		} finally {
			Util.endTransaction();;
		}

	}

	private void excute() {


		MemberHistInfoDAO dao = new MemberHistInfoDAO();

		List<YearEstimateInfoPre> list = dao.selectMemberHistYearEstimateInfo();

		System.out.println(list.size());

		List<Integer> retireCountList = new ArrayList<Integer>();
		List<Map<YearEstimateInfoPre, Integer>> retireCountMeisaiList
		= new ArrayList<Map<YearEstimateInfoPre, Integer>>();


		List<Integer> notRetireCountList = new ArrayList<Integer>();
		List<Map<YearEstimateInfoPre, Integer>> notRetireCountMeisaiList
		= new ArrayList<Map<YearEstimateInfoPre, Integer>>();

		for (YearEstimateInfoPre info :list ) {

			int years = info.getYears();

			while (retireCountList.size() <= years) {
				retireCountList.add(Integer.valueOf(0));
				retireCountMeisaiList.add(new TreeMap<YearEstimateInfoPre, Integer>());
				notRetireCountList.add(Integer.valueOf(0));
				notRetireCountMeisaiList.add(new TreeMap<YearEstimateInfoPre, Integer>());
			}

			for (int i = 0; i < years; i++) {
				Integer addCount = Integer.valueOf(info.getCount().intValue()
						+ info.getCensored().intValue());
				notRetireCountList.set(i,
						Integer.valueOf(
								notRetireCountList.get(i).intValue()
								+ addCount.intValue()));
				notRetireCountMeisaiList.get(i).put(info, addCount);

			}

			retireCountList.set(years,
					Integer.valueOf(
							retireCountList.get(years).intValue()
							+ info.getCount().intValue())) ;
			retireCountMeisaiList.get(years).put(info, info.getCount().intValue());


		}


		// 冗長削除
		for (int i = retireCountList.size()-1; i >=0; i--) {
			if (retireCountList.get(i).intValue() != 0 ||
					notRetireCountList.get(i).intValue() != 0) {
				break;
			}
			retireCountList.remove(i);
			retireCountMeisaiList.remove(i);
			notRetireCountList.remove(i);
			notRetireCountMeisaiList.remove(i);
		}


		// 何か年か？
		int yearRange = retireCountList.size();


		int betaSize = 1 + retireCountList.size() + getXList(list.get(0)).size();

		double[] betaArr = new double[betaSize];
		for (int i = 0; i < betaArr.length; i++) {
			betaArr[i] = 1;
		}

		double sa = 1000;
		int num = 0;
		int max = 100000;
		do {

			// 1階微分
			//double[] deltaBetaArr = new double[betaSize];
			List<Double> deltaBetaList = new ArrayList<Double>(betaSize);

			// deltaBetaArr[0] = − （iが1からNまでの総和）(PDi−Yi)*1



			// PD計算キャッシュ
			// betaArrの値が変わるたびに更新してください。
			List<Map<YearEstimateInfoPre, Double>> pdCacheList =
					new ArrayList<Map<YearEstimateInfoPre, Double>>();

			for (int year = 0; year < yearRange; year++) {
				pdCacheList.add(new HashMap<YearEstimateInfoPre, Double>());
			}


			List<DDouble> sumList = new ArrayList<DDouble>();
			Map<YearEstimateInfoPre, Integer> map = null;
			Set<Entry<YearEstimateInfoPre, Integer>> set = null;

			// デルタbeta0 (全体)
			for (int year = 0; year < yearRange; year++) {
				// 継続中のデータなのでY=0
				int Y = 0;
				// 継続
				map = notRetireCountMeisaiList.get(year);
				makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, -1);

				// 退職
				Y = 1;
				map = retireCountMeisaiList.get(year);
				makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, -1);
			}

			// 昇順ソート
			Collections.sort(sumList);
			// パネルデータの総和部分
			double sigma = 0;

			for (DDouble dd :sumList) {
				sigma += dd.getValue();
			}

			deltaBetaList.add(Double.valueOf(-sigma));

			// 経過年数それぞれのデルタbeta
			for (int year = 0; year < yearRange; year++) {
				sumList.clear();
				// 継続中のデータなのでY=0
				int Y = 0;
				// 継続
				map = notRetireCountMeisaiList.get(year);
				makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, -1);

				// 退職
				Y = 1;
				map = retireCountMeisaiList.get(year);
				makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, -1);

				// 昇順ソート
				Collections.sort(sumList);
				// パネルデータの総和部分
				sigma = 0;

				for (DDouble dd :sumList) {
					sigma += dd.getValue();
				}
				deltaBetaList.add(Double.valueOf(-sigma));
			}

			// 経過年数それぞれのデルタbeta
			for (int xIndex = 0; xIndex < HazardConst.RETIRE_X_INDEX_NUM; xIndex++) {
				sumList.clear();
				// 年数
				for (int year = 0; year < yearRange; year++) {
					// 継続中のデータなのでY=0
					int Y = 0;
					// 継続
					map = notRetireCountMeisaiList.get(year);
					makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, xIndex);

					// 退職
					Y = 1;
					map = retireCountMeisaiList.get(year);
					//	set = map.entrySet();
					makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, xIndex);
				}

				// 昇順ソート
				Collections.sort(sumList);
				// パネルデータの総和部分
				sigma = 0;
				for (DDouble dd :sumList) {
					sigma += dd.getValue();
				}
				deltaBetaList.add(Double.valueOf(-sigma));

			}

			// 二階微分デルタ
			// beta0(全体) × beta0(全体)

			sumList.clear();

			List<List<Double>> delta2BetaList = new ArrayList<List<Double>>();

			// beta0(全体)


			delta2BetaList.add(new ArrayList<Double>());

			// beta0(全体) × beta0(全体)
			sumList.clear();
			for (int year = 0; year < yearRange; year++) {
				// 継続
				map = notRetireCountMeisaiList.get(year);
				makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, year, -1, -1);

				map = retireCountMeisaiList.get(year);
				makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, year, -1, -1);
			}

			// 昇順ソート
			Collections.sort(sumList);
			// パネルデータの総和部分
			sigma = 0;
			for (DDouble dd :sumList) {
				sigma += dd.getValue();
			}
			delta2BetaList.get(0).add(sigma);

			// beta0(全体) × 経過月
			for (int year = 0; year < yearRange; year++) {
				sumList.clear();

				// 継続
				map = notRetireCountMeisaiList.get(year);
				makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, year, -1,-1);

				map = retireCountMeisaiList.get(year);
				makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, year, -1,-1);

				// 昇順ソート
				Collections.sort(sumList);
				// パネルデータの総和部分
				sigma = 0;

				for (DDouble dd :sumList) {
					sigma += dd.getValue();
				}

				delta2BetaList.get(0).add(sigma);
			}

			// beta0(全体) × 共変量
			for (int xIndex = 0; xIndex < HazardConst.RETIRE_X_INDEX_NUM; xIndex++) {
				sumList.clear();
				// 年数
				for (int year = 0; year < yearRange; year++) {
					// 継続中のデータなのでY=0
					int Y = 0;
					// 継続
					map = notRetireCountMeisaiList.get(year);
					makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, year, xIndex, -1);

					// 退職
					Y = 1;
					map = retireCountMeisaiList.get(year);
					//	set = map.entrySet();
					makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, year, xIndex, -1);
				}

				// 昇順ソート
				Collections.sort(sumList);
				// パネルデータの総和部分
				sigma = 0;
				for (DDouble dd :sumList) {
					sigma += dd.getValue();
				}
				delta2BetaList.get(0).add(sigma);
			}


			// 経過年
			for (int nowYear = 0; nowYear < yearRange; nowYear++) {

				delta2BetaList.add(new ArrayList<Double>());

				// 経過年1×beta0
				// =beta0×経過年1

				delta2BetaList.get(nowYear+1).add(delta2BetaList.get(0).get(nowYear+1));

				// 経過
				for (int year = 0; year < yearRange; year++) {

					if (nowYear != year) { // 経過年が違うと0を設定する。
						delta2BetaList.get(nowYear+1).add(Double.valueOf(0));
						continue;
					}

					sumList.clear();
					// 継続
					map = notRetireCountMeisaiList.get(nowYear);
					makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, nowYear, -1, -1);

					map = retireCountMeisaiList.get(nowYear);
					makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, nowYear, -1, -1);

					// 昇順ソート
					Collections.sort(sumList);
					// パネルデータの総和部分
					sigma = 0;

					for (DDouble dd :sumList) {
						sigma += dd.getValue();
					}

					delta2BetaList.get(nowYear+1).add(sigma);
				}

				// 経過年0×共変量x
				for (int xIndex = 0; xIndex < HazardConst.RETIRE_X_INDEX_NUM; xIndex++) {
					sumList.clear();
					// 経過
					for (int year = 0; year < yearRange; year++) {
						// 継続
						map = notRetireCountMeisaiList.get(nowYear);
						makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, nowYear, xIndex, -1);

						// 退職
						map = retireCountMeisaiList.get(nowYear);
						//	set = map.entrySet();
						makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, nowYear, xIndex, -1);

					}

					// 昇順ソート
					Collections.sort(sumList);
					// パネルデータの総和部分
					sigma = 0;
					for (DDouble dd :sumList) {
						sigma += dd.getValue();
					}
					delta2BetaList.get(nowYear+1).add(sigma);
				}
			}

			// 共変量
			for (int nowXIndex = 0; nowXIndex < HazardConst.RETIRE_X_INDEX_NUM; nowXIndex++) {

				delta2BetaList.add(new ArrayList<Double>());
				//x0
				// 共変量x0×beta0
				// =beta0×共変量x0
				delta2BetaList.get(delta2BetaList.size() - 1).add(delta2BetaList.get(0).get(delta2BetaList.size() - 1));

				// 共変量x0×経過年
				// =経過年×共変量x0
				// 経過
				for (int year = 0; year < yearRange; year++) {
					delta2BetaList.get(delta2BetaList.size() - 1).add(delta2BetaList.get(year+1).get(delta2BetaList.size() - 1));
				}

				// 共変量x×共変量x
				for (int xIndex = 0; xIndex < HazardConst.RETIRE_X_INDEX_NUM; xIndex++) {
					sumList.clear();
					for (int year = 0; year < yearRange; year++) {
						// 継続
						map = notRetireCountMeisaiList.get(year);
						makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, year, nowXIndex, xIndex);

						// 退職
						map = retireCountMeisaiList.get(year);
						//	set = map.entrySet();
						makeSumList2(yearRange, betaArr, pdCacheList, sumList, map, year, nowXIndex, xIndex);
					}

					// 昇順ソート
					Collections.sort(sumList);
					// パネルデータの総和部分
					sigma = 0;
					for (DDouble dd :sumList) {
						sigma += dd.getValue();
					}
					delta2BetaList.get(delta2BetaList.size() - 1).add(sigma);
				}
			}



//			System.out.print("Δβ=(");
//			for (int i = 0; i < deltaBetaList.size(); i++) {
//				if (i < deltaBetaList.size()-1) {
//					System.out.print(deltaBetaList.get(i) +",");
//				} else {
//					System.out.println(deltaBetaList.get(i) + ")");
//				}
//			}
//			System.out.println("ΔΔβ=(");
//			for (int i = 0; i < delta2BetaList.size(); i++) {
//				List<Double> tempList = delta2BetaList.get(i);
//
//				System.out.print("(");
//				for (int j = 0; j < tempList.size(); j++) {
//					if (j < tempList.size()-1) {
//						System.out.print(tempList.get(j) +",");
//					} else {
//						System.out.println(tempList.get(j) + ")");
//					}
//				}
//			}
//			System.out.println(")");

			//betaArr
			double[][]betaVec = new double[betaSize][1];
			for (int i = 0; i < betaSize; i++) {
				betaVec[i][0] = betaArr[i];
			}

			double[][]deltaBetaVec = new double[betaSize][1];
			for (int i = 0; i < betaSize; i++) {
				deltaBetaVec[i][0] = deltaBetaList.get(i).doubleValue();
			}

			double[][] delta2BetaVec = new double[betaSize][betaSize];
			for (int i = 0; i < betaSize; i++) {
				for (int j = 0; j < betaSize; j++) {
					delta2BetaVec[i][j] = delta2BetaList.get(i).get(j).doubleValue();
				}
			}
			RealMatrix betaMatrix = MatrixUtils.createRealMatrix(betaVec);
			RealMatrix deltaBetaMatrix = MatrixUtils.createRealMatrix(deltaBetaVec);
			//System.out.println("--------");
			//System.out.println(deltaBetaMatrix);
			//System.out.println("--------");


			//		System.out.println("ddd  " + retireCountList.get(27));
			//		System.out.println("ddd2 " + notRetireCountList.get(27));
			RealMatrix delta2BetaMatrix = MatrixUtils.createRealMatrix(delta2BetaVec);
			RealMatrix delta2BetaInv = MatrixUtils.blockInverse(delta2BetaMatrix, 0);
			//System.out.println(delta2BetaInv);
			//double[][] data = delta2BetaInv.getData();
			EigenDecomposition ed = new EigenDecomposition(delta2BetaMatrix);
			//System.out.println("行列式: " + ed.getDeterminant());

			RealMatrix deltaMatrix = delta2BetaInv.multiply(deltaBetaMatrix);
			double[][] deltaData =  deltaMatrix.getData();

			double temp = 0;
			for (int i = 0; i < betaSize; i++) {
				temp += deltaData[i][0] * deltaData[i][0];
			}

			// 差を設定
			sa = Math.sqrt(temp);

			System.out.println("差：" + sa);


			RealMatrix nextBetaMatrix = betaMatrix.subtract(deltaMatrix);


			System.out.println(nextBetaMatrix);

			double[][] nextBetaData =  nextBetaMatrix.getData();

			for (int i = 0; i < betaSize; i++) {
				betaArr[i] = nextBetaData[i][0];
			}

			num++;
		} while (sa >= 1);

		System.out.println("なんと1よりさがったよ！");

		//		for (int i = 0; i < retireCountList.size(); i++) {
		//			System.out.println("" + i+":" + notRetireCountList.get(i) +
		//					":" + retireCountList.get(i));
		//		}

	}




	private void makeSumList(int yearRange, double[] betaArr, List<Map<YearEstimateInfoPre, Double>> pdCacheList,
			List<DDouble> sumList, Map<YearEstimateInfoPre, Integer> map, int year, int Y, int xIndex) {
		Set<Entry<YearEstimateInfoPre, Integer>> set;
		set = map.entrySet();
		for (Entry<YearEstimateInfoPre, Integer> entry  :set) {

			List<Integer> xList = getXList(entry.getKey());

			if (xIndex >=0 && xList.get(xIndex).intValue() == 0) {
				continue;
			}

			double pd = 0;
			if (pdCacheList.get(year).get(entry.getKey()) == null) {
				// キャッシュに値がない
				// PD計算
				pd  = culcPD(betaArr, year, yearRange, xList);
				pdCacheList.get(year).put(entry.getKey(), Double.valueOf(pd));
			} else {
				pd = pdCacheList.get(year).get(entry.getKey()).doubleValue();
			}

			//  共変量(経過年の場合は1)
			int x = 1;

			if (xIndex >=0) {
				//
				x = xList.get(xIndex).intValue();
			}


			double v = (pd - Y) * x * entry.getValue().intValue();
			// 今日
			sumList.add(new DDouble(v));
		}
	}

	private void makeSumList2(int yearRange, double[] betaArr, List<Map<YearEstimateInfoPre, Double>> pdCacheList,
			List<DDouble> sumList, Map<YearEstimateInfoPre, Integer> map, int year, int xIndex0, int xIndex1) {
		Set<Entry<YearEstimateInfoPre, Integer>> set;
		set = map.entrySet();
		for (Entry<YearEstimateInfoPre, Integer> entry  :set) {

			List<Integer> xList = getXList(entry.getKey());

			if (xIndex0 >=0 && xList.get(xIndex0).intValue() == 0) {
				continue;
			}
			if (xIndex1 >=0 && xList.get(xIndex1).intValue() == 0) {
				continue;
			}


			double pd = 0;
			if (pdCacheList.get(year).get(entry.getKey()) == null) {
				// キャッシュに値がない
				// PD計算
				pd  = culcPD(betaArr, year, yearRange, xList);
				pdCacheList.get(year).put(entry.getKey(), Double.valueOf(pd));
			} else {

				pd = pdCacheList.get(year).get(entry.getKey()).doubleValue();

			}

			//  共変量(経過年の場合は1)
			int xi = 1;
			int xj = 1;

			if (xIndex0 >=0) {
				//
				xi = xList.get(xIndex0).intValue();
			}
			if (xIndex1 >=0) {
				//
				xj = xList.get(xIndex1).intValue();
			}

			// ∑i=1NPDi・(1−PDi)・Xi,j・Xi,jj
			//		∂2L(β)∂βj∂βjj=∑i=1NPDi・(1−PDi)・Xi,j・Xi,jj
			double v = pd * (1-pd) * xi * xj * entry.getValue().intValue();
			// 今日
			sumList.add(new DDouble(v));
		}
	}
	private List<Integer> getXList(YearEstimateInfoPre info) {
		List<Integer> result = new ArrayList<Integer>();
		if (info.getX0() != null) {
			result.add(info.getX0());
		}
		if (info.getX1() != null) {
			result.add(info.getX1());
		}
		if (info.getX2() != null) {
			result.add(info.getX2());
		}
		if (info.getX3() != null) {
			result.add(info.getX3());
		}
		if (info.getX4() != null) {
			result.add(info.getX4());
		}

		return result;
	}



	private double culcPD(double[] betaArr, int year, int yearRange, List<Integer> xlist ) {
		return 1 / ( 1  +  Math.exp(-culcZ(betaArr, year, yearRange, xlist)));

	}

	private double culcZ(double[] betaArr, int year, int yearRange, List<Integer> xlist) {

		double result = 0;

		// 絶対値が小さいほうから足す
		List<DDouble> resultList = new ArrayList<DDouble>();

		resultList.add(new DDouble(betaArr[0]));

		resultList.add(new DDouble(betaArr[year + 1]));

		for (int i = 0; i < xlist.size(); i++) {
			double v = betaArr[yearRange + 1 + i] * xlist.get(i).intValue();
			resultList.add(new DDouble(v));
		}

		// 昇順ソート
		Collections.sort(resultList);

		for (DDouble data :resultList) {
			result += data.getValue();
		}

		return result;
	}

}

//class DDouble implements Comparable<DDouble> {
//
//	public DDouble(double value)  {
//		this.value = value;
//		this.square = value * value;
//	}
//
//	double square;
//	double value;
//
//	public double getSquare() {
//		return square;
//	}
//
//	public void setSquare(double square) {
//		this.square = square;
//	}
//
//	public double getValue() {
//		return value;
//	}
//
//	public void setValue(double value) {
//		this.value = value;
//	}
//
//
//	public boolean equals(Object anObject) {
//		if (this == anObject) {
//			return true;
//		}
//		if (anObject instanceof DDouble) {
//			DDouble m = (DDouble)anObject;
//			return m.square == this.square;
//		}
//		return false;
//	}
//
//	public int compareTo(DDouble o) {
//
//		DDouble dd = (DDouble)o;
//
//		if (this.square < dd.square) {
//			return -1;
//		} else if ( this.square > dd.square) {
//			return 1;
//		} else {
//			return 0;
//		}
//
//	}
