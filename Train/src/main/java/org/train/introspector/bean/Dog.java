package org.train.introspector.bean;

import java.io.Serializable;

public class Dog extends Animal implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6735477025317747485L;
	protected String tail;

	public String getTail() {
		return tail;
	}

	public void setTail(String tail) {
		this.tail = tail;
	}
	
	
}
