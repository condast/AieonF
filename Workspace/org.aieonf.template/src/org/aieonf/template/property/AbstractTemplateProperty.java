package org.aieonf.template.property;

import java.util.HashMap;
import java.util.Map;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.template.ITemplateLeaf;
import org.aieonf.model.template.property.ITemplateProperty;

public abstract class AbstractTemplateProperty<T extends IDescriptor, V extends Enum<?>, X extends Object> implements ITemplateProperty< String,V, String>{

	private ITemplateLeaf<T> leaf;
	private String key;
	private X defaultValue;

	private Map<V, String> attributes;

	protected AbstractTemplateProperty( ITemplateLeaf<T> leaf, String key, X defaultValue )
	{
		super();
		this.leaf = leaf;
		this.key = key;
		this.defaultValue = defaultValue;
		attributes = new HashMap<V, String>();
	}

	@Override
	public String getKey() {
		return this.key;
	}

	
	protected X getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setValue( String value) {
		this.leaf.getDescriptor().set(key, value);
	}

	@Override
	public boolean verify( String  value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAttribute( V attr) {
		return attributes.get( attr );
	}

	@Override
	public boolean setAttribute( V attr, String value) {
		this.attributes.put( attr, value);
		return true;
	}

	@Override
	public Object removeAttribute( V attr) {
		return this.attributes.remove(attr);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V[] attributes() {
		return (V[]) this.attributes.keySet().toArray( new Object[this.attributes.size()]);
	}
}
