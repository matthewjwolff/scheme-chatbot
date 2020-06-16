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
import java.util.Map;

import io.wolff.helpers.IsUrl;
import io.wolff.helpers.SendTo;
import io.wolff.helpers.SenderHasPerm;
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
	
	protected final Object execScheme(String command) throws Throwable {
		return this.execScheme(command, null);
	}
	
	/**
	 * Execute the provided scheme code within the provided environment
	 * @param command the scheme code to execute
	 * @param transientEnvironment a set of key-value pairs that will be bound in an enclosing environment. TODO: is this environment transient
	 * @return the result of the command. A bot should return the result as a message; if the result is null, no message should be sent.
	 * @throws Throwable any exception the command might throw. The bot should use an implementation-specific format to display the error to the sender
	 */
	protected final Object execScheme(String command, Map<String, Object> transientEnvironment) throws Throwable {
		// TODO: implement transients with proper environment handling (let ((key val)...) (expr))
		if(transientEnvironment!=null) {
			for(Map.Entry<String, Object> entry : transientEnvironment.entrySet()) {
				scheme.define(entry.getKey(), entry.getValue());
			}
		}
		Object result = scheme.eval(command);
		if(transientEnvironment!=null) {
			for(String key : transientEnvironment.keySet()) {
				scheme.define(key, null);
			}
		}
		
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
		scheme.defineFunction(new SenderHasPerm());
	}
}
