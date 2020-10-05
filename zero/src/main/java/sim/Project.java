package sim;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import deus_proto.Member;

public class Project {

//	public int getItukara(int nowJiki) {
//
//		return jiki + nannkagetugo - nowJiki;
//
//	}

	// 状態0引き合い、1、実行中、2プロジェクト終了
	public int status;


	public int nexEigyouJiki;

	// 開始時期
	public int startJiki = -1;

	// 終了時期
	public int endJiki = -1;

//	// 提案時期 1
//	public int jiki;
//
//	//  提案時期  + 開始時期
//	public int nannkagetugo;


	// 名前
	public String name;

	// 客コード
	public Kyaku kyaku;

//    // 期間
//    public int itumade;
//
//    // のびる可能性
//    public Integer nobirukamo;

    // 最大人数
    public int maxnannin;

    // プロジェクト単位の長さど
    public double nagai;

    // 一人当たりの単金
    public int tankin;

    // 増減周期
    public int syuuki;

    // 増減範囲
    public double range;

    // 刻み幅比率
    public double kizamihiritu;
    // 周期位相
    public double syukiisou;

    // 参画中メンバーセット
    public Set<Member> memberSet = new HashSet<Member>();

    @Override
    public int hashCode(){
        return name.hashCode();
    }


	public boolean equals(Object anObject) {
		if (this == anObject) {
			return true;
		}
		if (anObject instanceof Project) {
			Project p = (Project)anObject;
			return p.name.equals(this.name);
		}
		return false;
	}

    public void doOneMonKeika() {
    	// 客の

        //-public double sinraido =0; // 信頼度
    	// 取引人月
    	int memberCount = memberSet.size();

    	// 取引人月
    	kyaku.torinin =+ memberCount;
    	for (Iterator<Member> itr = memberSet.iterator(); itr.hasNext();){
    		Member m = itr.next();
    		// 仕事楽しい度変化
    		m.omosiroido += m.eikyoudo * kyaku.tanosi;
    		// 能力向上
    		m.nouryokukoujyoudo += m.seityoudo + kyaku.sodatu;
    	}

    }



}