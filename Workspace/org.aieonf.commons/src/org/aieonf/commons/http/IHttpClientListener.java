package org.aieonf.commons.http;

public interface IHttpClientListener<R extends Object, D extends Object> {

	public void notifyResponse( ResponseEvent<R,D> event);
}
