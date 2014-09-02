package org.condast.aieonf.browsersupport.context;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.template.provider.AbstractModelProvider;
import org.aieonf.util.filter.IFilter;
import org.aieonf.util.parser.ParseException;

public class CombinedProvider<T extends ILoaderAieon> extends AbstractModelProvider<T,IConcept, IModelLeaf<IConcept>> 
{
	private Collection<IModelProvider<T, IModelLeaf<IConcept>>> providers;
	
	public CombinedProvider( IContextAieon context, IModelLeaf<T> model )
	{
		super( context, model );
		providers = new ArrayList<IModelProvider<T, IModelLeaf<IConcept>>>();
	}

	public void addProvider( IModelProvider<T, IModelLeaf<IConcept>> provider ){
		this.providers.add( provider );
	}

	public void removeProvider( IModelProvider<T, IModelLeaf<IConcept>> provider ){
		this.providers.remove( provider );
	}

	
	@Override
	public void open() {
		for( IModelProvider<T, IModelLeaf<IConcept>> provider: this.providers ){
			provider.open();
		}
		super.open();
	}

	@Override
	public void close() {
		for( IModelProvider<T, IModelLeaf<IConcept>> provider: this.providers ){
			provider.close();
		}
		super.close();
	}

	@Override
	protected void onSetup( ManifestAieon manifest ) {
	}

	@Override
	public Collection<IModelLeaf<IConcept>> onSearch( IFilter<IDescriptor> filter) throws ParseException{
		super.setFilter(filter);
		Collection<IModelLeaf<IConcept>> results = new ArrayList<IModelLeaf<IConcept>>();
		for( IModelProvider<T, IModelLeaf<IConcept>> provider: this.providers ){
			if( provider.isOpen() )
				results.addAll( provider.search(filter));
		}
		return results;
	}
}
