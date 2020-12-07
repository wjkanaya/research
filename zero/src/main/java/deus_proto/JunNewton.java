package deus_proto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class JunNewton {
	public static void main(String[] args) {


		//gpx = []
		List<Double> gpx = new ArrayList<Double>();

		//gpy = []
		List<Double> gpy = new ArrayList<Double>();

		//
		//
		//I = np.zeros((2,2))
		//I[0,0] = 1.0
		//I[1,1] = 1.0
		double[][] I = new double[2][2];
		I[0][0] = 1.0;
		I[1][1] = 1.0;

		double[][] H = I;
		//
		//#定数、黄金比
		double gold = 0.618034;
		int h = 1;
		//
		//nowp = np.zeros((2,1))
		double[][] nowp = new double[2][1];

		//nowp[0,0] = -10
		nowp[0][0] = 10.0;
		//nowp[1,0] = 0
		nowp[1][0] = 0;
		RealMatrix nowPMat = MatrixUtils.createRealMatrix(nowp);

		//for j in range(10):
		for (int j = 0; j < 10; j++) {

//		    print("現点：" + str(nowp))
			System.out.println("現点：" + nowPMat);
			//
//			    gpx.append(nowp[0])
			gpx.add(nowPMat.getEntry(0, 0));
//			    gpy.append(nowp[1])
			gpy.add(nowPMat.getEntry(1, 0));
			//
//			    w, v = LA.eig(H)
			RealMatrix hMat = MatrixUtils.createRealMatrix(H);
			EigenDecomposition hMatEd = new EigenDecomposition(hMat);

			//			    print("固有値:" + str(w))
			double[] eigens = hMatEd.getRealEigenvalues();
			for(int i=0; i<eigens.length; i++) {
				System.out.println("固有値: " + str(eigens[i]));
				//	System.out.println("固有ベクトル: " + ed.getEigenvector(i));
			}

			//
//			    vectr = - H @ delta(nowp)
			RealMatrix vectorMat =
					hMat.multiply(MatrixUtils.createRealMatrix(delta(nowPMat.getData())))
					.scalarMultiply(-1);


//			    # 正規化
//			    vectr = vectr / np.sqrt((vectr ** 2).sum())
			vectorMat =
					vectorMat.scalarMultiply(1 / vectorMat.getColumnVector(0).getNorm());


			//
//			    print("ベクトル：" + str(vectr))
			System.out.println("ベクトル：" + vectorMat);
			//

//			    p1 = nowp + 0 * vectr * h

			RealMatrix p1 = nowPMat.add(vectorMat.scalarMultiply(0*h));
//		    p2 = nowp + 1 * vectr * h
			RealMatrix p2 = nowPMat.add(vectorMat.scalarMultiply(1*h));
//
//		    p3 = nowp + 2 * vectr * h
			RealMatrix p3 = nowPMat.add(vectorMat.scalarMultiply(2*h));


			RealMatrix al_s = null;
			RealMatrix al_e = null;

			int nowI = 0;
//		    for i in range(1000):
			for (int i = 0; i < 1000; i++) {
				nowI = i;
				//
//			        if alf(p1) < alf(p2):
				if (alf(p1.getData()) < alf(p2.getData())) {
		            al_s = p1;
				    al_e = p2;
				    break;
				}
			//
//			        if alf(p1) > alf(p2) and alf(p2) < alf(p3):
				if (alf(p1.getData()) > alf(p2.getData()) && alf(p2.getData()) < alf(p3.getData())) {
		            al_s = p1;
		            al_e = p3;
		            break;
				}

		        p1 = p2;
		        p2 = p3;

//		        p1 = p2
//		        p2 = p3
		//
//		        p3 = nowp + (i + 3) * vectr * h
		        p3 = nowPMat.add(vectorMat.scalarMultiply((i + 3) * h));
		//

			}

//		    print("i：" + str(i))
			System.out.println("i：" + nowI);

//		    print("s：" + str(al_s))
			System.out.println("s：" + al_s);

			//		    print("e：" + str(al_e))
			System.out.println("e：" + al_e);

					//
//		    # 初期設定
//		    #al_s = 0.0
//		    #al_e = 10.0

			//
//		    # 終末の差
//		    end_sa = 0.00001
			double end_sa = 0.00001;
//		    for i in range(100):
			for (int i = 0; i < 100; i++) {
				//
//		        # 終末確認
//		        sa = np.sqrt(((al_e - al_s) ** 2).sum())
				double sa = al_e.subtract(al_s).getColumnVector(0).getNorm();
		//
//		        if sa < end_sa:
//		            break
				if(sa < end_sa) {
					break;
				}
		//
//		        #中間点1,2を計算
//		        al_a = al_s + (al_e - al_s) * (1-gold)
				RealMatrix al_a = al_s.add(al_e.subtract(al_s).scalarMultiply(1-gold));

//		        al_b = al_s + (al_e - al_s) * gold
				RealMatrix al_b = al_s.add(al_e.subtract(al_s).scalarMultiply(gold));
				//
//		        #中間点１を通る2次式の最小値
//		        min_hokan_a = min(al_s,alf(al_s),al_a,alf(al_a),al_e,alf(al_e))
		//
//		        #中間点２を通る2次式の最小値
//		        min_hokan_b = min(al_s,alf(al_s),al_b,alf(al_b),al_e,alf(al_e))
		//
//		        if alf(al_a) > alf(al_b):
//		            al_s = al_a
//		        else:
//		            al_e = al_b
			}


		//

		//


		}

		//print("最終点：" + str(nowp) + " 値:" + str(alf(nowp)))
		System.out.println("最終点：" + str(nowp) + " 値:" + str(alf(nowp)));


	}

//


	//# 対象数式
	private static double alf(double[][] al) {
//	    #return 20 - al[0] - (10/(al[0]-10.9))
//	    #return al[0,0]**2 + 2 * al[1,0]**2 - al[0,0]*al[1,0] + al[0,0] -2 * al[1,0]
	    return al[0][0]*al[0][0] + 2 * al[1][0]*al[1][0] - al[0][0]*al[1][0] + al[0][0] -2 * al[1][0];
	}

	private static String str(double[][] al) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < al.length; i++) {
			sb.append("[");
			double[] temp = al[i];
			for (int j = 0; i < temp.length; j++) {
				sb.append("[");
				sb.append(temp[j]);
				sb.append("]");
			}
			sb.append("]");
		}
		return sb.toString();
	}

	private static String str(double al) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(al);
		sb.append("]");
		return sb.toString();
	}


	private static double[][] delta(double[][]  x) {
		double[][] d = new double[2][1];
		d[0][0] = 2*x[0][0] - x[1][0] + 1;
		d[1][0] = 4*x[1][0] - x[0][0] - 2;

		return d;
	}

	//# 3点を通る2次数式の最小値
	//def min(al_s,f_s,al_m,f_m,al_e,f_e):
