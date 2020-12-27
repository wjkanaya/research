package mybaits.vo;

public class YearEstimateInfoPre implements Comparable{

	Integer years;

	Integer count;

	Integer censored;

	public Integer getX0() {
		return x0;
	}

	public void setX0(Integer x0) {
		this.x0 = x0;
	}

	public Integer getX1() {
		return x1;
	}

	public void setX1(Integer x1) {
		this.x1 = x1;
	}

	public Integer getX2() {
		return x2;
	}

	public void setX2(Integer x2) {
		this.x2 = x2;
	}

	public Integer getX3() {
		return x3;
	}

	public void setX3(Integer x3) {
		this.x3 = x3;
	}

	public Integer getX4() {
		return x4;
	}

	public void setX4(Integer x4) {
		this.x4 = x4;
	}

	Integer x0;

	Integer x1;

	Integer x2;

	Integer x3;

	Integer x4;



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
		YearEstimateInfoPre o = (YearEstimateInfoPre) inO;
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

    	if (x0 != null && o.getX0()!= null) {
			result = this.x0.compareTo(o.getX0());
			if (result != 0) {
				return result;
			}
    	} else if (x0 == null && o.getX0()!= null) {
			return -1;
		} else if (x0 != null && o.getX0()== null) {
			return 1;
		}

    	if (x1 != null && o.getX1()!= null) {
    		result = this.x1.compareTo(o.getX1());
			if (result != 0) {
				return result;
			}
    	} else if (x1 == null && o.getX1()!= null) {
			return -1;
		} else if (x1 != null && o.getX1()== null) {
			return 1;
		}

    	if (x2 != null && o.getX2()!= null) {
    		result = this.x2.compareTo(o.getX2());
			if (result != 0) {
				return result;
			}
    	} else if (x2 == null && o.getX2()!= null) {
			return -1;
		} else if (x2 != null && o.getX2()== null) {
			return 1;
		}

    	if (x3 != null && o.getX3()!= null) {
    		result = this.x3.compareTo(o.getX3());
			if (result != 0) {
				return result;
			}
    	} else if (x3 == null && o.getX3()!= null) {
			return -1;
		} else if (x3 != null && o.getX3()== null) {
			return 1;
		}

    	if (x4 != null && o.getX4()!= null) {
    		result = this.x4.compareTo(o.getX4());
    	} else if (x4 == null && o.getX4()!= null) {
			return -1;
		} else if (x4 != null && o.getX4()== null) {
			return 1;
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
    	if (x0 != null) {
    		sb.append("x0").append(x0.toString());
    	}
      	if (x1 != null) {
    		sb.append("x1").append(x1.toString());
    	}
       	if (x2 != null) {
    		sb.append("x2").append(x2.toString());
    	}
       	if (x3 != null) {
    		sb.append("x3").append(x3.toString());
    	}
     	if (x4 != null) {
    		sb.append("x4").append(x4.toString());
    	}

     	return sb.hashCode();

    }

    @Override
    public boolean equals(Object obj) {
    	return compareTo((YearEstimateInfoPre)obj)  == 0;
	}
//
//	public int compareTo(YearEstimateInfo o) {
//
//		int result = 0;
//
//		if (years != null && o.getYears()!= null) {
//			result = this.years.compareTo(o.getYears());
//			if (result != 0) {
//				return result;
//			}
//		} else if (years == null && o.getYears()!= null) {
//			return -1;
//		} else if (years != null && o.getYears()== null) {
//			return 1;
//		}
//
//    	if (x0 != null && o.getX0()!= null) {
//			result = this.x0.compareTo(o.getX0());
//			if (result != 0) {
//				return result;
//			}
//    	} else if (x0 == null && o.getX0()!= null) {
//			return -1;
//		} else if (x0 != null && o.getX0()== null) {
//			return 1;
//		}
//
//    	if (x1 != null && o.getX1()!= null) {
//    		result = this.x1.compareTo(o.getX1());
//			if (result != 0) {
//				return result;
//			}
//    	} else if (x1 == null && o.getX1()!= null) {
//			return -1;
//		} else if (x1 != null && o.getX1()== null) {
//			return 1;
//		}
//
//    	if (x2 != null && o.getX2()!= null) {
//    		result = this.x2.compareTo(o.getX2());
//			if (result != 0) {
//				return result;
//			}
//    	} else if (x2 == null && o.getX2()!= null) {
//			return -1;
//		} else if (x2 != null && o.getX2()== null) {
//			return 1;
//		}
//
//    	if (x3 != null && o.getX3()!= null) {
//    		result = this.x3.compareTo(o.getX3());
//			if (result != 0) {
//				return result;
//			}
//    	} else if (x3 == null && o.getX3()!= null) {
//			return -1;
//		} else if (x3 != null && o.getX3()== null) {
//			return 1;
//		}
//
//    	if (x4 != null && o.getX4()!= null) {
//    		result = this.x4.compareTo(o.getX4());
//    	} else if (x4 == null && o.getX4()!= null) {
//			return -1;
//		} else if (x4 != null && o.getX4()== null) {
//			return 1;
//		}
//
//    	return result;
//	}


}
