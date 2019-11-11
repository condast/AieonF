package org.aieonf.model.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

public class SerialisableModel extends ConceptBase
{	
	//The concept that is modelled
	private Map<String, String> base;
	private Map<String, String> descriptor;
	
	private SerialisableModel parent;
	
	private Collection<SerialisableModel> children;

	public SerialisableModel(){
		this.children = new ArrayList<>();
	}
	
	public SerialisableModel getParent() {
		return parent;
	}

	@SuppressWarnings("unchecked")
	protected SerialisableModel( SerialisableModel parent, IModelLeaf<? extends IDescriptor> leaf ) {
		this.parent = parent;
		Iterator<Map.Entry<String, String>> iterator = leaf.iterator();
		base = new HashMap<>();
		while( iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			base.put(entry.getKey(), entry.getValue());
		}
		iterator = leaf.getDescriptor().iterator();
		descriptor = new HashMap<>();
		while( iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			descriptor.put(entry.getKey(), entry.getValue());
		}
		if(!( leaf instanceof IModelNode))
			return;
		IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) leaf;
		for( IModelLeaf<? extends IDescriptor> child: model.getChildren()) {
			SerialisableModel smodel = new SerialisableModel( this, child ); 
			children.add( smodel );
		}
	}
	
	public static SerialisableModel create( IModelLeaf<? extends IDescriptor> leaf ) {
		return new SerialisableModel(null, leaf);
	}
	
	public static SerialisableModel[] create( Collection<IModelLeaf<IDescriptor>> leafs ) {
		Collection<SerialisableModel> models = new ArrayList<>();
		for( IModelLeaf<? extends IDescriptor> leaf: leafs )
			models.add( new SerialisableModel(null, leaf));
		return models.toArray( new SerialisableModel[ models.size()]);
	}

}