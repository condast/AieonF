package org.aieonf.commons.persistence.service;

import java.util.EventObject;

import org.aieonf.commons.persistence.service.IPersistenceServiceListener.Services;

public class PersistencyServiceEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private Services service;
	
	public PersistencyServiceEvent(Object arg0, Services service) {
		super(arg0);
		this.service = service;
	}

	public Services getService() {
		return service;
	}
}
