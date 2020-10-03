package deus.enums;

public enum ProjectStatus {
	TYU(Integer.valueOf(0)), // 運営中
	KAN(Integer.valueOf(1)), // 完了
	NA(null); // 設定値無し

	Integer value;

    private ProjectStatus(Integer param) {
      this.value = param;
    }

    public Integer getInteger() {
    	return this.value;
    }

}
