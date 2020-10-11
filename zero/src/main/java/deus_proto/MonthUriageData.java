package deus_proto;

import java.util.Date;
import java.util.List;

public class MonthUriageData {

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public List<Integer> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<Integer> priceList) {
		this.priceList = priceList;
	}

	Date targetDate;

	List<Integer> priceList;

}