//	    return ((al_m**2 - al_e**2) * f_s + (al_e**2 - al_s**2)*f_m + (al_s**2 - al_m**2)*f_e)/
	//(2*((al_m - al_e) * f_s + (al_e - al_s) * f_m + (al_s - al_m) * f_e))
	private static RealMatrix min(RealMatrix al_s,double f_s,RealMatrix  al_m,double f_m,RealMatrix  al_e,double f_e) {

		RealMatrix result = null;

		RealMatrix child = MatUtil.matBroPow(al_m,2).subtract(MatUtil.matBroPow(al_e,2)).scalarMultiply(f_s)
				.add(MatUtil.matBroPow(al_e,2).subtract(MatUtil.matBroPow(al_s,2)).scalarMultiply(f_m))
				.add(MatUtil.matBroPow(al_s,2).subtract(MatUtil.matBroPow(al_m,2)).scalarMultiply(f_e));



		return null;
	}


	//def delta(x):
//  d = np.empty(x.shape)
//  d[0] = 2*x[0] - x[1] +1
//  d[1] = 4*x[1] - x[0] -2
//  return d
//


}



//# ニュートン法関連
//
//import numpy as np
//import numpy.linalg as LA
//import matplotlib.pyplot as plt
//
//# 3点を通る2次数式
//def N_s(alf,al_s,al_m,al_e):
//    return ((alf - al_m) * (alf - al_e))/((al_s- al_m) * (al_s - al_e))
//
//def N_m(alf,al_s,al_m,al_e):
//    return ((alf - al_e) * (alf - al_s))/((al_m- al_e) * (al_m - al_s))
//
//def N_e(alf,al_s,al_m,al_e):
//    return ((alf - al_s) * (alf - al_m))/((al_e- al_s) * (al_e - al_m))
//
//def f_q(alf,al_s,f_s,al_m,f_m,al_e,f_e):
//    return N_s(alf,al_s,al_m,al_e) * f_s + N_m(alf,al_s,al_m,al_e) * f_m + N_e(alf,al_s,al_m,al_e) * f_e
//
//# 3点を通る2次数式の最小値
//def min(al_s,f_s,al_m,f_m,al_e,f_e):
//    return ((al_m**2 - al_e**2) * f_s + (al_e**2 - al_s**2)*f_m + (al_s**2 - al_m**2)*f_e)/(2*((al_m - al_e) * f_s + (al_e - al_s) *
//
//                                                                                           f_m + (al_s - al_m) * f_e))
//# 対象数式
//def alf(al):
//    #return 20 - al[0] - (10/(al[0]-10.9))
//    #return al[0,0]**2 + 2 * al[1,0]**2 - al[0,0]*al[1,0] + al[0,0] -2 * al[1,0]
//    return al[0]**2 + 2 * al[1]**2 - al[0]*al[1] + al[0] -2 * al[1]
//
//def delta(x):
//    d = np.empty(x.shape)
//    d[0] = 2*x[0] - x[1] +1
//    d[1] = 4*x[1] - x[0] -2
//    return d
//
//gpx = []
//gpy = []
//
//
//I = np.zeros((2,2))
//I[0,0] = 1.0
//I[1,1] = 1.0
//
//def siki1(preH,y,s, beta):
//    return (preH @ y @ s.T + s @ y.T @ preH) / beta
//
//def siki2(beta, gamma, s):
//    return ((beta + gamma) / (beta ** 2))  * (s @ s.T)
//
//def bfgs(nowp,prep,preH):
//    s = nowp - prep
//    y = delta(nowp) - delta(prep)
//    beta = s.T @ y
//    gamma = y.T @ preH @ y
//    H = preH - siki1(preH, y, s, beta) + siki2(beta, gamma, s)
//    return H
//
//H = I
//
//#定数、黄金比
//gold = 0.618034
//h = 1
//
//nowp = np.zeros((2,1))
//nowp[0,0] = -10
//nowp[1,0] = 0
//
//for j in range(10):
//    print("現点：" + str(nowp))
//
//    gpx.append(nowp[0])
//    gpy.append(nowp[1])
//
//    w, v = LA.eig(H)
//    print("固有値:" + str(w))
//
//    vectr = - H @ delta(nowp)
//    # 正規化
//    vectr = vectr / np.sqrt((vectr ** 2).sum())
//
//    print("ベクトル：" + str(vectr))
//
//
//    p1 = nowp + 0 * vectr * h
//    p2 = nowp + 1 * vectr * h
//    p3 = nowp + 2 * vectr * h
//
//
//
//
//    for i in range(1000):
//
//        if alf(p1) < alf(p2):
//            al_s = p1
//            al_e = p2
//            break
//
//        if alf(p1) > alf(p2) and alf(p2) < alf(p3):
//            al_s = p1
//            al_e = p3
//            break
//
//        p1 = p2
//        p2 = p3
//
//        p3 = nowp + (i + 3) * vectr * h
//
//
//    print("i：" + str(i))
//    print("s：" + str(al_s))
//    print("e：" + str(al_e))
//
//    # 初期設定
//    #al_s = 0.0
//    #al_e = 10.0
//
//    # 終末の差
//    end_sa = 0.00001
//
//    for i in range(100):
//
//        # 終末確認
//        sa = np.sqrt(((al_e - al_s) ** 2).sum())
//
//        if sa < end_sa:
//            break
//
//        #中間点1,2を計算
//        al_a = al_s + (al_e - al_s) * (1-gold)
//        al_b = al_s + (al_e - al_s) * gold
//
//        #中間点１を通る2次式の最小値
//        min_hokan_a = min(al_s,alf(al_s),al_a,alf(al_a),al_e,alf(al_e))
//
//        #中間点２を通る2次式の最小値
//        min_hokan_b = min(al_s,alf(al_s),al_b,alf(al_b),al_e,alf(al_e))
//
//        if alf(al_a) > alf(al_b):
//            al_s = al_a
//        else:
//            al_e = al_b
//
//    #print(" 計算回数:" + str(i))
//    print("終点s：" + str(al_s) + " 値s:" + str(alf(al_s)) + " 計算回数:" + str(i))
//    print("終点e：" + str(al_e) + " 値e:" + str(alf(al_e)) + " 計算回数:" + str(i))
//
//
//    prep = nowp
//    nowp = (al_s + al_e) / 2
//
//
//    H =bfgs(nowp,prep,H)
//
//print("最終点：" + str(nowp) + " 値:" + str(alf(nowp)))
//
//x = np.linspace(-25, 25 ,3000)
//y = np.linspace(-25, 25 ,3000)
//xmesh, ymesh = np.meshgrid(x,y)
//print("shappe:" +str(xmesh.shape))
//
//gp = np.array([xmesh.ravel(),ymesh.ravel()])
//print("shappe:" +str(xmesh.shape))
//
//z = alf(gp).reshape(xmesh.shape)
//plt.contour(x,y,z,colors ="k",levels=[1,10,20,40,80])
//plt.scatter(nowp[0],nowp[1])
//plt.plot(gpx,gpy, color="r")
//
//plt.show()
//
