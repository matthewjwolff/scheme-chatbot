/*******************************************************************************
 * Copyright (C) 2019 mjw
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package io.wolff.chatbot;

import gnu.lists.FString;
import gnu.math.DFloNum;
import gnu.math.IntNum;

public class Utils {
	public static String maxLength(String s, int maxLength) {
		if(s.length() > maxLength) {
			return s.substring(0, maxLength);
		}
		return s;
	}
	
	/**
	 * Convert the output of a scheme eval into a standard Java object
	 * @param o the output of Interpreter.eval
	 * @return a standard-library Java object, null if no specific return value
	 */
	public static Object convertResult(Object o) {
		if(o==null) {
			throw new IllegalArgumentException("Must be nonnull");
		}
		if(o instanceof FString) {
			return o.toString();
		}
		if(o instanceof IntNum) {
			return ((IntNum) o).ival;
		}
		if(o instanceof DFloNum) {
			return ((DFloNum)o).doubleValue();
		}
		return o;
	}
}
