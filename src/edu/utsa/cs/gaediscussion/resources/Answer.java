package edu.utsa.cs.gaediscussion.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.listener.LoadCallback;
import javax.jdo.listener.StoreCallback;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

@PersistenceCapable
public class Answer implements StoreCallback {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Date lastModified;

	@Persistent
	private String userId;
	
	@Persistent
	private QuestionConcrete question;

	/*
	 * No, this isn't a real decorator. A real decorator wouldn't actually fit
	 * our needs -- we need to keep the reference to the concrete class in order
	 * to persist it.
	 */
	@NotPersistent
	private List<AnswerDecorator> decorations;

	@NotPersistent
	private boolean dirty = false;

	@Persistent
	private String jsonDecorationString;

	public Answer(String uid) {
		this.userId = uid;
		dirty = true;
	}

	/**
	 * Mash the decorator-ish structure back down to JSON.
	 */
	@Override
	public void jdoPreStore() {
		if (dirty) {
			JSONObject jsonDec = new JSONObject();
			for (AnswerDecorator ad : decorations) {
				ad.appendJson(jsonDec);
			}
			jsonDecorationString = jsonDec.toString();
			lastModified = new Date();
		}
	}

	/**
	 * Build the decorators for ease of manipulation. This is called the first
	 * time access to the decorators is requested.
	 */
	private void generateDecorators() {
		if (decorations == null) {
			try {
				decorations = AnswerFactory.generateDecorators(new JSONObject(
						jsonDecorationString));
			} catch (JSONException e) {
				// Not possible.
				e.printStackTrace();
			}
		}

	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<AnswerDecorator> getDecorations() {
		dirty = true;
		generateDecorators();
		return decorations;
	}

	public void setDecorations(List<AnswerDecorator> list) {
		dirty = true;
		this.decorations = list;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setQuestion(QuestionConcrete question) {
		this.question = question;
	}

	public QuestionConcrete getQuestion() {
		return question;
	}

}
