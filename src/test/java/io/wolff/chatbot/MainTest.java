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
package io.wolff.chatbot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import kawa.standard.Scheme;

class MainTest {

	@Test
	void testConvertResult() throws Throwable {
		Object o = new Scheme().eval("#t");
		System.out.println(o);
		Object o2 = Utils.convertResult(o);
		System.out.println(o2);
	}

}
