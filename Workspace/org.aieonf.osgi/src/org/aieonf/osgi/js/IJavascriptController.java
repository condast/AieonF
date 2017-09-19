package org.aieonf.osgi.js;

import org.aieonf.osgi.eval.IEvaluationListener;

public interface IJavascriptController {

	boolean isInitialised();

	void addEvaluationListener(IEvaluationListener<Object[]> listener);

	void removeEvaluationListener(IEvaluationListener<Object[]> listener);

	Object evaluate(String query);

}