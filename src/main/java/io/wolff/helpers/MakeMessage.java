/*******************************************************************************
 * Copyright (C) 2019 mjw
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package io.wolff.helpers;

import gnu.mapping.ProcedureN;
import io.wolff.chatbot.pojo.ComplexMessage;
import io.wolff.chatbot.pojo.Embeddable;

public class MakeMessage extends ProcedureN {
	
	public MakeMessage() {
		super("make-message");
	}
	
	@Override
	public Object applyN(Object[] arg0) throws Throwable {
		ComplexMessage message = new ComplexMessage();
		StringBuilder builder = new StringBuilder();
		for(Object arg : arg0) {
			if(arg instanceof Embeddable) {
				message.embeds.add((Embeddable) arg);
			} else {
				builder.append(String.valueOf(arg));
				builder.append("\n");
			}
		}
		message.content=builder.toString();
		return message;
	}

}
