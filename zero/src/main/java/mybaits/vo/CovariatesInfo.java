package mybaits.vo;

import java.math.BigDecimal;
import java.util.Date;

public class CovariatesInfo {

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
	//	計算対象コード	culc_target_code	text
	String culcTargetCode;
	//	共変量コード	covariates_code	text
	String covariatesCode;

	// 共変量ラベル番号
	Integer covariatesLabelNum;

	public Integer getCovariatesLabelNum() {
		return covariatesLabelNum;
	}
	public void setCovariatesLabelNum(Integer covariatesLabelNum) {
		this.covariatesLabelNum = covariatesLabelNum;
	}
	//	有効状態開始日時	effect_start_time	timestamp
	Date effectStartTime;
	//計算ID	culc_id	text
	String culcId;

	// 共変量値
	BigDecimal covariatesValue;

	public BigDecimal getCovariatesValue() {
		return covariatesValue;
	}
	public void setCovariatesValue(BigDecimal covariatesValue) {
		this.covariatesValue = covariatesValue;
	}



}
