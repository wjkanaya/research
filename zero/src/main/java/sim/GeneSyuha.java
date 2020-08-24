package sim;

public class GeneSyuha {
	public int nowNinzu(int jiki, int syuki,int maxnumi,double isouRan){

		double result = (maxnumi  - 1) *((Math.sin(2*Math.PI*(jiki/syuki)+ 2*Math.PI * isouRan)) + 1)/2;
		return (int) Math.round(result);
	}

}
