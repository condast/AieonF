package org.aieonf.model.db;

import java.io.InputStream;
import java.util.Collection;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.filter.IModelFilter;

public interface IAieonFDbService< D extends IDescriptor> {

	public IModelLeaf<IContextAieon> create(String identifier, Class<?> clss, InputStream in);

	public Collection<IModelLeaf<D>> getModels( D descriptor, boolean checkVersion );
	
	public Collection<IModelLeaf<D>> search( String key, String value );

	public Collection<IModelLeaf<D>> search( IModelFilter<D, IDescribable<D>> filter );

	public boolean remove( D descriptor );
	
	public boolean update( IModelLeaf<D> model );

	boolean connect(ILoaderAieon domain);

	void disconnect(ILoaderAieon domain);

}
