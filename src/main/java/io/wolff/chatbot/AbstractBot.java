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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public abstract class AbstractBot {
	
	protected final Scheme2 scheme;
	
	/**
	 * Construct a bot with a brand new scheme environment
	 */
	protected AbstractBot( ) {
		this.scheme = new Scheme2();
		initializeInterpreter();
	}
	
	/**
	 * Construct a bot with the provided environment
	 * @param inputStream an input stream ready to deserialize a JScheme object
	 * @throws ClassNotFoundException thrown by inputStream.readObject()
	 * @throws IOException thrown by inputStream.readObject()
	 */
	public AbstractBot(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
		this.scheme = (Scheme2) inputStream.readObject();
		// the serialized environment will obviously not have a reference to this, so we need to set that up
		scheme.setGlobalValue("_bot", this);
	}
	
	public void serializeInterpreter(ObjectOutputStream destination) throws IOException {
		// unset this variable so that object serialization will not find a reference to this and attempt to serialize this
		scheme.setGlobalValue("_bot", null);
		destination.writeObject(scheme);
		// set it back so we can continue using the bot
		scheme.setGlobalValue("_bot", this);
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
	 * Alert the bot that a message was sent. If any on-message handlers are registered, they will be run
	 * @param sender sender of the message
	 * @param message the text of the message
	 * @param implementationVars variable map to pass implementation-specific variables
	 */
	protected final void onMessage(Object sender, String message, Map<String, Object> implementationVars) {
		// TODO: implement transients with proper environment handling (let ((key val)...) (expr))
		// TODO: do we actually need implementation vars?
		for(Map.Entry<String, Object> var : implementationVars.entrySet()) {
			this.scheme.setGlobalValue(var.getKey(), var.getValue());
		}

		this.scheme.getGlobalSchemeProcedure("on-message").apply(new Object[] {sender, message});
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
		this.scheme.setGlobalValue("_sender", sender);
		this.scheme.setGlobalValue("_message", message);

		Object result = this.scheme.eval(command);
		
		return result;
	}
	
//	private Object evaluateFully(String expression) {
//		Object result = null;
//		while(result!=InputPort.EOF) {
//			Object it = this.scheme.read(s)
//		}
//		
//	}
	
	private void initializeInterpreter() {
		try {
			// define global functions from scheme
			String globalScript = new String(Files.readAllBytes(Paths.get(getClass().getResource("/global.scm").toURI())));
			scheme.eval(globalScript);
			// this method is just easier in java
			// TODO: figure out how to make java callbacks
			//scheme.defineFunction(new IsUrl());
			
			// finally, bind this to _bot variable
			scheme.setGlobalValue("_bot", this);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
