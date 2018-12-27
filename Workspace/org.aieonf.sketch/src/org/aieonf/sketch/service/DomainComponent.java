package org.aieonf.sketch.service;

import org.aieonf.osgi.selection.DomainEvent;
import org.aieonf.osgi.selection.IActiveDomainManager;

public class DomainComponent implements IActiveDomainManager {

	private DomainService service = DomainService.getInstance();
	
	public void activate(){/* nothing */}
	public void deactivate(){/* nothing */}

	@Override
	public void notifyActiveDomainChanged(DomainEvent event) {
		service.notifyActiveDomainChanged(event);
	}
}
