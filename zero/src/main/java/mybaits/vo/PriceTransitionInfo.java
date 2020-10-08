package mybaits.vo;

import java.util.Date;

public class PriceTransitionInfo {

	//	tbl_id	tbl_id	serial
	private Integer tblId;

	public Integer getTblId() {
		return tblId;
	}
	public void setTblId(Integer tblId) {
		this.tblId = tblId;
	}

	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Integer getMenberMonths() {
		return menberMonths;
	}
	public void setMenberMonths(Integer menberMonths) {
		this.menberMonths = menberMonths;
	}
	public String getEnrolledHistId() {
		return enrolledHistId;
	}
	public void setEnrolledHistId(String enrolledHistId) {
		this.enrolledHistId = enrolledHistId;
	}
	public Date getPriceStartDate() {
		return priceStartDate;
	}
	public void setPriceStartDate(Date priceStartDate) {
		this.priceStartDate = priceStartDate;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	String memberId;
	Integer menberMonths;
	String enrolledHistId;
	Date priceStartDate;
	Integer price;


}
