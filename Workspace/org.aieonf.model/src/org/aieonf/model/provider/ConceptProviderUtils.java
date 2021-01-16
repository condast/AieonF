package org.aieonf.model.provider;

import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.datauri.IDataURI;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelLeaf;

public class ConceptProviderUtils {

	private IConceptProvider provider;
	
	public ConceptProviderUtils(IConceptProvider provider) {
		super();
		this.provider = provider;
	}

	public <D extends IDescriptor> void complete( String key, IModelNode<D> model) {
		if( model.getData().get(key) == null )
			return;
		String attr = model.getData().get(key);
		IDataURI[] concepts = provider.query(attr);
		for( IDataURI concept: concepts ) {
			model.addChild( new ModelLeaf<IDescriptor>( concept ), provider.getName() );
		}
	}

	public <D extends IDescriptor> void complete( String key, Collection<IModelNode<D>> models) {
		for(IModelNode<D> model: models )
			complete( key, model);
	}

	public <D extends IDescriptor> void complete( String key, IModelNode<D>[] models) {
		for(IModelNode<D> model: models )
			complete( key, model);
	}
}
