package deus_proto;

public class Member  implements Comparable<Member>   {
	public int entT; // 入社時期
	public int retT = -1; // 退社時期
    public String memberId;
	public String name;
	public int sex;

	public double yameritu; // もともとのやめる率
	//
	public double eikyoudo; // 楽しさに影響の受けやすさ [0-1] 正規分布

	public double kisonouryoku;    //もともとの能力

	public double seityoudo; // 成長しやすさ [0-1] 正規分布

	// 楽しくなかったらプロジェクトを辞めたい
	public double tanosisajyuusi;

	// 成長しないならプロジェクトを辞めたい
	public double seityousitai;

	// 動的に変化する値
	public double omosiroido;// 仕事楽しい度

	public double nouryokukoujyoudo; // 能力向上度

	public int retire;

    @Override
    public int hashCode(){
        return memberId.hashCode();
    }


	public boolean equals(Object anObject) {
		if (this == anObject) {
			return true;
		}
		if (anObject instanceof Member) {
			Member m = (Member)anObject;
			return m.memberId.equals(this.memberId);
		}
		return false;
	}

	public int compareTo(Member o) {
		// TODO 自動生成されたメソッド・スタブ

		int result = this.memberId.compareTo(o.memberId);

		return result;
	}



}
