package mybaits.vo;
/**
 * 顧客情報
 * @author wjkan
 *
 */
public class CovariatesMst {

	public Integer getTblId() {
		return tblId;
	}

	public void setTblId(Integer tblId) {
		this.tblId = tblId;
	}

	public String getCovariatesCode() {
		return covariatesCode;
	}

	public void setCovariatesCode(String covariatesCode) {
		this.covariatesCode = covariatesCode;
	}

	public String getCovariatesName() {
		return covariatesName;
	}

	public void setCovariatesName(String covariatesName) {
		this.covariatesName = covariatesName;
	}

	public Integer getCovariatesType() {
		return covariatesType;
	}

	public void setCovariatesType(Integer covariatesType) {
		this.covariatesType = covariatesType;
	}

	public Integer getRangeStart() {
		return rangeStart;
	}

	public void setRangeStart(Integer rangeStart) {
		this.rangeStart = rangeStart;
	}

	public Integer getRangeEnd() {
		return rangeEnd;
	}

	public void setRangeEnd(Integer rangeEnd) {
		this.rangeEnd = rangeEnd;
	}

	//	tbl_id	tbl_id	serial
	Integer tblId;

	String covariatesCode;

	String covariatesName;

	Integer covariatesType;

	Integer rangeStart;

	Integer rangeEnd;


}
