package deus_proto;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import junit.framework.TestCase;

public class MatUtilTest extends TestCase {

	public void testMatScalaMulti() {
//		fail("まだ実装されていません");


	}

	public void testMatSub() {
//		fail("まだ実装されていません");
	}

	public void testMatMulti() {
//		fail("まだ実装されていません");
		double[][] A = new double[2][2];
		double[][] B = new double[2][1];

		A[0][0] = 1;
		A[0][1] = 2;
		A[1][0] = 3;
		A[1][1] = 5;


		B[0][0] = 7;
		B[1][0] = 11;

		double[][] result =  MatUtil.matMulti(A, B);

		assertEquals(result.length,2);
		assertEquals(result[0].length,1);

		RealMatrix AMat = MatrixUtils.createRealMatrix(A);
		RealMatrix BMat = MatrixUtils.createRealMatrix(B);

		double[][] ch = AMat.multiply(BMat).getData();

//		for (int i = 0; i < result.length; i++) {
//			for (int j = 0; j < result[0].length; j++) {
//
//				System.out.println(ch[i][j] + "," + result[i][j]);
//
//			}
//
//		}


		assertEquals(ch.length,2);
		assertEquals(ch[0].length,1);

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {

				assertEquals(ch[i][j], result[i][j]);

			}

		}

	}

	public void testGetVectorNorm() {
//		fail("まだ実装されていません");

		double[][] vector = new double[2][1];
		vector[0][0] = 6;
		vector[1][0] = 2;

		double vectorNorm = MatUtil.getVectorNorm(vector);

		RealMatrix vecMat = MatrixUtils.createRealMatrix(vector);

		System.out.println(Math.sqrt(5));
		assertEquals(vectorNorm,vecMat.getColumnVector(0).getNorm());
		System.out.println("--");

		RealMatrix rm = vecMat.copy();
		rm.walkInOptimizedOrder(new PowVisitor());

		System.out.println(rm);
		System.out.println("dfasdf");
	}

	public void testMatAdd() {
//		fail("まだ実装されていません");
	}

	public void testMatBroDiv() {
		double[][] vector = new double[2][1];
		vector[0][0] = 6;
		vector[1][0] = 2;
		double[][] vector2 = new double[2][1];
		vector2[0][0] = 7;
		vector2[1][0] = 4;

		RealMatrix re = MatUtil.matBroDiv( MatrixUtils.createRealMatrix(vector),
				MatrixUtils.createRealMatrix(vector2));

		assertEquals(vector[0][0]/vector2[0][0],re.getEntry(0, 0) );
		assertEquals(vector[1][0]/vector2[1][0],re.getEntry(1, 0) );

	}


}
