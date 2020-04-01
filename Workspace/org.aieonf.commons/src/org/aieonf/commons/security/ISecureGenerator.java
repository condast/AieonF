package org.aieonf.commons.security;

import java.util.Map;

public interface ISecureGenerator {

	public Map.Entry<Long,Long> createIdAndToken( String domain );
}
