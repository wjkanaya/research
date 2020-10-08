package sim;

import java.util.Date;

import deus_proto.Member;

public class AkiMember {

	public Member member;

	public Date itukaraDate;

    @Override
    public int hashCode(){
        return member.name.hashCode();
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



}