package mybaits.vo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class YearEstimateInfo implements Comparable{

	Integer years;

	Integer count;

	Integer censored;

	public Integer getLive() {
		return live;
	}

	public void setLive(Integer live) {
		this.live = live;
	}

	Integer live;


	Map<String, BigDecimal>  covariatesMap;

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

	public Integer getYears() {
		return years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCensored() {
		return censored;
	}

	public void setCensored(Integer censored) {
		this.censored = censored;
	}

	public int compareTo(Object inO) {
		YearEstimateInfo o = (YearEstimateInfo) inO;
		int result = 0;

		if (years != null && o.getYears()!= null) {
			result = this.years.compareTo(o.getYears());
			if (result != 0) {
				return result;
			}
		} else if (years == null && o.getYears()!= null) {
			return -1;
		} else if (years != null && o.getYears()== null) {
			return 1;
		}

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

    	return result;
	}


    @Override
    public int hashCode(){

    	StringBuilder sb = new StringBuilder();

    	if (years != null) {
    		sb.append("years").append(years.toString());
    	}
    	if (count != null) {
    		sb.append("count").append(count.toString());
    	}
    	if (censored != null) {
    		sb.append("censored").append(censored.toString());
    	}

		Set<Entry<String, BigDecimal>> entrySet = covariatesMap.entrySet();

		for (Entry<String, BigDecimal> entry :entrySet) {
			if (entry.getValue() != null) {
				sb.append(entry.getKey()).append(entry.getValue().toString());
			}
		}

     	return sb.hashCode();

    }

    @Override
    public boolean equals(Object obj) {
    	return compareTo((YearEstimateInfo)obj)  == 0;
	}

}
