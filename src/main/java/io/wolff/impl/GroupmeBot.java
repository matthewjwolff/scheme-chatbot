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
 * along with SchemeChatbot.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package io.wolff.impl;

import io.wolff.chatbot.AbstractBot;
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
	public void beginListening() {
		//the callback url is handled with groupme
		Flux<Message> messages = bot.listen();
		while(true) {
			Message message = messages.blockFirst();
			if(message.text.startsWith("!")) {
				String response;
				try {
					Object result = this.execScheme(message.text.substring(1), message.sender_id, message);
					if(result==null) {
						return;
					}
					response = result.toString();
				} catch (Throwable e) {
					response = e.getMessage();
				}
				BotMessage responseMessage = new BotMessage();
				responseMessage.text = response;
				bot.postMessage(responseMessage).subscribe();
			}
		}
	}

	@Override
	public void sendMessage(String message, Object target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean userHasPermission(Object user, String permission) {
		// TODO Auto-generated method stub
		return false;
	}

}
