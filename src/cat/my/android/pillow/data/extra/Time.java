package cat.my.android.pillow.data.extra;

import java.util.Date;

public class Time {
	int minute;
	int hour;
	
	public Time(){
		Date now = new Date();
		this.minute= now.getMinutes();
		this.hour = now.getHours();
	}
	
	public Time(int hour, int minute) {
		super();
		this.minute = minute;
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}
	
	
	
}
