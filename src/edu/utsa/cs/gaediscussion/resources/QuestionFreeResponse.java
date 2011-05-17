package edu.utsa.cs.gaediscussion.resources;

import java.util.List;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.PersistenceCapable;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

@PersistenceCapable
@Inheritance(customStrategy = "complete-table")
public class QuestionFreeResponse extends QuestionDecorator {

	public QuestionFreeResponse(QuestionConcrete concrete) {
		this(concrete, true);
	}

	public QuestionFreeResponse(QuestionConcrete concrete, boolean required) {
		super(concrete, required);
	}

	public QuestionFreeResponse(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
	}

	@Override
	public void appendJson(JSONObject decorations) throws JSONException {
		JSONObject fr = super.toJson();
		decorations.put("freeResponse", fr);
	}
	
	@Override
	public void validateAnswer(List<AnswerDecorator> decorators) throws InvalidAnswerException {
		AnswerFreeResponse freeResponse = null;
		for (AnswerDecorator ad : decorators) {
			if (ad instanceof AnswerFreeResponse) {
				freeResponse = (AnswerFreeResponse) ad;
				break;
			}
		}
		if (freeResponse == null) {
			if (required == true) {
				throw new InvalidAnswerException("No free response submitted.");
			} else {
				return;
			}
		}
		
		if (freeResponse.getResponse().length() == 0) {
			throw new InvalidAnswerException("Empty free response submitted.");
		}
		
		
		return;
	}

}
