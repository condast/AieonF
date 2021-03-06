package org.aieonf.orientdb.service;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.orientdb.core.Dispatcher;
import org.aieonf.orientdb.db.DatabasePersistenceService;
import org.aieonf.osgi.selection.IActiveDomainProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component( name=ActiveDomainComponent.S_ACTIVE_DOMAIN_ID, immediate=true)
public class ActiveDomainComponent {

	public static final String S_ACTIVE_DOMAIN_ID = "org.aieonf.orientdb.active.domain";
	
	private Dispatcher service = Dispatcher.getInstance();

	private static DatabasePersistenceService persistence = DatabasePersistenceService.getInstance();

	public void activate(){/* nothing */}
	public void deactivate(){/* nothing */}

	@Reference( cardinality = ReferenceCardinality.MANDATORY,
			policy=ReferencePolicy.DYNAMIC)
	public void setDomainProvider( IActiveDomainProvider manager){
		this.service.setProvider( manager );
		for( IDomainAieon domain: manager.getDomains()) {
			persistence.createCluster(domain);
		}
	}

	public void unsetDomainProvider( IActiveDomainProvider manager){
		this.service.setProvider(null);
	}

}
