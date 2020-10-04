package sim;

public class Transaction {

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


	public boolean equals(Object anObject) {
		if (this == anObject) {
			return true;
		}
		if (anObject instanceof Transaction) {
			Transaction t = (Transaction)anObject;
			return t.pro.name.equals(this.pro.name) && t.jiki == this.jiki;
		}
		return false;
	}




}
