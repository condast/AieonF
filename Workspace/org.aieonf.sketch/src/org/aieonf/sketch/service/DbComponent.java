package org.aieonf.sketch.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.db.IAieonFDbService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component( name="org.aieonf.orientdb.domain.service", immediate=true)
public class DbComponent {

	private DBService service = DBService.getService();
		
	public void activate(){/* nothing */}
	public void deactivate(){/* nothing */}

	@Reference( cardinality = ReferenceCardinality.AT_LEAST_ONE,
			policy=ReferencePolicy.STATIC)
	public void addManager( IAieonFDbService<IDescriptor> service){
		this.service.addManager( service );
	}

	public void removeManager( IAieonFDbService<IDescriptor> service){
		this.service.removeManager( service );
	}
}