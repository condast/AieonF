package org.aieonf.model.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.aieonf.commons.Utils;
import org.aieonf.commons.db.IDatabaseConnection.Requests;
import org.aieonf.commons.http.ResponseEvent;
import org.aieonf.commons.security.ISecureGenerator;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.filter.ModelFilter;
import org.aieonf.model.serialise.ModelTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractModelRestDatabase extends AbstractRestDatabase<IDescriptor,IModelLeaf<IDescriptor>>{

	public AbstractModelRestDatabase(ISecureGenerator generator, IDomainAieon domain, String path) {
		super(generator, domain, path);
	}

	@SuppressWarnings("unchecked")
	public IModelNode<IDescriptor> createModel( String cat ) {
		IModelNode<IDescriptor> selected = (IModelNode<IDescriptor>) super.createModel();
		selected.setReverse( true );
		CategoryAieon category = new CategoryAieon( cat.trim() );
		selected.addChild( new ModelLeaf<IDescriptor>( category ));
		return selected;
	}
	
	/**
	 * Return the log entries that have been added
	 * @return
	 */
	public Collection<IModelLeaf<IDescriptor>> search( String attribute, String wildcard ){
		try {
			IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter = 
					new ModelFilter<IDescriptor, IModelLeaf<IDescriptor>>( new AttributeFilter<IDescriptor>( AttributeFilter.Rules.WILDCARD, 
							IDescriptor.Attributes.NAME.name(), attribute));
			Collection<IModelLeaf<IDescriptor>> search = search( filter );
			List<IModelLeaf<IDescriptor>> results = new ArrayList<>( search );
			Collections.sort( results );
			return results;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected String onSerialise(IModelLeaf<? extends IDescriptor>[] leaf) {
		GsonBuilder builder = new GsonBuilder(); 
		builder.enableComplexMapKeySerialization();
		ModelTypeAdapter adapter = new ModelTypeAdapter();
		builder.registerTypeAdapter( IModelLeaf.class, adapter);
		Gson gson = builder.create();
		String str = gson.toJson(leaf, IModelLeaf[].class);
		return str;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void getResults(ResponseEvent<Requests, IModelLeaf<IDescriptor>[]> event, 
			Collection<IModelLeaf<IDescriptor>> results ) {
		GsonBuilder builder = new GsonBuilder(); 
		builder.enableComplexMapKeySerialization();
		ModelTypeAdapter adapter = new ModelTypeAdapter();
		builder.registerTypeAdapter( IModelLeaf.class, adapter);
		Gson gson = builder.create();
		IModelLeaf<IDescriptor>[] models = gson.fromJson(event.getResponse(), IModelLeaf[].class ); 
		if( Utils.assertNull(models))
			return;
		results.addAll( Arrays.asList( models));
	}
}