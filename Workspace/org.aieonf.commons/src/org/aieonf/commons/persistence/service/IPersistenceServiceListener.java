package org.aieonf.commons.persistence.service;

import org.aieonf.commons.strings.StringUtils;

public interface IPersistenceServiceListener {
	public static enum Services{
		OPEN,
		CREATE,
		ADD,
		UPDATE,
		DELETE,
		CLOSE;

		@Override
		public String toString() {
			return StringUtils.prettyString(super.toString());
		}
	}
	
	/**
	 * Notify a change in the service
	 */
	void notifyServiceChanged( PersistencyServiceEvent event );
}
