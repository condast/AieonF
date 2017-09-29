package org.aieonf.commons.ui.language;

import org.aieonf.commons.i8n.Language;

public class NaLanguage extends Language {

	private static final String S_LANGUAGE = "NALanguage";

	private static NaLanguage language = new NaLanguage();
	
	private NaLanguage() {
		super( S_LANGUAGE, "UK", "en");
	}
	
	public static NaLanguage getInstance(){
		return language;
	}	
	
	
}
