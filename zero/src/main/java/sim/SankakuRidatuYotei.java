package sim;

import deus.enums.StopType;

public class SankakuRidatuYotei {


	public AkiMember mem;

	public Project pro;

	public int itukara;

	public StopType stopType;


	public boolean equals(Object anObject) {
		if (this == anObject) {
			return true;
		}
		if (anObject instanceof SankakuRidatuYotei) {
			SankakuRidatuYotei yotei = (SankakuRidatuYotei)anObject;
			return yotei.mem.equals(this.mem) && yotei.pro.equals(this.pro) &&
					yotei.itukara == this.itukara;
		}
		return false;
	}

}
