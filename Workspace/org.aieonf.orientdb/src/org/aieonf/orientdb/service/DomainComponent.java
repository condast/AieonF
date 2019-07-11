package org.aieonf.orientdb.service;

import org.aieonf.osgi.domain.IActiveDomainProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component( name="org.aieonf.orientdb.domain.service", immediate=true)
public class DomainComponent {

	private DomainService service = DomainService.getService();
		
	public void activate(){/* nothing */}
	public void deactivate(){/* nothing */}

	@Reference( cardinality = ReferenceCardinality.AT_LEAST_ONE,
			policy=ReferencePolicy.STATIC)
	public void addManager( IActiveDomainProvider provider){
		this.service.addProvider(provider);
	}

	public void removeManager( IActiveDomainProvider provider){
		this.service.removeProvider( provider );
	}
}