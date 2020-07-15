/*******************************************************************************
 * This file is part of SchemeChatbot.
 *
 * SchemeChatbot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SchemeChatbot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package io.wolff.chatbot;

import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
	public static boolean isEmpty(String s) {
		if(s == null || s.length()==0) {
			return true;
		}
		return false;
	}
	
	public static String maxLength(String s, int maxLength) {
		if(s.length() > maxLength) {
			return s.substring(0, maxLength);
		}
		return s;
	}
	
	public static boolean isUrl(String url) {
		if(!(url instanceof String)) {
			throw new IllegalArgumentException("Not a string");
		}
		try {
			new URL((String) url);
		} catch(MalformedURLException e) {
			return false;
		}
		return true;
	}
}
