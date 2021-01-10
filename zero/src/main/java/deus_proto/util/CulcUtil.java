package deus_proto.util;

public class CulcUtil {

	// AIC計算
	public static double culcAIC(double lnL, int pramNum) {
		return  -2 * (lnL - pramNum);
	}

}
