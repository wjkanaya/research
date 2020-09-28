package sim;

import java.util.Calendar;
import java.util.Date;

public class SimCalendar {

	Calendar calendar;

	// 計算期間月数
	public SimCalendar(int manths) {
		//kikan
		Calendar calendar = Calendar.getInstance();

		// 今日の日付
		Date nowDate = new Date();

		calendar.setTime(nowDate);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int date = 1;

		calendar.set(year, month, date, 0, 0, 0); // 今月の月初
		calendar.add(Calendar.MONTH, - manths); // 計算期間分マイナス
		this.calendar = calendar;
	}

	
	public Date getJikiDate(int jiki) {
		Calendar clone = (Calendar)calendar.clone();
		clone.add(Calendar.MONTH, jiki); // 時期分だけずらす
		return clone.getTime();
	}

}
