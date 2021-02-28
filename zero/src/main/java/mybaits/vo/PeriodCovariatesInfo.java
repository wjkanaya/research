package mybaits.vo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import deus_proto.util.CovariatesCodeComparator;

public class PeriodCovariatesInfo implements Comparable {


	public Integer getPeriodMonths() {
		return periodMonths;
	}

	public void setPeriodMonths(Integer periodMonths) {
		this.periodMonths = periodMonths;
	}

	Integer periodMonths;

	Integer periods;

	public Integer getPeriods() {
		return periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	Integer event;
	Integer count;

	Map<String, BigDecimal>  covariatesMap;

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
		Map<String, BigDecimal>  map = new TreeMap<String, BigDecimal>(new CovariatesCodeComparator());

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


	public int compareTo(Object inO) {
		PeriodCovariatesInfo o = (PeriodCovariatesInfo) inO;
		int result = 0;

		if (periodMonths != null && o.getPeriodMonths()!= null) {
			result = this.periodMonths.compareTo(o.getPeriodMonths());
			if (result != 0) {
				return result;
			}
		} else if (periodMonths == null && o.getPeriodMonths()!= null) {
			return -1;
		} else if (periodMonths != null && o.getPeriodMonths()== null) {
			return 1;
		}

		if (periods != null && o.getPeriods()!= null) {
			result = this.periods.compareTo(o.getPeriods());
			if (result != 0) {
				return result;
			}
		} else if (periods == null && o.getPeriods()!= null) {
			return -1;
		} else if (periods != null && o.getPeriods()== null) {
			return 1;
		}

		if (covariatesMap != null ) {
			Set<String> keySet = covariatesMap.keySet();

			for (String key :keySet) {

				if(covariatesMap.get(key) != null && o.getCovariatesMap().get(key)!=null) {

					result = this.covariatesMap.get(key).compareTo(o.getCovariatesMap().get(key));
					if (result != 0) {
						return result;
					}
				} else if (covariatesMap.get(key) == null && o.getCovariatesMap().get(key)!=null) {
					return -1;
				} else if (covariatesMap.get(key) != null && o.getCovariatesMap().get(key)==null) {
					return 1;
				}
			}

		}

		if (event != null && o.getEvent() != null) {
			result = this.event.compareTo(o.getEvent());
			if (result != 0) {
				return result;
			}
		} else if (event == null && o.getEvent()!= null) {
			return -1;
		} else if (event != null && o.getEvent()== null) {
			return 1;
		}

    	return result;
	}


    @Override
    public int hashCode(){

    	StringBuilder sb = new StringBuilder();

    	if (periodMonths != null) {
    		sb.append("periodMonths").append(periodMonths.toString());
    	}

    	if (periods != null) {
    		sb.append("periods").append(periods.toString());
    	}

    	if (event != null) {
    		sb.append("event").append(event.toString());
    	}

    	if (count != null) {
    		sb.append("count").append(count.toString());
    	}

    	if (covariatesMap != null ) {
    		Set<Entry<String, BigDecimal>> entrySet = covariatesMap.entrySet();

    		for (Entry<String, BigDecimal> entry :entrySet) {
    			if (entry.getValue() != null) {
    				sb.append(entry.getKey()).append(entry.getValue().toString());
    			}
    		}
    	}


     	return sb.toString().hashCode();

    }

    @Override
    public boolean equals(Object obj) {
    	return compareTo((PeriodCovariatesInfo)obj)  == 0;
	}

}
