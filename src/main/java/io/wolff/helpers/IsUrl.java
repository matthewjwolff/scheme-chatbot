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
package io.wolff.helpers;

import java.net.MalformedURLException;
import java.net.URL;

import gnu.mapping.Procedure1;

public class IsUrl extends Procedure1 {
	
	public IsUrl() {
		super("is-url?");
	}

	@Override
	public Object apply1(Object arg0) throws Throwable {
		if(!(arg0 instanceof String)) {
			throw new IllegalArgumentException("Not a string");
		}
		try {
			new URL((String) arg0);
		} catch(MalformedURLException e) {
			return false;
		}
		return true;
	}

}
