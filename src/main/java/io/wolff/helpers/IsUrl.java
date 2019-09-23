package io.wolff.helpers;

import java.net.MalformedURLException;
import java.net.URL;

import gnu.mapping.Procedure1;

public class IsUrl extends Procedure1 {
	
	public IsUrl() {
		super("is-url");
	}

	@Override
	public Object apply1(Object arg0) throws Throwable {
		if(!(arg0 instanceof String)) {
			throw new IllegalArgumentException("Not a string");
		}
		try {
			new URL((String) arg0);
		} catch(MalformedURLException e) {
			return false;
		}
		return true;
	}

}
