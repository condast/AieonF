package org.aieonf.browsersupport.service;

import org.aieonf.commons.db.IDatabaseConnection;
import org.aieonf.commons.db.IDatabaseConnection.Requests;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.request.AbstractKeyEventDataProvider;
import org.aieonf.concept.request.IKeyEventDataProvider;
import org.aieonf.concept.request.KeyEvent;
import org.aieonf.model.core.IModelLeaf;
import org.osgi.service.component.annotations.Component;

@Component( name="org.aieonf.browsersupport.key.data.service",
immediate=true)
public class KeyEventDataProvider extends AbstractKeyEventDataProvider<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>> implements IKeyEventDataProvider<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>> {

	public KeyEventDataProvider() {
	}

	@Override
	protected IModelLeaf<IDescriptor> onProcesskeyEvent(KeyEvent<Requests> event) {
		// TODO Auto-generated method stub
		return null;
	}
		
}