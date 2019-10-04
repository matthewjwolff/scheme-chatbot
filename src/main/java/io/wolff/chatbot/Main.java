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

public class Main {
	public static void main(String[] args) {
		String mode = null;
		String key = null;
		for(int i=0; i<args.length; i++) {
			if(args[i].equals("-m")) {
				mode = args[++i];
			} else if(args[i].equals("-k")) {
				key = args[++i];
			}
		}
		if(mode==null || key==null) {
			throw new IllegalArgumentException("Usage: -m Botmode -k key");
		}
		AbstractBot bot;
		switch(mode) {
		case "Discord":
			bot = new DiscordBot(key);
			break;
		case "GroupMe":
		case "Groupme":
			bot = new GroupmeBot(key);
			break;
			default: throw new UnsupportedOperationException("Do not support this bot type");
		}
		
		bot.beginListening();
	}
}
