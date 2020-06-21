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
package io.wolff.chatbot;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.wolff.helpers.IsUrl;
import kawa.standard.Scheme;

public abstract class AbstractBot {
	
	protected final Scheme scheme;
	private final List<String> onMessageExpressions;
	
	protected AbstractBot( ) {
		this.scheme = new Scheme();
		// TODO: make persistent
		this.onMessageExpressions = new LinkedList<>();
		// bind a reference to the bot in scheme. This will be how we define functionality in java
		// TODO: would it be better to use a delegate?
		this.scheme.define("_bot", this);
		initializeInterpreter();
	}
	
	public abstract void beginListening();
	
	/**
	 * Sends the provided message to the bot's chat
	 * @param message the message to send
	 * @param target implementation-defined object that defines the intended recipient of the message
	 */
	public abstract void sendMessage(String message, Object target);
	
	/**
	 * Check if the user has this permission
	 * @param user an implementation-defined reference to a user
	 * @param permission the name of the permission
	 * @return true if the user has the permission
	 */
	public abstract boolean userHasPermission(Object user, String permission);
	
	/**
	 * Register a scheme expression that should be run on every message
	 * @param expression the expression to be run
	 */
	public final void registerOnMessage(String expression) {
		this.onMessageExpressions.add(expression);
	}
	
	/**
	 * Alert the bot that a message was sent. If any on-message handlers are registered, they will be run
	 * @param sender sender of the message
	 * @param message the text of the message
	 * @param implementationVars variable map to pass implementation-specific variables
	 */
	protected final void onMessage(Object sender, String message, Map<String, Object> implementationVars) {
		for(String ex : this.onMessageExpressions) {
			// TODO: environments
			this.scheme.define("_sender", sender);
			this.scheme.define("_message", message);
			for(Map.Entry<String, Object> var : implementationVars.entrySet()) {
				this.scheme.define(var.getKey(), var.getValue());
			}
			try {
				this.scheme.eval(ex);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Execute the provided scheme code within the provided environment
	 * @param command the scheme code to execute
	 * @param sender implementation-specific object for the sender of the message. Bound to _sender in scheme
	 * @param message implementation-specific object for the message itself. Bound to _message in scheme
	 * @return the result of the command. A bot should return the result as a message; if the result is null, no message should be sent.
	 * @throws Throwable any exception the command might throw. The bot should use an implementation-specific format to display the error to the sender
	 */
	protected final Object execScheme(String command, Object sender, Object message) throws Throwable {
		// TODO: implement transients with proper environment handling (let ((key val)...) (expr))
		scheme.define("_sender", sender);
		scheme.define("_message", message);

		Object result = scheme.eval(command);
		
		return result;
	}
	
	private void initializeInterpreter() {
		try {
			// define global functions from scheme
			String globalScript = new String(Files.readAllBytes(Paths.get(getClass().getResource("/global.scm").toURI())));
			scheme.eval(globalScript);
			// this method is just easier in java
			scheme.defineFunction(new IsUrl());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
