package org.aieonf.sketch.core;

import java.util.EventObject;

public class KeywordEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private String keywords;
	
	public KeywordEvent(Object source, String keywords) {
		super(source);
		this.keywords = keywords;
	}

	public String getKeywords() {
		return keywords;
	}
}
