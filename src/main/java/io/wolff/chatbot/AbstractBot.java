package io.wolff.chatbot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import io.wolff.helpers.EmbedImage;
import io.wolff.helpers.IsUrl;
import io.wolff.helpers.MakeMessage;
import io.wolff.helpers.SenderHasPerm;
import kawa.standard.Scheme;

public abstract class AbstractBot {
	
	protected final Scheme scheme;
	
	protected AbstractBot( ) {
		this.scheme = new Scheme();
		initializeInterpreter();
	}
	
	abstract void beginListening();
	
	protected Object execScheme(String command, Map<String, Object> transientEnvironment) throws Throwable {
		for(Map.Entry<String, Object> entry : transientEnvironment.entrySet()) {
			scheme.define(entry.getKey(), entry.getValue());
		}
		Object result = scheme.eval(command);
		for(String key : transientEnvironment.keySet()) {
			scheme.define(key, null);
		}

		// the command was successful, log it
		log(command);
		
		return result;
	}
	
	private void initializeInterpreter() {
		// apply helper functions
		// TODO: how to handle implementation-dependent functions
		scheme.defineFunction(new MakeMessage());
		scheme.defineFunction(new EmbedImage());
		scheme.defineFunction(new IsUrl());
		scheme.defineFunction(new SenderHasPerm());
		try {
			// define global functions from scheme
			String globalScript = Files.readAllLines(Paths.get("global.scm"))
					.stream()
					.reduce((str1, str2) -> str1 + "\n" + str2)
					.get();
			scheme.eval(globalScript);
			// init from history
 			if(Files.exists(Paths.get("log.scm"))) {
				String historyScript = Files.readAllLines(Paths.get("log.scm"))
						.stream()
						.reduce((str1, str2) -> str1 + "\n" + str2)
						.get();
				scheme.eval(historyScript);
 			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	private void log(String command) {
		try {
			Files.write(Paths.get("log.scm"), (command+"\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
