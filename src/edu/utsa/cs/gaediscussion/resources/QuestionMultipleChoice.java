package edu.utsa.cs.gaediscussion.resources;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

@PersistenceCapable
@Inheritance(customStrategy = "complete-table")
public class QuestionMultipleChoice extends QuestionDecorator {
	
	@Persistent
	private List<String> choices;
	private static final String choicesJsonLabel = "choices";
	@Persistent
	private int minChoices;
	@Persistent
	private int maxChoices;
	
	@Deprecated
	public QuestionMultipleChoice(QuestionConcrete concrete, boolean required, int minChoices, int maxChoices, List<String> choices) {
		super(concrete, required);
		this.choices = choices;
		this.minChoices = minChoices;
		this.maxChoices = maxChoices;
	}
	public QuestionMultipleChoice(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		choices = new ArrayList<String>();
		JSONArray jsonChoices = jsonObject.getJSONArray(choicesJsonLabel);
		for (int i=0; i < jsonChoices.length(); i++) {
			choices.add(jsonChoices.getString(i));
		}
		
		/*
		 * TODO Add min and max choices when supported.  Just do 1 and 1 until then.
		 */
		minChoices = 1;
		maxChoices = 1;
	}
	@Override
	public void appendJson(JSONObject decorations) throws JSONException {
		JSONObject mc = toJson();
		decorations.put("multipleChoice", mc);
	}
	
	@Override
	protected JSONObject toJson() throws JSONException {
		JSONObject mc = super.toJson();
		JSONArray jsonChoices = new JSONArray(choices);
		mc.put(choicesJsonLabel, jsonChoices);
		return mc;
	}
	
	@Override
	public void validateAnswer(List<AnswerDecorator> decorators) throws InvalidAnswerException {
		AnswerMultipleChoice multipleChoice = null;
		for (AnswerDecorator ad : decorators) {
			if (ad instanceof AnswerMultipleChoice) {
				multipleChoice = (AnswerMultipleChoice) ad;
				break;
			}
		}
		if (multipleChoice == null) {
			if (required == true) {
				throw new InvalidAnswerException("No multiple choice submitted.");
			} else {
				return;
			}
		}
		
		List<Integer> ansChoices = multipleChoice.getChoices();
		
		int ansChoicesSize = ansChoices.size();
		if (ansChoicesSize < minChoices) {
			throw new InvalidAnswerException("Insufficient selections in multiple choice.");
		} else if (ansChoicesSize > maxChoices) {
			throw new InvalidAnswerException("Too many selections in multiple choice.");
		}

		int choicesSize = choices.size();
		for (Integer i : ansChoices) {
			if (i >= choicesSize || i < 0) {
				throw new InvalidAnswerException("Selected answer does not correspond to a valid choice.");
			}
		}
		
		
		return;
	}

}
