package deus_proto;

import org.apache.commons.math3.linear.RealMatrixChangingVisitor;

public class PowVisitor implements RealMatrixChangingVisitor {

	double x = 0;

	public  PowVisitor() {
		this.x = 2;
	}

	public  PowVisitor(double x) {
		this.x = x;
	}

	public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public double visit(int row, int column, double value) {
		// TODO 自動生成されたメソッド・スタブ
		return Math.pow(value, x);

	}

	public double end() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

}
