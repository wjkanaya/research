package mybaits.vo;

import java.util.Date;

public class CovariatesEffectiveInfo {

	public String getCulcTargetCode() {
		return culcTargetCode;
	}
	public void setCulcTargetCode(String culcTargetCode) {
		this.culcTargetCode = culcTargetCode;
	}
	public String getCovariatesCode() {
		return covariatesCode;
	}
	public void setCovariatesCode(String covariatesCode) {
		this.covariatesCode = covariatesCode;
	}
	public Date getEffectStartTime() {
		return effectStartTime;
	}
	public void setEffectStartTime(Date effectStartTime) {
		this.effectStartTime = effectStartTime;
	}
	public String getCulcId() {
		return culcId;
	}
	public void setCulcId(String culcId) {
		this.culcId = culcId;
	}
	public Boolean getEffectFlg() {
		return effectFlg;
	}
	public void setEffectFlg(Boolean effectFlg) {
		this.effectFlg = effectFlg;
	}
	//	計算対象コード	culc_target_code	text
	String culcTargetCode;
	//	共変量コード	covariates_code	text
	String covariatesCode;
	//	有効状態開始日時	effect_start_time	timestamp
	Date effectStartTime;
	//計算ID	culc_id	text
	String culcId;
	//有効可否	effect_flg	boolean
	Boolean effectFlg;



}
