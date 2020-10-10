package sim;

import java.util.Calendar;
import java.util.Date;

public class SimCalendar {

	int kikan;
	Date nowDate;

	// 計算期間月数
	public SimCalendar(int kikan) {
		nowDate = new Date();
		this.kikan = kikan;

	}


	public Date getJikiDate(int jiki) {

		//kikan
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(nowDate);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int date = 1;

		calendar.set(year, month, date, 0, 0, 0); // 今月の月初
		calendar.add(Calendar.MONTH, - kikan + jiki); // 計算期間分マイナス

		return calendar.getTime();
	}



}
