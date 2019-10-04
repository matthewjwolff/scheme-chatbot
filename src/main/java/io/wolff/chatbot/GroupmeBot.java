package io.wolff.chatbot;

import io.wolff.groupme.GroupMeBot;
import io.wolff.groupme.model.BotMessage;
import io.wolff.groupme.model.Message;
import reactor.core.publisher.Flux;

public class GroupmeBot extends AbstractBot {
	
	private final GroupMeBot bot;
	
	public GroupmeBot(String key) {
		super();
		this.bot = new GroupMeBot(key);
	}

	@Override
	void beginListening() {
		//the callback url is handled with groupme
		Flux<Message> messages = bot.listen();
		while(true) {
			Message message = messages.blockFirst();
			if(message.text.startsWith("!")) {
				String response;
				try {
					Object result = this.execScheme(message.text.substring(1));
					if(result==null || Utils.isEmpty(result.toString())) {
						response = "OK";
					} else {
						response = result.toString();
					}
					
				} catch (Throwable e) {
					response = e.getMessage();
				}
				BotMessage responseMessage = new BotMessage();
				responseMessage.text = response;
				bot.postMessage(responseMessage).subscribe();
			}
		}
	}

}
