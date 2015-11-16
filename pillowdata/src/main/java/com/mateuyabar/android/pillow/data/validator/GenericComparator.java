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

package com.mateuyabar.android.pillow.data.validator;

import com.mateuyabar.util.exceptions.UnimplementedException;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class GenericComparator implements Comparator<Object>{

		@Override
		public int compare(Object o1, Object o2) {
			if (o1 instanceof Integer){
				Integer i1 = (Integer) o1;
				Integer i2 = (Integer) o2;
				return i1-i2;
			}
			if(o1 instanceof Date){
				Date date1 = (Date) o1;
				Date date2 = (Date) o2;
				long time1 = date1.getTime();
				long time2 = date2.getTime();
				return time1 < time2 ? -1 : (time1 == time2 ? 0 : 1);
			}
			if(o1 instanceof Calendar){
				Calendar cal1 = (Calendar) o1;
				Calendar cal2 = (Calendar) o2;
				return cal1.compareTo(cal2);
			}
			throw new UnimplementedException();
		}
	}