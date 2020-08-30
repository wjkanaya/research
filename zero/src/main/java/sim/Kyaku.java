package sim;

public class Kyaku {

	public String name;

	// 客コード
	public int kyakuCd;

    // 要求技術平易度(信頼度の高まりやすさ)
	public double heiiDo; //

	// 人数多い度
	public double ooi; // たくさん雇ってくれる

	// 人数増せる率
	public double maxooi; // 最大人数比率

	// 単金高い度
	public double takai; // 高い矢印売上に繋がる

	// 単金増せる率
	public double maxtakai; // 最大単金比率

	// 長く続く度
	public double nagai; // 長く続く

    // 仕事楽しい度
    public double tanosi; // 退職率に効果i

    // 成長できる度
    public double sodatu; // 単金上昇率に効果

// 動的変数
    public double sinraido =0; // 信頼度

    public int torinin = 0; // 取引人月



}
