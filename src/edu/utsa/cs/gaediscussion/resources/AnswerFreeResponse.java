package edu.utsa.cs.gaediscussion.resources;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * @author spivek
 *
 */
public class AnswerFreeResponse extends AnswerDecorator {
	
	private String response;
	

	/**
	 * @param json The FreeResponse portion of the answer json
	 * @throws JSONException
	 */
	public AnswerFreeResponse(JSONObject json) throws JSONException {
		response = json.getString("response");
	}

	public void appendJson(JSONObject decorations) {
		try {
			decorations.put("freeResponse", toJson());
		} catch (JSONException e) {
			// Can't happen.
			e.printStackTrace();
		}
	}

	private JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("response", response);
		} catch (JSONException e) {
			// Can't happen.
			e.printStackTrace();
		}
		return json;
	}

	public String getResponse() {
		return response;
	}
}
