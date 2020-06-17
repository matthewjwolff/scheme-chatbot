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

import gnu.mapping.Procedure2;
import io.wolff.chatbot.AbstractBot;

public class UserHasPerm extends Procedure2 {
	
	private final AbstractBot bot;
	
	public UserHasPerm(AbstractBot bot) {
		super("user-has-perm?");
		this.bot = bot;
	}

	//TODO: refactor to delegate permission check to specific subclass
	@Override
	public Object apply2(Object arg0, Object arg1) throws Throwable {
		if(!(arg1 instanceof String)) {
			throw new IllegalArgumentException("Invalid usage of sender-has-perm?. Use (sender-has-perm? perm:String)");
		}
		String perm = (String) arg1;
		return this.bot.userHasPermission(perm, arg0);
	}

}
