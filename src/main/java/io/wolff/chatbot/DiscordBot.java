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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import io.wolff.chatbot.pojo.ComplexMessage;
import io.wolff.chatbot.pojo.Embeddable;
import io.wolff.chatbot.pojo.ImageEmbed;

public class DiscordBot extends AbstractBot {
	
	private static final int DISCORD_MESSAGE_MAX_LENGTH = 2000;
	private static final String COMMAND_PREFIX = "!";
	
	private DiscordClient client;
	
	public DiscordBot(String key) {
		super();
		client = new DiscordClientBuilder(key).build();
	}

	@Override
	void beginListening() {
		client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(event -> {
			Optional<String> content = event.getMessage().getContent();
			if(content.isPresent() && content.get().startsWith(COMMAND_PREFIX)) {
				// grab some vars to bind them in scheme
				Message inMessage = event.getMessage();
				MessageChannel channel = inMessage.getChannel().block();
				channel.createMessage(message -> {
					try {
						//Scheme.registerEnvironment();
						// TODO timeout
						String command = content.get().substring(COMMAND_PREFIX.length());
						Map<String, Object> env = new HashMap<>();
						env.put("_event", event);
						Object result = this.execScheme(command, env);
						// this should be the only typecheck performed
						if(result instanceof ComplexMessage) {
							handleComplexMessage(message, (ComplexMessage) result);
						} else {
							String response = Utils.maxLength(String.valueOf(result), DISCORD_MESSAGE_MAX_LENGTH);
							message.setContent(response.isEmpty() ? "OK" : response);
						}
					} catch (Throwable e) {
						message.setContent(e.getMessage());
						message.setEmbed(embed -> {
							embed.setTitle("Stack Trace");
							StringWriter s = new StringWriter();
							PrintWriter pw = new PrintWriter(s);
							e.printStackTrace(pw);
							embed.setDescription(Utils.maxLength(s.toString(), DISCORD_MESSAGE_MAX_LENGTH));
						});
					}
				}).block();
			}
			
		});

		client.login().block();
	}
	
	private void handleComplexMessage(MessageCreateSpec message, ComplexMessage result) {
		message.setContent(Utils.maxLength(String.valueOf(result.content), DISCORD_MESSAGE_MAX_LENGTH));
		if(result.embeds!=null && !result.embeds.isEmpty()) {
			message.setEmbed(embed -> {
				for(Embeddable embeddable : result.embeds ) {
					if(embeddable instanceof ImageEmbed) {
						embed.setImage(((ImageEmbed)embeddable).url);
					}
				}
			});
		}
		
	}

}
