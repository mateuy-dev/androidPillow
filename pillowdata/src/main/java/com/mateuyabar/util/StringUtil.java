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

package com.mateuyabar.util;

import java.text.DecimalFormat;

public class StringUtil {
	private static final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

	public static boolean isBlanck(String s){
		return s==null || s.length()==0;
	}

	public static String trim(String string){
		return string.replace((char) 160, ' ').trim();
	}

	public static String deleteSpaces(String string){
		return trim(string).replace(" ", "");
	}

	public static String getShortPrice(Double price){
		return decimalFormat.format(price) +"€";
	}

	public static String getFullPrice(Double price){
		return decimalFormat.format(price) +"€";
	}

	public static String capitalizeFirst(String string){
		return string.substring(0,1).toUpperCase() + string.substring(1);
	}

	public static String toString(Integer integer, String def){
		if(integer==null)
			return def;
		return integer.toString();
	}

	public static Integer toInteger(String string){
		try {
			return Integer.valueOf(string);
		}catch(Exception e){
			return null;
		}
	}
}
