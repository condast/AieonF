package org.aieonf.sketch.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.db.IAieonFDbService;
import org.aieonf.model.filter.IModelFilter;

public class DBService implements IAieonFDbService<IDescriptor>{

	private Collection<IAieonFDbService<IDescriptor>> services;
	
	private IAieonFDbService<IDescriptor> selected;
	
	private static DBService service = new DBService();
	
	private DBService() {
		services = new ArrayList<>();
	}
	
	public static DBService getService() {
		return service;
	}

	public void addManager( IAieonFDbService<IDescriptor> service){
		this.services.add( service );
	}

	public void removeManager( IAieonFDbService<IDescriptor> service){
		this.services.remove( service );
	}

	@Override
	public boolean connect(ILoaderAieon domain) {
		for(IAieonFDbService<IDescriptor> service: this.services ) {
			if( service.connect(domain)) {
				selected = service;
				return true;
			}
		}
		return false;
	}

	@Override
	public void disconnect(ILoaderAieon domain) {
		selected.disconnect(domain);
		this.selected = null;
	}


	@Override
	public IModelLeaf<IContextAieon> create(String identifier, Class<?> clss, InputStream in){
		return this.selected.create(identifier, clss, in);
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> getModels(IDescriptor descriptor, boolean checkVersion) {
		return this.selected.getModels(descriptor, checkVersion);
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> search(String key, String value) {
		return this.selected.search(key, value);
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> search(IModelFilter<IDescriptor, IDescribable<IDescriptor>> filter) {
		return this.selected.search(filter);
	}

	@Override
	public boolean remove(IDescriptor descriptor) {
		return this.selected.remove(descriptor);
	}

	@Override
	public boolean update(IModelLeaf<IDescriptor> model) {
		return selected.update(model);
	}
}
