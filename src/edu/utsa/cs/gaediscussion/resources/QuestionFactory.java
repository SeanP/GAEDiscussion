package edu.utsa.cs.gaediscussion.resources;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import edu.utsa.cs.gaediscussion.util.PMF;

public final class QuestionFactory {

	private QuestionFactory() {
	}

	public static QuestionConcrete newQuestion(JSONObject json) {
		QuestionConcrete q = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			String question;
			boolean answersPublic = false;

			question = json.getString("question");
			if (json.has("answerspublic")) {
				answersPublic = json.getBoolean("answerspublic");
			}
			q = new QuestionConcrete(question, answersPublic);
			q = pm.makePersistent(q);
			q = pm.detachCopy(q);
			
			JSONObject decorations = json.getJSONObject("decorations");
			
			Iterator<String> itr = decorations.keys();
			while (itr.hasNext()) {
				String k = itr.next();
				QuestionDecorator qd;
				if (k.equals("freeResponse")) {
					qd = new QuestionFreeResponse(decorations.getJSONObject(k));
				} else if (k.equals("multipleChoice")){
					qd = new QuestionMultipleChoice(decorations.getJSONObject(k));
				} else {
					continue;
				}
				qd = pm.makePersistent(qd);
				q.addDecorator(qd);
			}
			
			q = pm.makePersistent(q);
			q = pm.detachCopy(q);
			
		} catch (JSONException e) {
			q = null;
			e.printStackTrace();
		} finally {
			pm.close();
		}

		return q;
	}
}
