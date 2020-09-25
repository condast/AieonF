package org.aieonf.concept.request;

import java.util.Map;

public interface IKeyEventService<R> {

	void addListener(IKeyEventListener<R> listener);

	void removeListener(IKeyEventListener<R> listener);

	void setEvent(R request, Map<String, String> parameters) throws Exception;

	void setEvent(R request, long id, long token) throws Exception;

}