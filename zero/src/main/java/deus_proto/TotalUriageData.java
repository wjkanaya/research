package deus_proto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TotalUriageData {

	List<MonthUriageData> uriageDataList = new ArrayList<MonthUriageData>();

	int nowIdx = 0;

	public void init(Date initDate) {

		if (uriageDataList.isEmpty()) {

			MonthUriageData data = new MonthUriageData();
			data.setTargetDate(initDate);
			data.setPriceList(new ArrayList<Integer>());
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

}
