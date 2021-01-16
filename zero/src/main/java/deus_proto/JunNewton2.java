package deus_proto;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import deus_proto.util.CulcUtil;
import sim.Util;

public class JunNewton2 {

	static Logger logger = LogManager.getLogger(JunNewton2.class);


	// <<退職圧要因一覧情報>>
	public static void main(String[] args) throws Exception {

		Util.startTransaction();
		try {
			JunNewton2 jn = new JunNewton2();
			jn.execute();

		} finally {
			Util.endTransaction();;
		}
	}

	private void execute() {

		GeneSurvDataEstimate2 est = new GeneSurvDataEstimate2();

		est.getData();
		est.makeData();


	//	double[][] H = I;
		RealMatrix hMat = MatrixUtils.createRealIdentityMatrix(est.getBetaSize());
		//#定数、黄金比
		double gold = 0.618034;
		int h = 1;
		//
		//nowp = np.zeros((2,1))
		double[][] nowp;
		RealMatrix nowPMat = MatrixUtils.createRealMatrix(est.getBetaSize(), 1);

		//for j in range(10):
		for (int j = 0; j < 10; j++) {

//		    print("現点：" + str(nowp))
			logger.debug("現点：" + nowPMat);
			//			//
//			    w, v = LA.eig(H)
			EigenDecomposition hMatEd = new EigenDecomposition(hMat);

			//			    print("固有値:" + str(w))
			double[] eigens = hMatEd.getRealEigenvalues();
			for(int i=0; i<eigens.length; i++) {
				logger.debug("固有値: " + str(eigens[i]));
				//	logger.debug("固有ベクトル: " + ed.getEigenvector(i));
			}

			//
//			    vectr = - H @ delta(nowp)
			RealMatrix vectorMat =
					hMat.multiply(est.delta(nowPMat))
					.scalarMultiply(-1);


//			    # 正規化
//			    vectr = vectr / np.sqrt((vectr ** 2).sum())
			vectorMat =
					vectorMat.scalarMultiply(1 / vectorMat.getColumnVector(0).getNorm());

			//
//			    print("ベクトル：" + str(vectr))
			logger.debug("ベクトル：" + vectorMat);
			//

//			    p1 = nowp + 0 * vectr * h

			RealMatrix p1 = nowPMat.add(vectorMat.scalarMultiply(0*h));
			double L1 = est.L(p1.getData());

//		    p2 = nowp + 1 * vectr * h
			RealMatrix p2 = nowPMat.add(vectorMat.scalarMultiply(1*h));
            double L2 = est.L(p2.getData());

//		    p3 = nowp + 2 * vectr * h
			RealMatrix p3 = nowPMat.add(vectorMat.scalarMultiply(2*h));
			double L3 = est.L(p3.getData());


			RealMatrix al_s = null;
			RealMatrix al_e = null;
			double alsL = 0;
			double aleL = 0;

			int nowI = 0;
//		    for i in range(1000):
			for (int i = 0; i < 1000; i++) {
				nowI = i;
				//
//			        if alf(p1) < alf(p2):
				if (L1 < L2) {
		            al_s = p1;
		            alsL = L1;
				    al_e = p2;
				    aleL = L2;
				    break;
				}
			//
//			        if alf(p1) > alf(p2) and alf(p2) < alf(p3):
				if (L1 > L2 && L2 < L3) {
		            al_s = p1;
		            alsL = L1;
		            al_e = p3;
		            aleL = L3;
		            break;
				}

		        p1 = p2;
		        L1 = L2;
		        p2 = p3;
		        L2 = L3;

//		        p1 = p2
//		        p2 = p3
		//
//		        p3 = nowp + (i + 3) * vectr * h
		        h *= 2;

		        p3 = p3.add(vectorMat.scalarMultiply(h));
		        L3 = est.L(p3.getData());
		        //

			}

//		    print("i：" + str(i))
			logger.debug("i：" + nowI);

//		    print("s：" + str(al_s))
			logger.debug("s：" + al_s);

			//		    print("e：" + str(al_e))
			logger.debug("e：" + al_e);

					//
//		    # 初期設定
//		    #al_s = 0.0
//		    #al_e = 10.0

			//
//		    # 終末の差
//		    end_sa = 0.00001
			double end_sa = 0.00001;
//		    for i in range(100):
			nowI = 0;
			for (int i = 0; i < 1000; i++) {
				nowI = i;
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
				double alaL = est.L(al_a.getData());

//		        al_b = al_s + (al_e - al_s) * gold
				RealMatrix al_b = al_s.add(al_e.subtract(al_s).scalarMultiply(gold));
				double albL = est.L(al_b.getData());

				//
//		        #中間点１を通る2次式の最小値
//		        min_hokan_a = min(al_s,alf(al_s),al_a,alf(al_a),al_e,alf(al_e))
				RealMatrix min_hokan_a =
						min(al_s, alsL, al_a, alaL, al_e, aleL);
//				min(al_s,alf(al_s.getData()),al_a,alf(al_a.getData()),al_e,alf(al_e.getData()));
		//
//		        #中間点２を通る2次式の最小値
//		        min_hokan_b = min(al_s,alf(al_s),al_b,alf(al_b),al_e,alf(al_e))

				RealMatrix min_hokan_b =
						min(al_s, alsL, al_b, albL, al_e, aleL);
//				min(al_s,alf(al_s.getData()),al_b,alf(al_b.getData()),al_e,alf(al_e.getData()));


				//
//		        if alf(al_a) > alf(al_b):
//		            al_s = al_a
//		        else:
//		            al_e = al_b
				if (alaL > albL) {
					al_s = al_a;
				} else {
					al_e = al_b;
				}
			}

//		    #print(" 計算回数:" + str(i))
//		    print("終点s：" + str(al_s) + " 値s:" + str(alf(al_s)) + " 計算回数:" + str(i))
			logger.debug("終点s：" + al_s + " 値s:" +alsL + " 計算回数:" + nowI);


//		    print("終点e：" + str(al_e) + " 値e:" + str(alf(al_e)) + " 計算回数:" + str(i))
			logger.debug("終点e：" + al_e + " 値e:" + aleL + " 計算回数:" + nowI);
		//
		//
//		    prep = nowp
			RealMatrix prepMat = nowPMat.copy();

//		    nowp = (al_s + al_e) / 2
			nowPMat = al_s.add(al_e).scalarMultiply(0.5);


			double sa = prepMat.subtract(nowPMat).getColumnVector(0).getNorm();
	//
//	        if sa < end_sa:
//	            break
			if(sa < end_sa) {
				break;
			}

//		    H =bfgs(nowp,prep,H)

			hMat = bfgs(est, nowPMat,prepMat, hMat);

		}
		nowp = nowPMat.getData();
		//print("最終点：" + str(nowp) + " 値:" + str(alf(nowp)))
//		logger.debug("最終点：" + str(nowp) + " 値:" + str(alf(nowp)));
		logger.debug("最終点：" + nowPMat + " 対数尤度値:"  + -est.L(nowPMat.getData())+
				" AIC:" + CulcUtil.culcAIC( -est.L(nowPMat.getData()), est.getBetaSize())) ;

		est.setCovariatesValue(nowPMat);

	}



//

//def bfgs(nowp,prep,preH):
//  s = nowp - prep
//  y = delta(nowp) - delta(prep)
//  beta = s.T @ y
//  gamma = y.T @ preH @ y
//  H = preH - siki1(preH, y, s, beta) + siki2(beta, gamma, s)
//  return H
//

