package io.wolff.chatbot;

import java.io.Serializable;
import java.io.StringReader;

import jscheme.SchemeProcedure;
import jsint.Evaluator;
import jsint.InputPort;
import jsint.Scheme;
import jsint.Symbol;
import jsint.U;

/**
 * Custom interface to JScheme with the eval semantics I want
 * 
 * @author mjw
 *
 */
public class Scheme2 implements Serializable {

	/**
	 * 1. Initial implementation
	 */
	private static final long serialVersionUID = 1L;
	private final Evaluator evaluator = new Evaluator();

	/**
	 * Fully evaluates and returns the last result
	 * 
	 * @param expression the expression to evaluate
	 * @return The result of the last expression (possibly null), null if no
	 *         expressions were evaluated
	 */
	public Object eval(String expression) {
		InputPort port = new InputPort(new StringReader(expression));
		Object result = null;
		// it really likes to push/pop evaluators. i don't know why, but i guess lets do
		// it
		Scheme.pushEvaluator(evaluator);
		try {
			// wacky for loop but it works
			for (Object it = port.read(); it != InputPort.EOF; it=port.read()) {

				result = evaluator.evalToplevel(it, evaluator.interactionEnvironment);
			}
			return result;
		} finally {
			Scheme.popEvaluator();
		}
	}

	// below code is copied verbatim from jscheme.JScheme

	/** Set the value of the global variable named s to v. **/
	public void setGlobalValue(String s, Object v) {
		enter();
		try {
			Symbol.intern(s).setGlobalValue(v);
		} finally {
			exit();
		}
	}

	/**
	 * Enters this instance's evaluator.
	 */
	private void enter() {
		Scheme.pushEvaluator((Evaluator) evaluator);
	}

	/**
	 * Exits an evaluator. This must be called for every call to <code>enter</code>.
	 */
	private void exit() {
		Scheme.popEvaluator();
	}

	/** Returns the global procedure named s. **/
	public SchemeProcedure getGlobalSchemeProcedure(String s) {
		enter();
		try {
			return U.toProc(getGlobalValue(s));
		} finally {
			exit();
		}
	}

	/** Get the value of the global variable named s. **/
	public Object getGlobalValue(String s) {
		enter();
		try {
			return Symbol.intern(s).getGlobalValue();
		} finally {
			exit();
		}
	}

}
