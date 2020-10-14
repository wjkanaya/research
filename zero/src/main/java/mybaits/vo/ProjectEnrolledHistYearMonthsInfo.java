package mybaits.vo;

import java.util.Date;

public class ProjectEnrolledHistYearMonthsInfo {
	//	tbl_id	tbl_id	serial
	private Integer tblId;

	public void setTblId(Integer tblId) {
		this.tblId = tblId;
	}
	public Integer getTblId() {
		return tblId;
	}
    Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	public Date getStopDate() {
		return stopDate;
	}
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	public Integer getStopType() {
		return stopType;
	}
	public void setStopType(Integer stopType) {
		this.stopType = stopType;
	}

	Date joinDate;
	Date stopDate;
	Integer stopType;
	Integer yearMonths;

	public Integer getYearMonths() {
		return yearMonths;
	}
	public void setYearMonths(Integer yearMonths) {
		this.yearMonths = yearMonths;
	}

}
