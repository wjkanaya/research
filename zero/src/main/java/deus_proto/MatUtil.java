package deus_proto;

import org.apache.commons.math3.linear.RealMatrix;

public class MatUtil {
//	System.out.println("A-B: " + matrixA.subtract(matrixB));
//	System.out.println("3A: " + matrixA.scalarMultiply(3));
	public static double[][] matScalaMulti(double[][]A, double b) {

		double[][] result = new double[A.length][A[0].length];
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				result[i][j] = A[i][j] * b;
			}
		}
		return result;
	}

	public static double[][] matSub(double[][] A,double[][] B) {
		return matAdd(A,matScalaMulti(B,-1));

	}

	public static double[][] matMulti(double[][] A,double[][] B) {

		TotalCounter c = new TotalCounter();

		// Aの行数×Bの列数
		double[][] result = new double[A.length][B[0].length];
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < B[0].length; j++) {
				c.clear();
				for (int k = 0; k < A[0].length; k++) {
					c.set(A[i][k] * B[k][j]);
				}
				result[i][j] = c.getTotal();
			}
		}

		return result;

	}
	public static double getVectorNorm(double[][] v) {
		double sum = 0;
		for (int i = 0; i < v.length; i++) {
			sum += v[i][0]*v[i][0];
		}
		return Math.sqrt(sum);

	}


//	System.out.println("A+B: " + matrixA.add(matrixB));
	public static double[][] matAdd(double[][] A,double[][] B) {
		double[][] result = new double[A.length][A[0].length];
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				result[i][j] = A[i][j] + B[i][j];
			}
		}
		return result;
	}

	public static RealMatrix matBroPow(RealMatrix A ,double p) {
		RealMatrix result = A.copy();
		result.walkInOptimizedOrder(new PowVisitor(p));
		return result;

	}


	public static RealMatrix matBroDiv(RealMatrix A ,RealMatrix B) {
		RealMatrix result = A.copy();
		result.walkInOptimizedOrder(new DivVisitor(B));
		return result;

	}



}
