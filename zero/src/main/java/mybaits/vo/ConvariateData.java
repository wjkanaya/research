package mybaits.vo;

import java.math.BigDecimal;

public class ConvariateData {

	String convariateCode;

	public int getConvariateLabel() {
		return convariateLabel;
	}

	public void setConvariateLabel(int convariateLabel) {
		this.convariateLabel = convariateLabel;
	}

	int convariateLabel;

	BigDecimal value;

	public String getConvariateCode() {
		return convariateCode;
	}

	public void setConvariateCode(String convariateCode) {
		this.convariateCode = convariateCode;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
