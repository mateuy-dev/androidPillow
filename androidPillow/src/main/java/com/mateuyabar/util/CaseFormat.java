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

public class CaseFormat {
	
	public String cammelCaseToSnakeCase(String cammelCase){
		String regex = "([a-z])([A-Z]+)";
	    String replacement = "$1_$2";
	    return cammelCase.replaceAll(regex, replacement).toLowerCase();
	}
	
	public String firstLetterToLowerCase(String string){
		String firstLetter = string.substring(0,1).toLowerCase();
		String restLetters = string.substring(1);
		return firstLetter + restLetters;
	}

}
