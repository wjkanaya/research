package mybaits.vo;

import java.util.List;

public class CovariatesInfoParam {


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


	List<ClientInfo> clientList;

}
