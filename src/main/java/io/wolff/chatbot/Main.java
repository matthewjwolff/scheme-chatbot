/*******************************************************************************
 * Copyright (C) 2019 mjw
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package io.wolff.chatbot;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.MessageCreateSpec;
import io.wolff.chatbot.pojo.ComplexMessage;
import io.wolff.chatbot.pojo.Embeddable;
import io.wolff.chatbot.pojo.ImageEmbed;
import io.wolff.helpers.EmbedImage;
import io.wolff.helpers.IsUrl;
import io.wolff.helpers.MakeMessage;
import kawa.standard.Scheme;

public class Main {

	private static final int DISCORD_MESSAGE_MAX_LENGTH = 2000;
	private static final String COMMAND_PREFIX = "!";

	public static void main(String[] args) {
		if(args.length != 1) {
			throw new IllegalArgumentException("Please provide key only.");
		}
		DiscordClient client = new DiscordClientBuilder(args[0]).build();
		Scheme scheme = new Scheme();
		initializeInterpreter(scheme);
		client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(event -> {
			Optional<String> content = event.getMessage().getContent();
			if(content.isPresent() && content.get().startsWith(COMMAND_PREFIX)) {
				event.getMessage().getChannel().block().createMessage(message -> {
					try {
						//Scheme.registerEnvironment();
						// TODO timeout
						String command = content.get().substring(COMMAND_PREFIX.length());
						Object result = scheme.eval(command);
						// the command was successful, log it
						log(command);
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

	private static void log(String command) {
		try {
			Files.write(Paths.get("log.scm"), (command+"\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void initializeInterpreter(Scheme scheme) {
		// apply helper functions
		scheme.defineFunction(new MakeMessage());
		scheme.defineFunction(new EmbedImage());
		scheme.defineFunction(new IsUrl());
		try {
			// define global functions from scheme
			String globalScript = Files.readAllLines(Paths.get("global.scm"))
					.stream()
					.reduce((str1, str2) -> str1 + "\n" + str2)
					.get();
			scheme.eval(globalScript);
			// init from history
 			if(Files.exists(Paths.get("log.scm"))) {
				String historyScript = Files.readAllLines(Paths.get("log.scm"))
						.stream()
						.reduce((str1, str2) -> str1 + "\n" + str2)
						.get();
				scheme.eval(historyScript);
 			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void handleComplexMessage(MessageCreateSpec message, ComplexMessage result) {
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
