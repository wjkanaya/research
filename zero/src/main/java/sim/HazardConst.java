package sim;

public class HazardConst {

	// 計算期間(単位月)
	public static final int KIKAN_CONST = 360;

	// 退職率操作定数
	public static final double YAMERITU_CONST = 0.0000001;
	//public static final double YAMERITU_CONST = 90000000;

	// 仕事の面白さ退職影響定数
	public static final double OMOSIROIDO_CONST = -0.000001;

	// 先輩うざい度
	public static final double SENPAIUZAI_CONST = 0.000001;

	// 女性比率
	public static final double FEMALE_RITU_CONST = 0.5;

	// 女性のやめやすさ
	public static final double FEMALE_RTIR_CONST = 5;

    // 退職に絡む経過年以外の共変量数
	public static final int RETIRE_X_INDEX_NUM = 1;

}
