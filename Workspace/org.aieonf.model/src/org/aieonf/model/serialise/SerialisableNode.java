package org.aieonf.model.serialise;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelLeaf;

public class SerialisableNode {

	//properties
	private Map<String, String> p;

	//descriptor
	private Map<String, String> d;


	protected SerialisableNode() {
		p = new HashMap<>();
		d = new HashMap<>();
	}
	
	public SerialisableNode( IModelLeaf<? extends IDescriptor> model ) {
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
	}

	protected void putDescriptor(String key, String property) {
		d.put(key, property);
	}

	protected void put(String key, String property) {
		p.put(key, property);
	}
	
	public boolean isleaf() {
		return true;
	}

	public Map<String, String> getProperties() {
		return p;
	}

	public Map<String, String> getDescriptors() {
		return d;
	}

	@Override
	public String toString() {
		return p.toString();
	}

	/**
	 * Create a model from the given serialisable model
	 * @param sm
	 * @return
	 */
	public static IModelLeaf<IDescriptor> createModel( SerialisableNode sm ) {
		IDescriptor descriptor = new Descriptor();
		for( Map.Entry<String, String> entry: sm.getDescriptors().entrySet())
			descriptor.set(entry.getKey(), entry.getValue());
		IModelLeaf<IDescriptor> leaf = sm.isleaf()? new ModelLeaf<IDescriptor>( descriptor): new Model<IDescriptor>( descriptor );
		for( Map.Entry<String, String> entry: sm.getProperties().entrySet())
			leaf.set(entry.getKey(), entry.getValue());
		return leaf;
	}
}
