package io.wolff.helpers;

import gnu.mapping.Procedure2;
import io.wolff.chatbot.AbstractBot;

public class SendTo extends Procedure2 {
	
	private final AbstractBot bot;

	public SendTo(AbstractBot bot) {
		super("send-to");
		this.bot = bot;
	}

	@Override
	public Object apply2(Object message, Object target) throws Throwable {
		if(!(message instanceof String)) {
			throw new IllegalArgumentException("Not a string.");
		}
		this.bot.sendMessage((String) message, target);
		return null;
	}
	
}
