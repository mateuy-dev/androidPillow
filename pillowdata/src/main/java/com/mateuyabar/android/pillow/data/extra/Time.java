/*
 * Copyright (c) Mateu Yabar Valles (http://mateuyabar.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.mateuyabar.android.pillow.data.extra;

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
