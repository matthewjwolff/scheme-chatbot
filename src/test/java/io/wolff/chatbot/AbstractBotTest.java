package io.wolff.chatbot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AbstractBotTest {

	@Test
	void testExecScheme() throws Throwable {
		TestBot test = new TestBot();
		
		// assert basic functionality
		Object result = test.execScheme("(define x 5) x");
		assertEquals("5", result.toString());
		
		// assert state is preserved
		result = test.execScheme("x");
		assertEquals("5", result.toString());
	}

}
