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

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class AbstractBotTest {

	@Test
	void testExecScheme() throws Throwable {
		TestBot test = new TestBot();
		
		Map<String, Object> env = new HashMap<>();
		
		// test send message
		String message = "clarence";
		Object target = new Object();
		env.put("message", message);
		env.put("target", target);
		
		test.execScheme("(send-to message target)", env);
		
		// assert that the bot method was called
		assert(test.message==message);
		assert(test.target==target);
		
		String perm = "test";
		Object user = new Object();
		env = new HashMap<>();
		env.put("perm", perm);
		env.put("user", user);
		
		test.execScheme("(user-has-perm? user perm)", env);
		
		assert(test.permission==perm);
		assert(test.user==user);
		
	}

}
