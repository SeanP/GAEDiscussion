package edu.utsa.cs.gaediscussion.resources;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

public abstract class AnswerDecorator {
	
	protected boolean dirty = false;

	public abstract void appendJson(JSONObject json);
	
	public boolean isDirty() {
		return dirty;
	}
}
