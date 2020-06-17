package org.aieonf.model.serialise;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelLeaf;

public class SerialisableModel {

	//properties
	private Map<String, String> p;

	//descriptor
	private Map<String, String> d;

	//children
	private Map<SerialisableModel, String> c;

	protected SerialisableModel() {
		p = new HashMap<>();
		d = new HashMap<>();
		c = new HashMap<>();
	}
	
	public SerialisableModel( IModelLeaf<? extends IDescriptor> model ) {
		this();
		Iterator<Map.Entry<String, String>> iterator = model.iterator();
		while( iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			if( !StringUtils.isEmpty(entry.getKey()))
				p.put(entry.getKey(), entry.getValue());
		}
		iterator = model.getData().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			if( !StringUtils.isEmpty(entry.getKey()))
				d.put(entry.getKey(), entry.getValue());
		}
		if( !( model instanceof IModelNode<?> ))
			return;
		IModelNode<?> node=  (IModelNode<?>) model;
		Collection<Map.Entry<IModelLeaf<? extends IDescriptor>, String>> children = node.getChildren().entrySet();
		for( Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry: children ) {
			c.put( new SerialisableModel( entry.getKey()), entry.getValue());
		}
	}

	protected void putDescriptor(String key, String property) {
		d.put(key, property);
	}

	protected void put(String key, String property) {
		d.put(key, property);
	}
	
	protected void addChild( SerialisableModel child, String label ) {
		c.put(child, label);	
	}

	public boolean isleaf() {
		return Utils.assertNull(c);
	}

	public Map<String, String> getProperties() {
		return p;
	}

	public Map<String, String> getDescriptors() {
		return d;
	}

	public Map<SerialisableModel, String> getChildren() {
		return c;
	}
	
	/**
	 * Create a model from the given serialisable model
	 * @param sm
	 * @return
	 */
	public static IModelLeaf<IDescriptor> createModel( SerialisableModel sm ) {
		IDescriptor descriptor = new Descriptor();
		for( Map.Entry<String, String> entry: sm.getDescriptors().entrySet())
			descriptor.set(entry.getKey(), entry.getValue());
		IModelLeaf<IDescriptor> leaf = sm.isleaf()? new ModelLeaf<IDescriptor>( descriptor): new Model<IDescriptor>( descriptor );
		for( Map.Entry<String, String> entry: sm.getProperties().entrySet())
			leaf.set(entry.getKey(), entry.getValue());
		if(sm.isleaf()) 
			return leaf;
		IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) leaf;
		for( Map.Entry<SerialisableModel, String> entry: sm.getChildren().entrySet())
			model.addChild( createModel( entry.getKey() ), entry.getValue());
		return model;
	}
}
