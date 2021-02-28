package deus_proto;

public class DeusConst {

	// 共変量種別 0:boolean,1:カテゴリ,2:順序,3:整数,4:実数
	public static final int COV_TYPE_BOOL = 0;

	public static final int COV_TYPE_CATE = 1;

	public static final int COV_TYPE_ORDR = 2;

	public static final int COV_TYPE_INTE = 3;

	public static final int COV_TYPE_REAL = 4;

	// 計算対象コード
	// 退職
	public static final String CT0001 = "CT0001";

	// プロジェクト終了(客都合)
	public static final String CT0101 = "CT0101";

	// プロジェクト終了(会社都合)
	public static final String CT0102 = "CT0102";

	// 退プロ(客都合)
	public static final String CT0201 = "CT0201";

	// 退プロ(メンバー都合)

	public static final String CT0202 = "CT0202";

	// 退プロ(会社都合)
	public static final String CT0203 = "CT0203";

	// 共変量コード
	// beta0
	public static final String C00000 = "C00000";

	// 経過年数
	// public static final String C00001 = "C00001";
	//	性別
	public static final String C00002 = "C00002";
	// 経過月数
	// public static final String C00003	 = "C00003";
	// 経過四半期数
	// public static final String C00005	 = "C00005";


	// 顧客共変量接頭辞
	public static final String CLIENT_PREFIX = "K";

	// 社員共変量接頭辞
	public static final String MEMBER_PREFIX = "M";

	// 期間共変量接頭辞
	public static final String PERIOD_PREFIX = "P";




}
