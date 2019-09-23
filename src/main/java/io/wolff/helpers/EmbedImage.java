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

import gnu.mapping.Procedure1;
import io.wolff.chatbot.pojo.ImageEmbed;

public class EmbedImage extends Procedure1 {
	
	public EmbedImage() {
		super("embed-image");
	}

	@Override
	public Object apply1(Object arg0) throws Throwable {
		ImageEmbed retval = new ImageEmbed();
		retval.url = String.valueOf(arg0);
		return retval;
	}

}
