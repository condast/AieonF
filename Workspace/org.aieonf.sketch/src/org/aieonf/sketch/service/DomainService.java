package org.aieonf.sketch.service;

import org.aieonf.osgi.selection.DomainEvent;
import org.aieonf.osgi.selection.IActiveDomainManager;

public class DomainService implements IActiveDomainManager {

	private static DomainService service = new DomainService();
	
	private DomainService() {}

	public static DomainService getInstance(){
		return service;
	}
	
	@Override
	public void notifyActiveDomainChanged(DomainEvent event) {
		// TODO Auto-generated method stub
		
	}

}
