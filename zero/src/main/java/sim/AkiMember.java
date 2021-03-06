package sim;

import deus_proto.Member;

public class AkiMember implements Comparable<AkiMember>  {

	public Member member;

	public int itukara;

    @Override
    public int hashCode(){
        return member.memberId.hashCode();
    }

	public boolean equals(Object anObject) {
		if (this == anObject) {
			return true;
		}

		if (anObject instanceof Member) {
			Member m = (Member)anObject;
			return m.memberId.equals(this.member.memberId);
		}

		if (anObject instanceof AkiMember) {
			AkiMember m = (AkiMember)anObject;
			return m.member.memberId.equals(this.member.memberId);
		}
		return false;
	}

	public int compareTo(AkiMember o) {
		// TODO 自動生成されたメソッド・スタブ
		return this.member.memberId.compareTo(o.member.memberId);
	}



}
