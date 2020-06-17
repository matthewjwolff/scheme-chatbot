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

public class TestBot extends AbstractBot {

	@Override
	void beginListening() {
		// do nothing
	}

	@Override
	protected void initializeInterpreter() {
		// do nothing
	}
	
	@Override
	public void sendMessage(String message, Object target) {
		// do nothing
	}

	@Override
	public boolean userHasPermission(String permission, Object user) {
		return true;
	}
	
	

}
