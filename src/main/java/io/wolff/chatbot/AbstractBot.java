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

import java.nio.file.Files;
import java.nio.file.Paths;

import io.wolff.helpers.IsUrl;
import kawa.standard.Scheme;

public abstract class AbstractBot {
	
	protected final Scheme scheme;
	
	protected AbstractBot( ) {
		this.scheme = new Scheme();
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
