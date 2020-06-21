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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import gnu.mapping.Environment;
import gnu.mapping.SimpleSymbol;
import io.wolff.chatbot.AbstractBot;
import io.wolff.chatbot.Utils;

public class DiscordBot extends AbstractBot {
	
	private static final int DISCORD_MESSAGE_MAX_LENGTH = 2000;
	private static final String COMMAND_PREFIX = "!";
	
	private DiscordClient client;
	
	public DiscordBot(String key) {
		super();
		client = new DiscordClientBuilder(key).build();
	}

	@Override
	public void beginListening() {
		client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(event -> {
			Optional<String> content = event.getMessage().getContent();
			if(content.isPresent() && content.get().startsWith(COMMAND_PREFIX)) {
				Message inMessage = event.getMessage();
				MessageChannel channel = inMessage.getChannel().block();
				String command = content.get().substring(COMMAND_PREFIX.length());
				try {
					Object result = this.execScheme(command, event.getMember().orElse(null), inMessage);
					if(result!=null) {
						String response = Utils.maxLength(String.valueOf(result), DISCORD_MESSAGE_MAX_LENGTH);
						channel.createMessage(message -> {
							message.setContent(response);
						}).block();
						
					}
				} catch (Throwable e) {
					channel.createMessage(message -> {
						message.setContent(e.getMessage());
						message.setEmbed(embed -> {
							embed.setTitle("Stack Trace");
							StringWriter s = new StringWriter();
							PrintWriter pw = new PrintWriter(s);
							e.printStackTrace(pw);
							embed.setDescription(Utils.maxLength(s.toString(), DISCORD_MESSAGE_MAX_LENGTH));
						});
					}).block();
				}
			}
			
		});

		client.login().block();
	}

	@Override
	public void sendMessage(String message, Object target) {
		// TODO must figure out what channel the message was sent in so that we can send the message to that channel
	}

	@Override
	public boolean userHasPermission(Object user, String perm) {
		// TODO: refactor this to not pull a symbol out of the environment
		Environment e = Environment.getCurrent();
		Object o = e.get(new SimpleSymbol("_user"));
		MessageCreateEvent event = (MessageCreateEvent) o;
		Member member = event.getMember().get();
		return member.getRoles().any(role -> role.getName().equals(perm)).block();
	}

}
