package mybaits.vo;

import java.util.Date;

public class ProjectInfo {
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
	public String getClientDd() {
		return clientDd;
	}
	public void setClientDd(String clientDd) {
		this.clientDd = clientDd;
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
	public Integer getProject_status() {
		return project_status;
	}
	public void setProject_status(Integer project_status) {
		this.project_status = project_status;
	}
	String projectId;
	String projectName;
	String clientDd;
	Date startDate;
	Date endDate;
	Integer project_status;

}
