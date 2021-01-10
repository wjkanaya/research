package mybaits.vo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class YearCovariatesInfo {


	Integer years;
	Integer event;
	Integer count;

	Map<String, BigDecimal>  covariatesMap;

	public Integer getYears() {
		return years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}

	public Integer getEvent() {
		return event;
	}

	public void setEvent(Integer event) {
		this.event = event;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Map<String, BigDecimal> getCovariatesMap() {
		return covariatesMap;
	}

	public void setCovariatesMap(Map<String, Object> covariatesMap) {

		Set<Entry<String, Object>> set = covariatesMap.entrySet();
		Map<String, BigDecimal>  map = new TreeMap<String, BigDecimal>();

		for (Entry<String, Object> ent :set) {
			BigDecimal bd = null;
			if (ent.getValue() instanceof Integer) {

				Integer integerVal = (Integer)ent.getValue();
				bd = BigDecimal.valueOf(integerVal.intValue());


			} else if (ent.getValue() instanceof Double) {

				Double doubleVal = (Double)ent.getValue();
				bd = BigDecimal.valueOf(doubleVal.doubleValue());


			} else if (ent.getValue() instanceof BigDecimal) {
				bd = (BigDecimal)ent.getValue();
			} else {
				throw new RuntimeException();
			}
			map.put(ent.getKey(), bd);
		}

		this.covariatesMap = map;
	}
}
