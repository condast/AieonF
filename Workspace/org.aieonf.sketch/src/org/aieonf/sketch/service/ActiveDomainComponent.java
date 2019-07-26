package org.aieonf.sketch.service;

import org.aieonf.osgi.selection.DomainEvent;
import org.aieonf.osgi.selection.IActiveDomainManager;
import org.aieonf.sketch.core.DomainService;
import org.osgi.service.component.annotations.Component;

@Component( name=ActiveDomainComponent.S_SKETCH_ID, immediate=true)
public class ActiveDomainComponent implements IActiveDomainManager {

	public static final String S_SKETCH_ID = "org.aieonf.sketch.domain.service";
	
	private DomainService service = DomainService.getInstance();
	
	public void activate(){/* nothing */}
	public void deactivate(){/* nothing */}

	@Override
	public void notifyActiveDomainChanged(DomainEvent event) {
		service.notifyActiveDomainChanged(event);
	}

}
