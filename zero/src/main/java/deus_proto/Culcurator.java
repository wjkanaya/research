package deus_proto;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

public interface Culcurator {

	public void clear();
	public void getData(List<Integer> searchYearsList,List<String> targetCodeList);
	public void makeData();
	// public void clearCache();
	public void setCovariatesValue(RealMatrix lastBetaMat);
	public void setCovariatesValueBeta0(RealMatrix lastBetaMat);


	public int getBetaSize();
	public void setBetaSize(int betaSize);
	public double L(double[][] beta);
	public RealMatrix delta(RealMatrix betaMat);

}
