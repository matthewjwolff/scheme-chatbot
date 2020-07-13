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
	
	public String message;
	public Object target;
	public Object user;
	public String permission;

	@Override
	public void beginListening() {
		// do nothing
	}
	
	@Override
	public void sendMessage(String message, Object target) {
		this.message = message;
		this.target= target;
	}

	@Override
	public boolean userHasPermission(Object user, String permission) {
		this.user = user;
		this.permission = permission;
		return true;
	}
	
	

}
