package edu.utsa.cs.gaediscussion.resources;

import java.io.Serializable;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;

import edu.utsa.cs.gaediscussion.util.PMF;

public class QuestionDecorationWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1720422588564965500L;
	private String wrappedClass;
	private Key wrappedKey;
	
	public QuestionDecorationWrapper(String wrappedClass, Key wrappedKey) {
		super();
		this.wrappedClass = wrappedClass;
		this.wrappedKey = wrappedKey;
	}

	public String getWrappedClass() {
		return wrappedClass;
	}

	public void setWrappedClass(String wrappedClass) {
		this.wrappedClass = wrappedClass;
	}

	public Key getWrappedKey() {
		return wrappedKey;
	}

	public void setWrappedKey(Key wrappedKey) {
		this.wrappedKey = wrappedKey;
	}
	
	public QuestionDecorator getQuestionDecorator() {
		QuestionDecorator qd = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			qd = (QuestionDecorator) pm.getObjectById(Class.forName(wrappedClass), wrappedKey);
			qd = pm.detachCopy(qd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		
		return qd;
	}
}
