package cat.my.android.pillow.data.extra;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Time implements Comparable<Time>, Serializable {
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

    public Time(Calendar calendar){
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
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

    public int getTime(){
        return minute + hour*60;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Time)) return false;

        Time time = (Time) o;

        if (hour != time.hour) return false;
        if (minute != time.minute) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = minute;
        result = 31 * result + hour;
        return result;
    }

    @Override
    public int compareTo(Time another) {
        return getTime() - another.getTime();
    }
}
