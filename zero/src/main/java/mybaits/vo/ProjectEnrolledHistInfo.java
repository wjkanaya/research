package mybaits.vo;

import java.util.Date;

public class ProjectEnrolledHistInfo {
	//	tbl_id	tbl_id	serial
	private Integer tblId;

	public Integer getTblId() {
		return tblId;
	}
	public void setTblId(Integer tblId) {
		this.tblId = tblId;
	}

	public String getEnrolledHistId() {
		return enrolledHistId;
	}
	public void setEnrolledHistId(String enrolledHistId) {
		this.enrolledHistId = enrolledHistId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Integer getBranchNum() {
		return branchNum;
	}
	public void setBranchNum(Integer branchNum) {
		this.branchNum = branchNum;
	}
	public Date getJoinDate() {
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
	public Integer getEnrolledStatus() {
		return enrolledStatus;
	}
	public void setEnrolledStatus(Integer enrolledStatus) {
		this.enrolledStatus = enrolledStatus;
	}
	String enrolledHistId;
	String memberId;
	String projectId;
	Integer branchNum;
	Date joinDate;
	Date stopDate;
	Integer stopType;
	Integer enrolledStatus;

}
