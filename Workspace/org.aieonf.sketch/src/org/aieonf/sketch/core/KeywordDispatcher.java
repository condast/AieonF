package org.aieonf.sketch.core;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.osgi.eval.EvaluationEvent;
import org.aieonf.osgi.eval.IEvaluationListener;

public class KeywordDispatcher implements IEvaluationListener<Object[]> {

	private Collection<IKeywordListener> listeners;
	
	public KeywordDispatcher() {
		listeners = new ArrayList<>();
	}

	public void addKeywordlistener( IKeywordListener listener ) {
		this.listeners.add(listener);
	}

	public void removeKeywordlistener( IKeywordListener listener ) {
		this.listeners.remove(listener);
	}
	
	protected void notifyKeywordListeners(  KeywordEvent event ) {
		for( IKeywordListener listener: listeners)
			listener.notifyKeyWordEvent(event);
	}

	@Override
	public void notifyEvaluation(EvaluationEvent<Object[]> event) {
		String data = (String) event.getData()[0];
		notifyKeywordListeners( new KeywordEvent(this, data));
	}

}
