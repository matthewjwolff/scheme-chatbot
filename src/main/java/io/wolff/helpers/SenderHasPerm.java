package io.wolff.helpers;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import gnu.mapping.Environment;
import gnu.mapping.Procedure1;
import gnu.mapping.SimpleSymbol;

public class SenderHasPerm extends Procedure1 {
	
	public SenderHasPerm() {
		super("sender-has-perm");
	}

	@Override
	public Object apply1(Object arg1) throws Throwable {
		if(!(arg1 instanceof String)) {
			throw new IllegalArgumentException("Invalid usage of sender-has-perm. Use (sender-has-perm perm:String)");
		}
		String perm = (String) arg1;
		Environment e = Environment.getCurrent();
		Object o = e.get(new SimpleSymbol("_user"));
		MessageCreateEvent event = (MessageCreateEvent) o;
		Member member = event.getMember().get();
		return member.getRoles().any(role -> role.getName().equals(perm)).block();
	}

}
