package org.aieonf.model.xml;

import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;

public class XMLModel<T extends IDescriptor> extends Model<T>{

	private Map<String, String> properties;

	public XMLModel( org.xml.sax.Attributes attributes) {
		super();
		properties = new HashMap<String, String>();
		fill( attributes );
		super.setLeaf(true);
	}
	
	private void fill( org.xml.sax.Attributes attributes ){
		properties = XMLUtils.convertAttributesToProperties(attributes);
		String name = getName();
		if(!Utils.assertNull( name ))
			properties.put( IModelLeaf.Attributes.IDENTIFIER.name().toLowerCase(), getName());
	}

	public String getID(){
		return properties.get( IDescriptor.Attributes.ID.name().toLowerCase());
	}

	public String getName(){
		return properties.get( IDescriptor.Attributes.NAME.name().toLowerCase());
	}

	protected String getAttribute( String key ) {
		return this.properties.get(key);
	}

	protected void addAttribute( String key, String value ) {
		this.properties.put(key, value);
	}
	
	@Override
	public void setLeaf(boolean leaf) {
		super.setLeaf(leaf);
	}

	@Override
	public void init(T descriptor) {
		String str = properties.get( IModelLeaf.Attributes.IDENTIFIER.name()); 
		if(!StringUtils.isEmpty(str))
			descriptor.set( IDescriptor.Attributes.ID, str);
		str = properties.get( IModelLeaf.Attributes.NAME.name()); 
		if(!StringUtils.isEmpty(str))
			descriptor.set( IDescriptor.Attributes.NAME, properties.get( IModelLeaf.Attributes.NAME.name()) );
		str = properties.get( IModelLeaf.Attributes.TYPE.name()); 
		if(!StringUtils.isEmpty(str))
		descriptor.set( IDescriptor.Attributes.VERSION, properties.get( IModelLeaf.Attributes.NAME.name()) );
		super.init(descriptor);
	}	
}