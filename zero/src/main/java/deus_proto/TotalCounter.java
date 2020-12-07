package deus_proto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TotalCounter {

	List<DDouble> list = new ArrayList<DDouble>();

	public void clear() {
		list.clear();
	}

	public void set(double d) {
		list.add(new DDouble(d));
	}

	public double getTotal() {
		Collections.sort(list);
		double result = 0;

		for (DDouble d :list) {
			result += d.getValue();
		}
		return result;
	}

}
