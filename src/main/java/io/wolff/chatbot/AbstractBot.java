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
import io.wolff.helpers.SendTo;
import io.wolff.helpers.UserHasPerm;
import kawa.standard.Scheme;

public abstract class AbstractBot {
	
	protected final Scheme scheme;
	
	protected AbstractBot( ) {
		this.scheme = new Scheme();
		defineGlobalFunctions();
		initializeInterpreter();
	}
	
	abstract void beginListening();
	
	/**
	 * Callback to define implementation-specific symbols
	 * Subclasses can override. The default implementation is a no-op
	 */
	void defineLocals(Scheme interpreter) {}
	
	/**
	 * Sends the provided message to the bot's chat
	 * @param message the message to send
	 * @param target implementation-defined object that defines the intended recipient of the message
	 */
	public abstract void sendMessage(String message, Object target);
	
	/**
	 * Check if the user has this permission
	 * @param permission the name of the permission
	 * @param user an implementation-defined reference to a user
	 * @return true if the user has the permission
	 */
	public abstract boolean userHasPermission(String permission, Object user);
	
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
	
	protected void initializeInterpreter() {
		try {
			// define global functions from scheme
			String globalScript = Files.readAllLines(Paths.get("global.scm"))
					.stream()
					.reduce((str1, str2) -> str1 + "\n" + str2)
					.get();
			scheme.eval(globalScript);
			this.defineLocals(this.scheme);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	protected void defineGlobalFunctions() {
		// apply helper functions
		scheme.defineFunction(new SendTo(this));
		scheme.defineFunction(new IsUrl());
		scheme.defineFunction(new UserHasPerm(this));
	}
}
