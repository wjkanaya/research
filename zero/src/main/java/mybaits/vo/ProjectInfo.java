package mybaits.vo;

import java.util.Date;

public class ProjectInfo {

	//	tbl_id	tbl_id	serial
	private Integer tblId;

	public Integer getTblId() {
		return tblId;
	}
	public void setTblId(Integer tblId) {
		this.tblId = tblId;
	}

	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(Integer projectStatus) {
		this.projectStatus = projectStatus;
	}
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	String projectId;
	String projectName;

	String clientId;
	Date startDate;
	Date endDate;
	Integer projectStatus;

}
