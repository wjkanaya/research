package deus_proto;

public class DDouble implements Comparable<DDouble> {

		public DDouble(double value)  {
			this.value = value;
			this.square = value * value;
		}

		double square;
		double value;

		public double getSquare() {
			return square;
		}

		public void setSquare(double square) {
			this.square = square;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}


//		public boolean equals(Object anObject) {
//			if (this == anObject) {
//				return true;
//			}
//			if (anObject instanceof DDouble) {
//				DDouble m = (DDouble)anObject;
//				return m.square == this.square;
//			}
//			return false;
//		}

		public int compareTo(DDouble o) {

			DDouble dd = (DDouble)o;

			if (this.square < dd.square) {
				return -1;
			} else if ( this.square > dd.square) {
				return 1;
			} else {
				return 0;
			}

		}

}
