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

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import gnu.mapping.Environment;
import gnu.mapping.Procedure1;
import gnu.mapping.SimpleSymbol;

public class SenderHasPerm extends Procedure1 {
	
	public SenderHasPerm() {
		super("sender-has-perm?");
	}

	@Override
	public Object apply1(Object arg1) throws Throwable {
		if(!(arg1 instanceof String)) {
			throw new IllegalArgumentException("Invalid usage of sender-has-perm?. Use (sender-has-perm? perm:String)");
		}
		String perm = (String) arg1;
		Environment e = Environment.getCurrent();
		Object o = e.get(new SimpleSymbol("_user"));
		MessageCreateEvent event = (MessageCreateEvent) o;
		Member member = event.getMember().get();
		return member.getRoles().any(role -> role.getName().equals(perm)).block();
	}

}
