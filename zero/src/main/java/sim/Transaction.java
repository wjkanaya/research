package sim;

public class Transaction implements Comparable<Transaction> {

	// 増減フラグ(false=増加、true=減少)
	boolean genFlg;

	// 新規客(0)、既存客+新規プロジェクト(1)、既存客+既存プロジェクト(終了)(2)、既存客+既存プロジェクト(営業中)(3)
	int transType;

	public Project pro;

	public int getItukara(int nowJiki) {

		return jiki + nannkagetugo - nowJiki;

	}

	// 提案時期 1
	public int jiki;

	//  提案時期  + 開始時期
	public int nannkagetugo;


    // 期間
    public int itumade;

    // のびる可能性
    public Integer nobirukamo;

    //要求人数
    public int nannin;

    // 充当人数
    public int juutounin;


    public int hashCode() {
        return (this.pro.id + this.jiki).hashCode();
    }

	public boolean equals(Object anObject) {
		if (this == anObject) {
			return true;
		}
		if (anObject instanceof Transaction) {
			Transaction t = (Transaction)anObject;
			return t.pro.id.equals(this.pro.id) && t.jiki == this.jiki;
		}
		return false;
	}

	public int compareTo(Transaction o) {
		// TODO 自動生成されたメソッド・スタブ

		int result = this.pro.id.compareTo(o.pro.id);

		if (result == 0) {
			result = this.jiki  - o.jiki;
		}

		return result;
	}




}
