package deus.enums;

public enum RetirementType {
	JIKO(Integer.valueOf(0)), // 自己都合
	KAISYA(Integer.valueOf(1)), // 会社都合
	NA(null); // 設定値無し

	Integer value;

    private RetirementType(Integer param) {
      this.value = param;
    }

    public Integer getInteger() {
    	return this.value;
    }

}
