package org.aieonf.template.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;

public class CombinedProvider<U extends Object> implements IModelProvider<U> 
{
	public final static String S_IDENTIFIER = "Combined";
	
	private Collection<IModelProvider<U>> providers;
	
	public CombinedProvider()
	{
		providers = new ArrayList<IModelProvider<U>>();
	}

	@Override
	public String getIdentifier() {
		return S_IDENTIFIER;
	}

	public void addProvider( IModelProvider<U> provider ){
		this.providers.add( provider );
	}

	public void removeProvider( IModelProvider<U> provider ){
		this.providers.remove( provider );
	}

	public String[] getIdentifiers(){
		Collection<String> collection = new ArrayList<String>();
		for( IModelProvider<U> provider: this.providers ){
			collection.add( provider.getIdentifier());
		}	
		return collection.toArray( new String[ collection.size()]);
	}
	
	/**
	 * Get the provider with the given name
	 * @param identifier
	 * @return
	 */
	public IModelProvider<U> getModelProvider( String identifier ){
		if( Utils.assertNull( identifier ))
			return null;
		for( IModelProvider<U> provider: this.providers ){
			if( identifier.equals( provider.getIdentifier() ))
				return provider;
		}		
		return null;
	}
	
	@Override
	public void open( IDomainAieon domain) {
		for( IModelProvider<U> provider: this.providers ){
			provider.open( domain );
		}
	}

	@Override
	public boolean isOpen( IDomainAieon domain) {
		for( IModelProvider<U> provider: this.providers ){
			if( provider.isOpen( domain ))
				return true;
		}
		return false;
	}


	@Override
	public void close( IDomainAieon domain ) {
		for( IModelProvider<U> provider: this.providers ){
			provider.close( domain );
		}
	}

	@Override
	public void addListener(IModelBuilderListener<U> listener) {
		for( IModelProvider<U> provider: this.providers ){
			provider.addListener(listener);
		}
	}

	@Override
	public void removeListener(IModelBuilderListener<U> listener) {
		for( IModelProvider<U> provider: this.providers ){
			provider.removeListener(listener);
		}
	}

	@Override
	public boolean hasFunction(String function) {
		for( IModelProvider<U> provider: this.providers ){
			if( provider.hasFunction(function))
			return true;
		}
		return false;
	}

	@Override
	public boolean contains(IModelLeaf<? extends IDescriptor> leaf) {
		for( IModelProvider<U> provider: this.providers ){
			if( provider.contains(leaf))
				return true;
		}
		return false;
	}

	@Override
	public Collection<U> get(IDescriptor descriptor)
			throws ParseException {
		Collection<U> results = new ArrayList<U>();
		for( IModelProvider<U> provider: this.providers ){
			Collection<U> temp = provider.get(descriptor);
			if(( temp != null ) && ( !temp.isEmpty() ))
				results.addAll( temp );
		}
		return results;
	}

	@Override
	public Collection<U> search(
			IModelFilter<IDescriptor> filter) throws ParseException {
		Collection<U> results = new ArrayList<U>();
		for( IModelProvider<U> provider: this.providers ){
			try{
				if( !provider.isOpen( null))
					continue;
				Collection<U> temp = provider.search( filter );
				if(( temp != null ) && ( !temp.isEmpty() ))
					results.addAll( temp );
			}
			catch( ParseException e ){
				e.printStackTrace();
			}
		}
		return results;
	}
	
	@Override
	public String printDatabase() {
		StringBuffer buffer = new StringBuffer();
		for( IModelProvider<U> provider: this.providers ){
			buffer.append( provider.printDatabase() + "\n\n" );
		}
		return buffer.toString();
	}

	@Override
	public void deactivate() {
		for( IModelProvider<U> provider: this.providers ){
			provider.deactivate();
		}
	}

	@Override
	public void sync() {
		for( IModelProvider<U> provider: this.providers ){
			provider.sync();
		}
	}
}
