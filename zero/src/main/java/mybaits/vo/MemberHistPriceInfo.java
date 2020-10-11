package mybaits.vo;

import java.util.List;

public class MemberHistPriceInfo {

	public MemberHistInfo getMemberHistInfo() {
		return memberHistInfo;
	}

	public void setMemberHistInfo(MemberHistInfo memberHistInfo) {
		this.memberHistInfo = memberHistInfo;
	}

	public List<ProjectEnrolledHistInfo> getProjectEnrolledHistInfoList() {
		return projectEnrolledHistInfoList;
	}

	public void setProjectEnrolledHistInfoList(List<ProjectEnrolledHistInfo> projectEnrolledHistInfoList) {
		this.projectEnrolledHistInfoList = projectEnrolledHistInfoList;
	}

	MemberHistInfo memberHistInfo;

	List<ProjectEnrolledHistInfo> projectEnrolledHistInfoList;

}
