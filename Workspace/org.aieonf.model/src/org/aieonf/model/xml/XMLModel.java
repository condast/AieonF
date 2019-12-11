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
		super();
		properties = new HashMap<String, String>();
		fill( attributes );
		super.setLeaf(true);
	}
	
	private void fill( org.xml.sax.Attributes attributes ){
		properties = XMLUtils.convertAttributesToProperties(attributes);
		String name = getName();
		if(!Utils.assertNull( name )) {
			properties.put( IModelLeaf.Attributes.IDENTIFIER.name().toLowerCase(), getName());
		}
		set( IDescriptor.Attributes.ID, getID());
		set( IDescriptor.Attributes.NAME, getName());
	}

	public String getID(){
		String key = StringStyler.xmlStyleString( IDescriptor.Attributes.ID.name().toLowerCase()); 
		return properties.get( key);
	}

	public String getName(){
		String key = StringStyler.xmlStyleString( IDescriptor.Attributes.NAME.name().toLowerCase()); 
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
		str = properties.get( IModelLeaf.Attributes.NAME.name()); 
		if(!StringUtils.isEmpty(str))
			descriptor.set( IDescriptor.Attributes.NAME, properties.get( IModelLeaf.Attributes.NAME.name()) );
		str = properties.get( IModelLeaf.Attributes.TYPE.name()); 
		if(!StringUtils.isEmpty(str))
		descriptor.set( IDescriptor.Attributes.VERSION, properties.get( IModelLeaf.Attributes.NAME.name()) );
		super.setData(descriptor);
	}	
}