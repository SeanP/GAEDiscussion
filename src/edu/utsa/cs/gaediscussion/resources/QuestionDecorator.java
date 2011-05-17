package edu.utsa.cs.gaediscussion.resources;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

@PersistenceCapable
public abstract class QuestionDecorator extends Question {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	protected boolean required;
//	@Persistent
//	protected Key concrete;
	
	public QuestionDecorator(QuestionConcrete concrete, boolean required) {
//		this.concrete= concrete.getKey();
		this.required = required;
	}

	public QuestionDecorator(JSONObject jsonObject) throws JSONException {
		this.required = jsonObject.getBoolean("required");
		
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
		
	public Key getKey() {
		return key;
	}
	
	protected JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("required", required);
		return json;
	}
	
	public abstract void appendJson(JSONObject decorations) throws JSONException;
	
	public abstract void validateAnswer(List<AnswerDecorator> decorations) throws InvalidAnswerException;

}
