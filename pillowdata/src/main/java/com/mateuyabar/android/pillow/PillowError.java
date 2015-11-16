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

package com.mateuyabar.android.pillow;

import com.mateuyabar.util.exceptions.BreakFastException;


@SuppressWarnings("serial")
public class PillowError extends Exception{

	public PillowError() {
		super();
	}

	public PillowError(String detailMessage, Throwable throwable) {
		super(detailMessage, getRealException(throwable));
	}

	public PillowError(String detailMessage) {
		super(detailMessage);
	}

	public PillowError(Throwable throwable) {
		super(getRealException(throwable));
	}
	
	private static Throwable getRealException(Throwable throwable){
		//We don't want to encapsulate exceptions in more than one lever of PillowError
		if(throwable instanceof PillowError){
			new BreakFastException("CHECK THIS");
		}
		return throwable;
	}
	
}
