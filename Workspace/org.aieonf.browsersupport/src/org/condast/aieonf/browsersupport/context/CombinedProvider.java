package org.condast.aieonf.browsersupport.context;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.template.provider.AbstractModelProvider;
import org.aieonf.util.parser.ParseException;

public class CombinedProvider<T extends ILoaderAieon> extends AbstractModelProvider<T,IDescriptor, IModelLeaf<IDescriptor>> 
{
	private Collection<IModelProvider<T, IModelLeaf<IDescriptor>>> providers;
	
	public CombinedProvider( IContextAieon context, IModelLeaf<T> model )
	{
		super( context, model );
		providers = new ArrayList<IModelProvider<T, IModelLeaf<IDescriptor>>>();
	}

	public void addProvider( IModelProvider<T, IModelLeaf<IDescriptor>> provider ){
		this.providers.add( provider );
	}

	public void removeProvider( IModelProvider<T, IModelLeaf<IDescriptor>> provider ){
		this.providers.remove( provider );
	}

	
	@Override
	public void open() {
		for( IModelProvider<T, IModelLeaf<IDescriptor>> provider: this.providers ){
			provider.open();
		}
		super.open();
	}

	@Override
	public void close() {
		for( IModelProvider<T, IModelLeaf<IDescriptor>> provider: this.providers ){
			provider.close();
		}
		super.close();
	}

	@Override
	protected void onSetup( ManifestAieon manifest ) {
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> onSearch( IModelFilter<IDescriptor> filter) throws ParseException{
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		for( IModelProvider<T, IModelLeaf<IDescriptor>> provider: this.providers ){
			if( provider.isOpen() )
				results.addAll( provider.search(filter));
		}
		return results;
	}
}
