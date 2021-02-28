package mybaits.vo;

import java.util.List;

public class CovariatesInfoParam {

	// 1期間あたりの月数
	int periodMonths = 1;

	public int getPeriodMonths() {
		return periodMonths;
	}

	public void setPeriodMonths(int periodMonths) {
		this.periodMonths = periodMonths;
	}



	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	boolean sex;

	public List<ClientInfo> getClientList() {
		return clientList;
	}

	public void setClientList(List<ClientInfo> clientList) {
		this.clientList = clientList;
	}



	public List<MemberHistInfo> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<MemberHistInfo> memberList) {
		this.memberList = memberList;
	}


	List<MemberHistInfo> memberList;

	List<ClientInfo> clientList;

}
