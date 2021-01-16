package org.aieonf.browsersupport.service;

import org.aieonf.browsersupport.core.Dispatcher;
import org.aieonf.model.provider.IConceptProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(
		name = ConceptProviderClient.COMPONENT_NAME
	)
public class ConceptProviderClient{

	public static final String COMPONENT_NAME = "org.condast.saight.domain.selection.service";

	private Dispatcher service = Dispatcher.getInstance();
		
	public void activate(){/* nothing */}
	public void deactivate(){/* nothing */}

	@Reference( cardinality = ReferenceCardinality.AT_LEAST_ONE,
			policy=ReferencePolicy.DYNAMIC)
	public void addDomainProvider( IConceptProvider provider){
		this.service.addProvider( provider );
	}

	public void removeDomainProvider( IConceptProvider provider){
		this.service.removeProvider( provider );
	}
}