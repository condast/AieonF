package org.aieonf.model.xml;

import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;

public class XMLModel<T extends IDescriptor> extends Model<T>{

	private Map<String, String> properties;

	public XMLModel( org.xml.sax.Attributes attributes) {
		super( -1);
		properties = new HashMap<String, String>();
		fill( attributes );
		super.setLeaf(true);
	}
	
	private void fill( org.xml.sax.Attributes attributes ){
		properties = XMLUtils.convertAttributesToProperties(attributes);
		String identifier = properties.get(IDescriptor.Attributes.ID.name());
		set( IModelLeaf.Attributes.IDENTIFIER.name(), identifier);
		properties.put(IDescriptor.Attributes.ID.name(), String.valueOf(-1));
		String name = getName();
		if(!Utils.assertNull( name )) {
			properties.put( IDescriptor.Attributes.NAME.name(), name);
		}
	}

	public long getID(){
		String key = StringStyler.xmlStyleString( IDescriptor.Attributes.ID.name()); 
		String value = properties.get( key);
		return StringUtils.isEmpty(value)?-1: Long.parseLong(value);
	}

	public String getName(){
		String key = StringStyler.xmlStyleString( IDescriptor.Attributes.NAME.name()); 
		return properties.get( key);
	}

	protected String getAttribute( String key ) {
		return this.properties.get(key);
	}

	protected void addAttribute( String key, String value ) {
		this.properties.put(key, value);
	}
	
	@Override
	public void setData(T descriptor) {
		String str = properties.get( IModelLeaf.Attributes.IDENTIFIER.name()); 
		if(!StringUtils.isEmpty(str))
			descriptor.set( IDescriptor.Attributes.ID, str);
		str = properties.get( IDescriptor.Attributes.NAME.name()); 
		if(!StringUtils.isEmpty(str))
			descriptor.set( IDescriptor.Attributes.NAME, properties.get( IDescriptor.Attributes.NAME.name()) );
		str = properties.get( IModelLeaf.Attributes.TYPE.name()); 
		if(!StringUtils.isEmpty(str))
		descriptor.set( IDescriptor.Attributes.VERSION, properties.get( IDescriptor.Attributes.NAME.name()) );
		super.setData(descriptor);
	}	
}