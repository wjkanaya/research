package sim;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimRandom {
	static Logger logger = LogManager.getLogger(SimRandom.class);


	Random random;

	public SimRandom(int seed) {

		this.random = new Random(seed);
	}

	public double nextDouble() {
		double result = this.random.nextDouble();
	//	logger.debug("nextDouble():" + result);
		return result;
	}

	public double nextGaussian() {
		double result = this.random.nextGaussian();
	//	logger.debug("nextGaussian():" + result);
		return result;
	}

	public int nextInt(int value) {
			int result = this.random.nextInt(value);
	//		logger.debug("nextInt():" + result);
		return result;
	}



}
