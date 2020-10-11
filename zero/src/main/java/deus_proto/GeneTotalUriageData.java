package deus_proto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mybaits.vo.MemberHistPriceInfo;
import mybaits.vo.PriceTransitionInfo;
import mybaits.vo.ProjectEnrolledHistInfo;
import mybatis.dao.MemberHistInfoDAO;
import sim.Util;

public class GeneTotalUriageData {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		Util.startTransaction();
		try {
			GeneTotalUriageData gene = new GeneTotalUriageData();
			gene.excute();

		} finally {
			Util.endTransaction();;
		}
	}

	private void excute() {


		Date nowDate = new Date();


		MemberHistInfoDAO dao = new MemberHistInfoDAO();

		List<MemberHistPriceInfo> list  = dao.selectAllMemberProjectEnrolledHistAndPrice();

		List<List<Integer>> priceDataList = new ArrayList<List<Integer>>();
		//
		//		int nowMonth = 0;
		//
		System.out.println(list.size());
		//

		for (MemberHistPriceInfo memberHistPriceInfo : list) {
			int nowMonth = 0;

			// 入社日
			Date enterDate = memberHistPriceInfo.getMemberHistInfo().getEnterDate();

			// 退社日
			Date lastDate =  memberHistPriceInfo.getMemberHistInfo().getRetirementDate();

			if (lastDate == null) {
				lastDate = new Date();
			}

			int zaisekiMonth = Util.differenceMonth(enterDate, lastDate);

			int nowPrice = 0;

			List<ProjectEnrolledHistInfo> histInfoList = memberHistPriceInfo.getProjectEnrolledHistInfoList();

			for ( ProjectEnrolledHistInfo histInfo :histInfoList) {

				Integer stopMemberMonths = histInfo.getStopMemberMonths();

				if (stopMemberMonths == null) {
					stopMemberMonths = Integer.valueOf(zaisekiMonth);
				}


				List<PriceTransitionInfo> priceList  = histInfo.getPriceTransitionInfoList();

				for (PriceTransitionInfo priceInfo :priceList) {

					int startMemberMonth = priceInfo.getMenberMonths();
					while (nowMonth < startMemberMonth) {

						if (priceDataList.size() == nowMonth) {
							priceDataList.add(new ArrayList<Integer>());
						}

						priceDataList.get(nowMonth).add(nowPrice);

						nowMonth++;
					}

					nowPrice = priceInfo.getPrice();
				}

				while (nowMonth < stopMemberMonths ) {
					if (priceDataList.size() == nowMonth) {
						priceDataList.add(new ArrayList<Integer>());
					}
					priceDataList.get(nowMonth).add(nowPrice);

					nowMonth++;
				}
			}

			nowPrice = 0;
			while (nowMonth < zaisekiMonth ) {
				if (priceDataList.size() == nowMonth) {
					priceDataList.add(new ArrayList<Integer>());
				}
				priceDataList.get(nowMonth).add(nowPrice);

				nowMonth++;
			}
		}

		for (int i = 0; i < priceDataList.size(); i++) {

			// 平均計算
			int sum = 0;
			List<Integer> priceList = priceDataList.get(i);

			for (Integer price :priceList) {
				sum += price.intValue();
			}
			double doubleSum = sum;
			double avg = doubleSum / priceList.size();

			// 標準偏差計算
			double sd = 0;

			if (priceList.size() > 1) {
				double bunsanSum = 0;
				for (Integer price :priceList) {
					bunsanSum += Math.pow(price.doubleValue() - avg,2);
				}

				double fuhenBunsan =  bunsanSum / (priceList.size() - 1);
				sd = Math.sqrt(fuhenBunsan);

			}

			System.out.println(i +":" + avg  + ":" + sd + ":" + priceDataList.get(i).size() );

		}

	}





}
