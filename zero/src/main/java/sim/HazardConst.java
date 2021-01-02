package sim;

public class HazardConst {

	// 計算期間(単位月)
	public static final int KIKAN_CONST = 360;

	// 退職率操作定数
	public static final double YAMERITU_CONST = 0.0000001;
	//public static final double YAMERITU_CONST = 90000000;

	// 仕事の面白さ退職影響定数
	public static final double OMOSIROIDO_CONST = -5000;

	// 先輩うざい度
	public static final double SENPAIUZAI_CONST = 0.001;

	// 女性比率
	public static final double FEMALE_RITU_CONST = 0.5;

	// 女性のやめやすさ
	public static final double FEMALE_RTIR_CONST = 0.3;

	// 顧客の天使と悪魔フラグ
	// trueの場合、顧客に天使と悪魔がいるようになる。
	public static final boolean KAKYU_ANGEL_DEVIL = true;

	// 天使がいる比率
    public static final double ANGEL_RITU = 0.00;


	// 悪魔がいる比率
	public static final double DEVIL_RITU = 0.5;

	public static final int RETIRE_X_INDEX_NUM = 1;


    // proラムダ
	public static final double PRO_LAMBDA = 0.1;

    // メンバーラムダ
	public static final double MEM_LAMBDA = 21;

	// メンバー✖プロジェクトラムダ
	public static final double PRO_MEM_LAMBDA = 0.01;

    //S(t) = e−(λt)p = 5年と6月　5*12+6 で 0.5 0.25
    // メンバーp
	public static final double MEM_P = 0.9;




}
