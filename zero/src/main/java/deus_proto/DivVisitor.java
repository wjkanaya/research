package deus_proto;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixChangingVisitor;

public class DivVisitor implements RealMatrixChangingVisitor {

	RealMatrix target = null;

	public  DivVisitor(RealMatrix x) {
		this.target = x;
	}

	public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public double visit(int row, int column, double value) {
		// TODO 自動生成されたメソッド・スタブ
		return value / target.getEntry(row, column);
	}

	public double end() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

}
