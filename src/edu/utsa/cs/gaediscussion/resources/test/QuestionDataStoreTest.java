package edu.utsa.cs.gaediscussion.resources.test;

import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.utsa.cs.gaediscussion.resources.Answer;
import edu.utsa.cs.gaediscussion.resources.AnswerFactory;
import edu.utsa.cs.gaediscussion.resources.QuestionConcrete;
import edu.utsa.cs.gaediscussion.resources.QuestionDecorator;
import edu.utsa.cs.gaediscussion.resources.QuestionFactory;
import edu.utsa.cs.gaediscussion.resources.QuestionFreeResponse;
import edu.utsa.cs.gaediscussion.resources.QuestionMultipleChoice;
import edu.utsa.cs.gaediscussion.util.PMF;

public class QuestionDataStoreTest {

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

	private void doBasicTest() {
		Long k = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			QuestionConcrete q = new QuestionConcrete("What is 2+2?", false);
			q = pm.makePersistent(q);
			q = pm.detachCopy(q);

			QuestionDecorator qd = new QuestionFreeResponse(q, true);
			qd = pm.makePersistent(qd);
			q.addDecorator(qd);

			List<String> choices = Arrays.asList("Foo", "bar", "baz");
			qd = new QuestionMultipleChoice(q, true, 1, 1, choices);
			qd = pm.makePersistent(qd);
			q.addDecorator(qd);

			q = pm.makePersistent(q);
			k = q.getKey();

			try {
				QuestionConcrete qc = (QuestionConcrete) pm.getObjectById(
						QuestionConcrete.class, k);
				assertTrue(qc.toJson().toString()
						.equals("{\"answerspublic\":false,\"current\":false,\"active\":false,\"decorations\":{\"freeResponse\":{\"required\":true},\"multipleChoice\":{\"choices\":[\"Foo\",\"bar\",\"baz\"],\"required\":true}},\"question\":\"What is 2+2?\",\"question_id\":1}"));
			} catch (Exception e) {
				System.err.println("It's all gone horribly wrong!");
				e.printStackTrace();
				fail();
			}
		} finally {
			pm.close();
		}
	}
	
	private void doFactoryTest() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"freeResponse\":{\"required\":true},\"multipleChoice\":{\"choices\":[\"Foo\",\"bar\",\"baz\"],\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				QuestionConcrete qc = (QuestionConcrete) pm.getObjectById(
						QuestionConcrete.class, q.getKey());
				System.err.println(qc.toJson().toString());
				assertTrue(qc.toJson().toString()
						.equals("{\"answerspublic\":false,\"current\":false,\"active\":false,\"decorations\":{\"freeResponse\":{\"required\":true},\"multipleChoice\":{\"choices\":[\"Foo\",\"bar\",\"baz\"],\"required\":true}},\"question\":\"What is the square root of -1?\",\"question_id\":1}"));
				Query query = pm.newQuery(QuestionConcrete.class);
				List<QuestionConcrete> l = (List<QuestionConcrete>) query.execute();
				assertEquals(1, l.size());
			} finally {
				pm.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private void doAnswerStorageTest() {
		try {
			JSONObject json = new JSONObject();
			json.put("question", "What is the square root of -1?");
			JSONObject decor = new JSONObject("{\"freeResponse\":{\"required\":true}}");
			json.put("decorations", decor);
			QuestionConcrete q = QuestionFactory.newQuestion(json);
			Answer ans = AnswerFactory.newAnswer(null, "1", new JSONObject("{\"decorations\":{\"freeResponse\":{\"response\":\"foobar\"}}}"));
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				QuestionConcrete qc = (QuestionConcrete) pm.getObjectById(
						QuestionConcrete.class, q.getKey());
				qc.addAnswer(ans);
//				pm.makePersistent(ans);
				System.err.println(qc.toJson().toString());
				assertTrue(qc.toJson().toString()
						.equals("{\"answerspublic\":false,\"current\":false,\"active\":false,\"decorations\":{\"freeResponse\":{\"required\":true}},\"question\":\"What is the square root of -1?\",\"question_id\":1}"));
				Query query = pm.newQuery(QuestionConcrete.class);
				List<QuestionConcrete> l = (List<QuestionConcrete>) query.execute();
				assertEquals(1, l.size());
				assertEquals(1, qc.getAnswers().size());
				JSONObject foo = new JSONObject();
				qc.getAnswers().get(0).getDecorations().get(0).appendJson(foo);
				assertEquals("{\"freeResponse\":{\"response\":\"foobar\"}}", foo.toString());
			} finally {
				pm.close();
			}
			pm = PMF.get().getPersistenceManager();
			try {
				QuestionConcrete qc = (QuestionConcrete) pm.getObjectById(
						QuestionConcrete.class, q.getKey());
			} finally {
				pm.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	

	@Test
	public void test1() {
		doBasicTest();
	}
	
	@Test
	public void test2() {
		doFactoryTest();
	}
	
	@Test
	public void test3() {
		doAnswerStorageTest();
	}

}
