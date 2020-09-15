package deus_proto;

public class Member {
	public int entT; // 入社時期
	public int retT; // 退社時期
    public String memberId;
	public String name;
	public int sex;

	public double yameritu; // もともとのやめる率
	//
	public double eikyoudo; // 楽しさに影響の受けやすさ [0-1] 正規分布

	public double kisonouryoku;    //もともとの能力

	public double seityoudo; // 成長しやすさ [0-1] 正規分布

	// 動的に変化する値
	public double omosiroido;// 仕事楽しい度

	public double nouryokukoujyoudo; // 能力向上度

	public int retire;

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

}
