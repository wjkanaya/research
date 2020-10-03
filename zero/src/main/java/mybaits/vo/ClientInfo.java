package mybaits.vo;
/**
 * 顧客情報
 * @author wjkan
 *
 */
public class ClientInfo {

	//	tbl_id	tbl_id	serial
	private Integer tblId;

	public Integer getTblId() {
		return tblId;
	}
	public void setTblId(Integer tblId) {
		this.tblId = tblId;
	}
	String clientId;
	String clientName;

	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}
