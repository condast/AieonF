package org.aieonf.sketch.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.osgi.js.AbstractJavascriptController;
import org.aieonf.sketch.core.IKeywordListener;
import org.aieonf.sketch.core.KeywordEvent;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

public class KeywordController extends AbstractJavascriptController{

	public static final String S_FUNCTION_NAME = "updateBody";
	public static final String S_CALLBACK_ID = "sketch.id";

	
	private String name;
	private Browser browser;
	private JavaScriptCallBack callback;
	
	private Collection<IKeywordListener> keyListeners;

	public KeywordController( Browser browser ) {
		this( browser, S_FUNCTION_NAME );
		keyListeners = new ArrayList<>();
	}

	public KeywordController( Browser browser, String name ) {
		super( browser, S_CALLBACK_ID );
		this.browser = browser;
		this.callback = new JavaScriptCallBack(browser, name);
	}

	public Browser getBrowser() {
		return browser;
	}

	public String getName() {
		return name;
	}

	public void addKeywordlistener( IKeywordListener listener ) {
		this.keyListeners.add(listener);
	}

	public void removeKeywordlistener( IKeywordListener listener ) {
		this.keyListeners.remove(listener);
	}
	
	protected void notifyKeywordListeners(  KeywordEvent event ) {
		for( IKeywordListener listener: keyListeners)
			listener.notifyKeyWordEvent(event);
	}

	protected JavaScriptCallBack getCallback() {
		return callback;
	}

	
	@Override
	protected void onLoadCompleted() {
		// TODO Auto-generated method stub
		
	}


	/**
	 * a default browser function that can be added to javascript code for call back
	 * @author Kees
	 *
	 */
	private class JavaScriptCallBack extends BrowserFunction{
	
		private JavaScriptCallBack(Browser browser, String functionName ) {
			super(browser, functionName);
		}

		@Override
		public Object function(Object[] arguments) {
			Object result = null;
			try {
				notifyKeywordListeners( new KeywordEvent(this, (String) arguments[0]));
				result = super.function(arguments);
			}
			catch( Exception ex ) {
				ex.printStackTrace();
			}
			return result;
		}	
	}
}
