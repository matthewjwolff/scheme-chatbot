package io.wolff.helpers;

import gnu.mapping.Procedure1;
import io.wolff.chatbot.AbstractBot;

public class SendMessage extends Procedure1 {
	
	private final AbstractBot bot;
	
	

	public SendMessage(AbstractBot bot) {
		super("send-message");
		this.bot = bot;
	}



	@Override
	public Object apply1(Object arg0) throws Throwable {
		if(!(arg0 instanceof String)) {
			throw new IllegalArgumentException("Not a string.");
		}
		this.bot.sendMessage((String) arg0);
		return null;
	}
	
}
