package edu.utsa.cs.gaediscussion.resources.test;

import java.util.List;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import static org.junit.Assert.*;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import edu.utsa.cs.gaediscussion.resources.AnswerDecorator;
import edu.utsa.cs.gaediscussion.resources.AnswerFactory;
import edu.utsa.cs.gaediscussion.resources.AnswerMultipleChoice;

public class AnswerTest {

	private void testFactory() {
		try {
			JSONObject json = new JSONObject();
			JSONObject fr = new JSONObject();
			fr.put("response", "foobar");
			json.put("freeResponse", fr);
			JSONObject mc = new JSONObject();
			JSONArray choices = new JSONArray();
			choices.put(0);
			choices.put(2);
			mc.put("choices", choices);
			json.put("multipleChoice", mc);
			List<AnswerDecorator> decorators = AnswerFactory
					.generateDecorators(json);
			assertEquals(2, decorators.size());
			JSONObject result = new JSONObject();
			decorators.get(0).appendJson(result);
			decorators.get(1).appendJson(result);
			assertEquals("{\"freeResponse\":{\"response\":\"foobar\"},\"multipleChoice\":{\"choices\":[0,2]}}",
					result.toString());
		} catch (JSONException e) {
			fail();
		}
	}
	
	private void testAMC() {
		try {
			JSONObject json = new JSONObject();
			JSONObject mc = new JSONObject();
			JSONArray choices = new JSONArray();
			choices.put(0);
			choices.put(2);
			mc.put("choices", choices);
			AnswerMultipleChoice amc = new AnswerMultipleChoice(mc);
			amc.appendJson(json);
			assertEquals("{\"multipleChoice\":{\"choices\":[0,2]}}", json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void basicTest1() {
		testFactory();
	}
	
	@Test
	public void basicTestAMC() {
		testAMC();
	}
}
