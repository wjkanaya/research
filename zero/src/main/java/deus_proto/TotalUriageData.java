package deus_proto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TotalUriageData {

	public List<MonthUriageData> getUriageDataList() {
		return uriageDataList;
	}

	public void setUriageDataList(List<MonthUriageData> uriageDataList) {
		this.uriageDataList = uriageDataList;
	}

	public int getNowIdx() {
		return nowIdx;
	}

	public void setNowIdx(int nowIdx) {
		this.nowIdx = nowIdx;
	}

	List<MonthUriageData> uriageDataList = new ArrayList<MonthUriageData>();

	int nowIdx = 0;

	public void init(Date initDate) {

		if (uriageDataList.isEmpty()) {

			MonthUriageData data = new MonthUriageData();
			data.setTargetDate(initDate);
			data.setPriceList(new ArrayList<Integer>());
			uriageDataList.add(data);
			nowIdx = 0;
		} else {

			for (int i = 0; i < uriageDataList.size();i++) {
				if (initDate.equals(uriageDataList.get(i).getTargetDate())) {
					nowIdx = i;
					return;
				}
			}

			throw new RuntimeException();

		}

	}

	public void set(int memberMonth, int price) {

		int jiki = nowIdx + memberMonth;

		if (jiki == uriageDataList.size() - 1 ) {

		    Calendar cal = Calendar.getInstance();

		    cal.setTime(uriageDataList.get(uriageDataList.size() - 1).getTargetDate());
		    cal.set(Calendar.DATE, 1);
		    cal.add(Calendar.MONTH, 1);

			MonthUriageData data = new MonthUriageData();
			data.setTargetDate(cal.getTime());
			data.setPriceList(new ArrayList<Integer>());
			uriageDataList.add(data);
		}

		uriageDataList.get(jiki).getPriceList().add(price);
	}

}
