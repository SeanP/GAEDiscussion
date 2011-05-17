package edu.utsa.cs.gaediscussion.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

public class AnswerFactory {

	private AnswerFactory() {
	}

	/**
	 * Creates a new answer and validates it against the QuestionConcrete. Does
	 * <strong>not</strong> add the answer to the question or the DataStore.
	 * 
	 * @param qc
	 *            Question
	 * @param userId
	 *            Some unique identifier. Can be anything from an IP address to
	 *            a student ID number. Use this to enforce one answer per
	 *            submitter.
	 * @param json
	 *            The JSON as submitted.
	 * @return
	 * @throws InvalidAnswerException
	 *             Thrown if the answer was not valid (e.g., submitter didn't
	 *             answer a required part of the question). We will fail as late
	 *             as possible, so that all problems are found. The message will
	 *             contain a user-agent-displayable error message.
	 * @throws JSONException
	 *             Thrown if there was a structural problem with the JSON. This
	 *             will be thrown on the first occurance.
	 */
	public static Answer newAnswer(QuestionConcrete qc, String userId,
			JSONObject json) throws InvalidAnswerException, JSONException {
		Answer answer = new Answer(userId);
		answer.setDecorations(generateDecorators(json
				.getJSONObject("decorations")));
		return answer;
	}

	public static List<AnswerDecorator> generateDecorators(
			JSONObject decorations) throws JSONException {
		Iterator<String> itr = decorations.keys();
		ArrayList<AnswerDecorator> decorators = new ArrayList<AnswerDecorator>();
		while (itr.hasNext()) {
			String k = itr.next();
			AnswerDecorator ad = null;
			if (k.equals("freeResponse")) {
				ad = new AnswerFreeResponse(decorations.getJSONObject(k));
			} else if (k.equals("multipleChoice")) {
				ad = new AnswerMultipleChoice(decorations.getJSONObject(k));
			} else {
				continue;
			}
			decorators.add(ad);
		}
		return decorators;
	}

}