	private static RealMatrix bfgs(GeneSurvDataEstimate2 est, RealMatrix nowPMat,RealMatrix prepMat,RealMatrix preHMat) {
		RealMatrix s = nowPMat.subtract(prepMat);
		RealMatrix y = est.delta(nowPMat).subtract(est.delta(prepMat));

		logger.debug( "s:" + s  );
		logger.debug( "y:" + y  );


		double beta = s.transpose().multiply(y).getEntry(0, 0); // 1×1の行列になっているはず

		double gamma = y.transpose().multiply(preHMat).multiply(y)
				.getEntry(0, 0);// 1×1の行列になっているはず
		logger.debug( "preHMat:" + preHMat  );
		logger.debug( "siki1:" +   siki1(preHMat, y, s, beta)  );

		logger.debug( "siki2:" +   siki2(beta, gamma, s)  )
		;
		RealMatrix H = preHMat.subtract(siki1(preHMat, y, s, beta)).add(siki2(beta, gamma, s));

		return H;
	}

	//def siki1(preH,y,s, beta):
//  return (preH @ y @ s.T + s @ y.T @ preH) / beta

	private static RealMatrix siki1(RealMatrix preHMat,RealMatrix y,RealMatrix s,double beta) {

		return preHMat.multiply(y).multiply(s.transpose()).add(s.multiply(y.transpose())
				.multiply(preHMat)).scalarMultiply(1/beta);
	}

	//
//def siki2(beta, gamma, s):
//  return ((beta + gamma) / (beta ** 2))  * (s @ s.T)
	private static RealMatrix siki2(double beta, double  gamma, RealMatrix s) {
		return s.multiply(s.transpose()).scalarMultiply((beta + gamma)/ Math.pow(beta, 2));

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




	//# 3点を通る2次数式の最小値
	//def min(al_s,f_s,al_m,f_m,al_e,f_e):
//	    return ((al_m**2 - al_e**2) * f_s + (al_e**2 - al_s**2)*f_m + (al_s**2 - al_m**2)*f_e)/
	//(2*((al_m - al_e) * f_s + (al_e - al_s) * f_m + (al_s - al_m) * f_e))
	private static RealMatrix min(RealMatrix al_s,double f_s,RealMatrix  al_m,double f_m,RealMatrix  al_e,double f_e) {

		RealMatrix result = null;

		RealMatrix child = MatUtil.matBroPow(al_m,2).subtract(MatUtil.matBroPow(al_e,2)).scalarMultiply(f_s)
				.add(MatUtil.matBroPow(al_e,2).subtract(MatUtil.matBroPow(al_s,2)).scalarMultiply(f_m))
				.add(MatUtil.matBroPow(al_s,2).subtract(MatUtil.matBroPow(al_m,2)).scalarMultiply(f_e));


		RealMatrix mother = al_m.subtract(al_e).scalarMultiply(f_s).add(al_e.subtract(al_s).scalarMultiply(f_m)).
				add(al_s.subtract(al_m).scalarMultiply(f_e)).scalarMultiply(2);

		result = MatUtil.matBroDiv(child, mother);

		return result;
	}


}

