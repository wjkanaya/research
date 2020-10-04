package deus.enums;

// 在籍状態
public enum EnrolledStatus {
	SANNAI(Integer.valueOf(0)), // 0:参画内定
	TYU(Integer.valueOf(1)),    // 1:運営中
	DATNAI(Integer.valueOf(2)), // 2:脱退内定
	DAT(Integer.valueOf(2)),    // 3:脱退内定

	NA(null); // 設定値無し

	Integer value;
	// 0:参画内定、1:参画中、2:脱退内定、3:脱退

    private EnrolledStatus(Integer param) {
      this.value = param;
    }

    public Integer getInteger() {
    	return this.value;
    }
}
