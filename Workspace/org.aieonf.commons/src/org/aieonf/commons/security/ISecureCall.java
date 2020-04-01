package org.aieonf.commons.security;

public interface ISecureCall {

	public boolean isSecure( long id, long token, String domain );
}
