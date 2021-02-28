package deus_proto;

import java.util.List;

import mybaits.vo.PeriodCovariatesInfo;

public interface GetSurvData {

	public List<PeriodCovariatesInfo> getData(List<String> targetCodeList);

	public String getCulcTargetCode();

	public int getPeriodMonths();
	public void setPeriodMonths(int periodMonths);

	public int selectMaxLastPeriod();




}
