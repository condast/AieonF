package org.aieonf.commons.security;

import java.util.Map;

public abstract class AbstractSecureProvider implements ISecureCall, ISecureGenerator {

	protected AbstractSecureProvider() {}

	@Override
	public Map.Entry<Long, Long> createIdAndToken(String domain) {
		return getEntry( domain );
	}

	@Override
	public boolean isSecure(long id, long token, String domain) {
		Map.Entry<Long, Long> entry = getEntry(domain);
		return (( id == entry.getKey() ) && ( token == entry.getValue() ));
	}
	
	protected abstract long createId( String domain ); 
	protected abstract long createToken( String domain ); 

	protected Map.Entry<Long, Long> getEntry( String domain ) {
		return new Entry( createId( domain ), createToken( domain ));
	}

	private class Entry implements Map.Entry<Long, Long>{

		private long id, token;
		
		protected Entry(long id, long token) {
			super();
			this.id = id;
			this.token = token;
		}

		@Override
		public Long getKey() {
			return id;
		}

		@Override
		public Long getValue() {
			return token;
		}

		@Override
		public Long setValue(Long token) {
			this.token=token;
			return token;
		}	
	}
}