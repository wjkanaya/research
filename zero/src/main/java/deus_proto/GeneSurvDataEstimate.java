package deus_proto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import mybaits.vo.YearEstimateInfo;
import mybatis.dao.MemberHistInfoDAO;
import sim.HazardConst;
import sim.Util;

public class GeneSurvDataEstimate {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		Util.startTransaction();
		try {
			GeneSurvDataEstimate gene = new GeneSurvDataEstimate();
			gene.excute();

		} finally {
			Util.endTransaction();;
		}

	}

	private void excute() {
		MemberHistInfoDAO dao = new MemberHistInfoDAO();

		List<YearEstimateInfo> list = dao.selectMemberHistYearEstimateInfo();

		System.out.println(list.size());

		List<Integer> retireCountList = new ArrayList<Integer>();
		List<Map<YearEstimateInfo, Integer>> retireCountMeisaiList
		= new ArrayList<Map<YearEstimateInfo, Integer>>();


		List<Integer> notRetireCountList = new ArrayList<Integer>();
		List<Map<YearEstimateInfo, Integer>> notRetireCountMeisaiList
		= new ArrayList<Map<YearEstimateInfo, Integer>>();

		for (YearEstimateInfo info :list ) {

			int years = info.getYears();

			while (retireCountList.size() <= years) {
				retireCountList.add(Integer.valueOf(0));
				retireCountMeisaiList.add(new TreeMap<YearEstimateInfo, Integer>());
				notRetireCountList.add(Integer.valueOf(0));
				notRetireCountMeisaiList.add(new TreeMap<YearEstimateInfo, Integer>());

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

		// 何か年か？
		int yearRange = retireCountList.size();

		List betaList = new ArrayList<Double>();

		int betaSize = 1 + retireCountList.size() + getXList(list.get(0)).size();

		double[] betaArr = new double[betaSize];

		for (int i = 0; i < betaArr.length; i++) {
			betaArr[i] = 0;
		}

		// 1階微分
		//double[] deltaBetaArr = new double[betaSize];
		List<Double> deltaBetaList = new ArrayList<Double>(betaSize);

		// deltaBetaArr[0] = − （iが1からNまでの総和）(PDi−Yi)*1



		// PD計算キャッシュ
		// betaArrの値が変わるたびに更新してください。
		List<Map<YearEstimateInfo, Double>> pdCacheList =
				new ArrayList<Map<YearEstimateInfo, Double>>();

		for (int year = 0; year < yearRange; year++) {
			pdCacheList.add(new HashMap<YearEstimateInfo, Double>());
		}


		List<DDouble> sumList = new ArrayList<DDouble>();
		Map<YearEstimateInfo, Integer> map = null;
		Set<Entry<YearEstimateInfo, Integer>> set = null;

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
			set = map.entrySet();
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
			set = map.entrySet();
			makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, -1);

			// 昇順ソート
			Collections.sort(sumList);

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
				set = map.entrySet();
				makeSumList(yearRange, betaArr, pdCacheList, sumList, map, year, Y, xIndex);
			}

			// 昇順ソート
			Collections.sort(sumList);

			for (DDouble dd :sumList) {
				sigma += dd.getValue();
			}
			deltaBetaList.add(Double.valueOf(-sigma));

		}

		for (int i = 0; i < retireCountList.size(); i++) {
			System.out.println("" + i+":" + notRetireCountList.get(i) +
					":" + retireCountList.get(i));
		}

	}




	private void makeSumList(int yearRange, double[] betaArr, List<Map<YearEstimateInfo, Double>> pdCacheList,
			List<DDouble> sumList, Map<YearEstimateInfo, Integer> map, int year, int Y, int xIndex) {
		Set<Entry<YearEstimateInfo, Integer>> set;
		set = map.entrySet();
		for (Entry<YearEstimateInfo, Integer> entry  :set) {

			List<Integer> xList = getXList(entry.getKey());

			if (xIndex >=0 && xList.get(xIndex).intValue() == 0) {
				continue;
			}

			double pd = 0;
			if (!pdCacheList.get(year).containsKey(entry.getKey())) {
				// キャッシュに値がない
				// PD計算
				pd  = culcPD(betaArr, year, yearRange, xList);
				pdCacheList.get(year).put(entry.getKey(), Double.valueOf(pd));
			} else {
				pd = pdCacheList.get(year).get(entry.getKey()).doubleValue();
			}

			//  共変量(経過年の場合は1)
			int x = 1;

			if (xIndex >=0 && xList.get(xIndex).intValue() == 0) {
				//
				x = xList.get(xIndex).intValue();
			}


			double v = (pd - Y) * x * entry.getValue().intValue();
			// 今日
			sumList.add(new DDouble(v));
		}
	}


	private List<Integer> getXList(YearEstimateInfo info) {
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
		return 1 / ( 1  +  culcZ(betaArr, year, yearRange, xlist));

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

class DDouble implements Comparable<DDouble> {

	public DDouble(double value)  {
		this.value = value;
		this.square = value * value;
	}

	double square;
	double value;

	public double getSquare() {
		return square;
	}

	public void setSquare(double square) {
		this.square = square;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}


	public boolean equals(Object anObject) {
		if (this == anObject) {
			return true;
		}
		if (anObject instanceof DDouble) {
			DDouble m = (DDouble)anObject;
			return m.square == this.square;
		}
		return false;
	}

	public int compareTo(DDouble o) {

		DDouble dd = (DDouble)o;

		if (this.square < dd.square) {
			return -1;
		} else if ( this.square > dd.square) {
			return 1;
		} else {
			return 0;
		}

	}

}