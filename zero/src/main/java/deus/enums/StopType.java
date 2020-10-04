package deus.enums;

// 脱退種別
public enum StopType {
	// 0:顧客起因、1:自社起因、2:個人起因
	KOKYAKU(Integer.valueOf(0)),
	JISYA(Integer.valueOf(1)),
	KOJIN(Integer.valueOf(2)),
	NA(null); // 設定値無し

	Integer value;

    private StopType(Integer param) {
      this.value = param;
    }

    public Integer getInteger() {
    	return this.value;
    }


}
