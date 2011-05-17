package edu.utsa.cs.gaediscussion.resources;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

public class AnswerMultipleChoice extends AnswerDecorator {

	private List<Integer> choices;

	public AnswerMultipleChoice(JSONObject json) throws JSONException {
		JSONArray array = json.getJSONArray("choices");
		choices = new ArrayList<Integer>();
		for (int i=0; i<array.length(); i++) {
			choices.add(array.getInt(i));
		}
	}
	
	public List<Integer> getChoices() {
		return choices;
	}

	public void appendJson(JSONObject decorations) {
		try {
			decorations.put("multipleChoice", toJson());
		} catch (JSONException e) {
			// Can't happen.
			e.printStackTrace();
		}
	}

	private JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("choices", new JSONArray(choices));
		} catch (JSONException e) {
			// Can't happen.
			e.printStackTrace();
		}
		return json;
	}

}
