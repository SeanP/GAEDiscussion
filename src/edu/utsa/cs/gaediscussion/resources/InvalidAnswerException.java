package edu.utsa.cs.gaediscussion.resources;

import com.google.appengine.repackaged.org.json.JSONException;

public class InvalidAnswerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1289903861024700473L;

	public InvalidAnswerException(String string) {
		super(string);
	}

}
