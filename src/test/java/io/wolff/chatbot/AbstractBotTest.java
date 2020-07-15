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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

class AbstractBotTest {
	
	@Test
	void testOnMessage() throws Throwable {
		TestBot test = new TestBot();
		// register an event listener that calls a method on the test bot
		test.execScheme("(register-message-listener (lambda (message sender) (.messagecallback _bot)))", null, null);
		test.onMessage(null, null, new HashMap<>());
		assertTrue(test.onMessageCalled);
	}
	
	@Test
	void testSendTo() throws Throwable {
		TestBot test = new TestBot();
		Object sender = new Object();
		Object message = "This is the message";
		test.execScheme("(send-to _message _sender)", sender, message);
		assertEquals(sender, test.target);
		assertEquals(message, test.message);
	}
	
	@Test
	void testUserHasPerm() throws Throwable {
		TestBot test = new TestBot();
		Object user = new Object();
		test.execScheme("(user-has-perm? _sender \"testperm\" )", user, null);
		assertEquals("testperm", test.permission);
		assertEquals(user, test.user);
	}
	
	@Test
	void testIsUrl() throws Throwable {
		TestBot test = new TestBot();
		assertEquals(Boolean.FALSE, test.execScheme("(is-url? \"notaurl\")", null, null));
		assertEquals(Boolean.TRUE, test.execScheme("(is-url? \"http://google.com/\")", null, null));
	}

}
