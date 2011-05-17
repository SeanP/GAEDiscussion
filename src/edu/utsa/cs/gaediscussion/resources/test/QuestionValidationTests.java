package edu.utsa.cs.gaediscussion.resources.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.utsa.cs.gaediscussion.resources.Answer;
import edu.utsa.cs.gaediscussion.resources.AnswerFactory;
import edu.utsa.cs.gaediscussion.resources.InvalidAnswerException;
import edu.utsa.cs.gaediscussion.resources.QuestionConcrete;
import edu.utsa.cs.gaediscussion.resources.QuestionFactory;

public class QuestionValidationTests {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}
	
	private void testQuestionFreeResponse() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"freeResponse\":{\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			JSONObject ajson = new JSONObject();
			JSONObject adecor = new JSONObject();
			JSONObject fr = new JSONObject();
			fr.put("response", "foobar");
			adecor.put("freeResponse", fr);
			ajson.put("decorations", adecor);
			Answer ans = AnswerFactory.newAnswer(null, "1", ajson);
			q.addAnswer(ans);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void testQuestionFreeResponseNoResponse() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"freeResponse\":{\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			JSONObject ajson = new JSONObject();
			JSONObject adecor = new JSONObject();
			JSONObject fr = new JSONObject();
//			fr.put("response", "foobar");
//			adecor.put("freeResponse", fr);
			ajson.put("decorations", adecor);
			Answer ans = AnswerFactory.newAnswer(null, "1", ajson);
			q.addAnswer(ans);
		} catch (InvalidAnswerException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void testQuestionFreeResponseEmptyResponse() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"freeResponse\":{\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			JSONObject ajson = new JSONObject();
			JSONObject adecor = new JSONObject();
			JSONObject fr = new JSONObject();
			fr.put("response", "");
			adecor.put("freeResponse", fr);
			ajson.put("decorations", adecor);
			Answer ans = AnswerFactory.newAnswer(null, "1", ajson);
			q.addAnswer(ans);
		} catch (InvalidAnswerException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void testQuestionMultipleChoice() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"multipleChoice\":{\"choices\":[\"Foo\",\"bar\",\"baz\"],\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			
			JSONObject ajson = new JSONObject();
			JSONObject adecor = new JSONObject();
			JSONObject mc = new JSONObject();
			JSONArray choices = new JSONArray();
			choices.put(1);
			mc.put("choices", choices);
			adecor.put("multipleChoice", mc);
			ajson.put("decorations", adecor);
			Answer ans = AnswerFactory.newAnswer(null, "1", ajson);
			q.addAnswer(ans);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void testQuestionMultipleChoiceTooManyAnswers() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"multipleChoice\":{\"choices\":[\"Foo\",\"bar\",\"baz\"],\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			
			JSONObject ajson = new JSONObject();
			JSONObject adecor = new JSONObject();
			JSONObject mc = new JSONObject();
			JSONArray choices = new JSONArray();
			choices.put(1);
			choices.put(2);
			mc.put("choices", choices);
			adecor.put("multipleChoice", mc);
			ajson.put("decorations", adecor);
			Answer ans = AnswerFactory.newAnswer(null, "1", ajson);
			q.addAnswer(ans);
		} catch (InvalidAnswerException e) {
			assertEquals("Too many selections in multiple choice.\n", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	private void testQuestionMultipleChoiceInsufficientAnswers() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"multipleChoice\":{\"choices\":[\"Foo\",\"bar\",\"baz\"],\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			
			JSONObject ajson = new JSONObject();
			JSONObject adecor = new JSONObject();
			JSONObject mc = new JSONObject();
			JSONArray choices = new JSONArray();
			mc.put("choices", choices);
			adecor.put("multipleChoice", mc);
			ajson.put("decorations", adecor);
			Answer ans = AnswerFactory.newAnswer(null, "1", ajson);
			q.addAnswer(ans);
		} catch (InvalidAnswerException e) {
			assertEquals("Insufficient selections in multiple choice.\n", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void testQuestionMultipleChoiceNoMC() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"multipleChoice\":{\"choices\":[\"Foo\",\"bar\",\"baz\"],\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			JSONObject ajson = new JSONObject();
			JSONObject adecor = new JSONObject();
			JSONObject mc = new JSONObject();
			JSONArray choices = new JSONArray();
			ajson.put("decorations", adecor);
			Answer ans = AnswerFactory.newAnswer(null, "1", ajson);
			q.addAnswer(ans);
		} catch (InvalidAnswerException e) {
			assertEquals("No multiple choice submitted.\n", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void testQuestionMultipleChoiceAnswerOutOfRangeHigh() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"multipleChoice\":{\"choices\":[\"Foo\",\"bar\",\"baz\"],\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			
			JSONObject ajson = new JSONObject();
			JSONObject adecor = new JSONObject();
			JSONObject mc = new JSONObject();
			JSONArray choices = new JSONArray();
			choices.put(5);
			mc.put("choices", choices);
			adecor.put("multipleChoice", mc);
			ajson.put("decorations", adecor);
			Answer ans = AnswerFactory.newAnswer(null, "1", ajson);
			q.addAnswer(ans);
		} catch (InvalidAnswerException e) {
			assertEquals("Selected answer does not correspond to a valid choice.\n", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void testQuestionMultipleChoiceAnswerOutOfRangeLow() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"multipleChoice\":{\"choices\":[\"Foo\",\"bar\",\"baz\"],\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			
			JSONObject ajson = new JSONObject();
			JSONObject adecor = new JSONObject();
			JSONObject mc = new JSONObject();
			JSONArray choices = new JSONArray();
			choices.put(-1);
			mc.put("choices", choices);
			adecor.put("multipleChoice", mc);
			ajson.put("decorations", adecor);
			Answer ans = AnswerFactory.newAnswer(null, "1", ajson);
			q.addAnswer(ans);
		} catch (InvalidAnswerException e) {
			assertEquals("Selected answer does not correspond to a valid choice.\n", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void basicTest1() {
		testQuestionFreeResponse();
	}
	
	@Test
	public void basicTest2() {
		testQuestionFreeResponseNoResponse();
	}
	
	@Test
	public void basicTest3() {
		testQuestionFreeResponseEmptyResponse();
	}
	
	@Test
	public void basicTest4() {
		testQuestionMultipleChoice();
	}
	
	@Test
	public void basicTest5() {
		testQuestionMultipleChoiceTooManyAnswers();
	}
	
	@Test
	public void basicTest6() {
		testQuestionMultipleChoiceInsufficientAnswers();
	}
	
	@Test
	public void basicTest7() {
		testQuestionMultipleChoiceNoMC();
	}
	
	@Test
	public void basicTest8() {
		testQuestionMultipleChoiceAnswerOutOfRangeHigh();
	}

}
