package edu.utsa.cs.gaediscussion.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.listener.LoadCallback;
import javax.jdo.listener.StoreCallback;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

/*
 * TODO
 * 
 * Add event listeners for JDO.  When we load, create a decorator-like
 * structure. When we write back to the datastore, collapse it down to JSON.
 */

@PersistenceCapable
public class QuestionConcrete extends Question implements LoadCallback, StoreCallback{

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;

	@Persistent
	private String question;
	@Persistent
	private boolean isActive;
	@Persistent
	private boolean answersPublic;
	@Persistent
	private boolean isCurrentQuestion;
	@Persistent
	private Date created;

	@Persistent(serialized = "true")
	private List<QuestionDecorationWrapper> decorators;
	
	@Persistent(mappedBy = "question"/*,extensions = @Extension(vendorName="datanucleus", key="list-ordering", value=" asc")*/)
	@Element(dependent = "true")
	private List<Answer> answers;

	public QuestionConcrete(String question, boolean answersPublic) {
		this.question = question;
		this.answersPublic = answersPublic;
		this.isActive = false;
		this.isCurrentQuestion = false;
		this.created = new Date();
		this.decorators = new ArrayList<QuestionDecorationWrapper>();
	}

	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("question", question);
		json.put("current", isCurrentQuestion);
		json.put("active", isActive);
		json.put("answerspublic", answersPublic);
		json.put("question_id", key);
		JSONObject jsonDecorations = new JSONObject();
		for (QuestionDecorationWrapper qdw : decorators) {
			qdw.getQuestionDecorator().appendJson(jsonDecorations);
		}
		json.put("decorations", jsonDecorations);
		return json;
	}
	
	public void addAnswer(Answer ans) throws InvalidAnswerException {
		StringBuilder errors = new StringBuilder();
		for (QuestionDecorationWrapper qdw : decorators) {
			try {
				qdw.getQuestionDecorator().validateAnswer(ans.getDecorations());
			} catch (InvalidAnswerException e) {
				errors.append(e.getMessage()).append("\n");
			}
		}
		if (errors.length() != 0) {
			throw new InvalidAnswerException(errors.toString());
		}
//		ans.setQuestion(this);
		getAnswers().add(ans);
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isAnswersPublic() {
		return answersPublic;
	}

	public void setAnswersPublic(boolean answersPublic) {
		this.answersPublic = answersPublic;
	}

	public boolean isCurrentQuestion() {
		return isCurrentQuestion;
	}

	public void setCurrentQuestion(boolean isCurrentQuestion) {
		this.isCurrentQuestion = isCurrentQuestion;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void addDecorator(QuestionDecorator qd) {
		decorators.add(new QuestionDecorationWrapper(qd.getClass()
				.getCanonicalName(), qd.getKey()));
	}

	public List<QuestionDecorationWrapper> getDecorators() {
		return decorators;
	}

	public void setDecorators(List<QuestionDecorationWrapper> decorators) {
		this.decorators = decorators;
	}

	@Override
	public void jdoPreStore() {
		// TODO Auto-generated method stub
		System.err.println("preStore");
	}

	@Override
	public void jdoPostLoad() {
		// TODO Auto-generated method stub
		System.err.println("postLoad");
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

}
