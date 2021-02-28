package deus.enums;

public enum PeriodType {
	MONTHS(1), // 一か月
	QUATERS(3), //３か月（
	YEARS(12);  //12か月
	int periodMonths;



    private PeriodType(int param) {
        this.periodMonths = param;
      }

      public Integer getPeriodMonths() {
      	return this.periodMonths;
      }


}
