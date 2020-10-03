package mybaits.vo;

public class ProjectMemberNumInfo {

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
	public Integer getProjectMonths() {
		return projectMonths;
	}
	public void setProjectMonths(Integer projectMonths) {
		this.projectMonths = projectMonths;
	}
	public Integer getMemberNum() {
		return memberNum;
	}
	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}
	String projectId;
	Integer projectMonths;
	Integer memberNum;

}
