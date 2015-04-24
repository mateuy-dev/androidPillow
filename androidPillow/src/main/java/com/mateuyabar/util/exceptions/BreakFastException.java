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

package com.mateuyabar.util.exceptions;

public class BreakFastException extends RuntimeException {

	private static final long serialVersionUID = -5956881676211995975L;

	public BreakFastException() {
		super();
	}

	public BreakFastException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public BreakFastException(String detailMessage) {
		super(detailMessage);
	}

	public BreakFastException(Throwable throwable) {
		super(throwable);
	}

}
