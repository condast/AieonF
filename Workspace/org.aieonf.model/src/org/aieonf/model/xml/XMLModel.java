package org.aieonf.model.xml;

import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.Model;

public class XMLModel extends Model<IDescriptor>{

	public XMLModel( org.xml.sax.Attributes attributes) {
		super( -1);
		fill( attributes );
		super.setLeaf(true);
	}
	
	private void fill( org.xml.sax.Attributes attributes ){
		IDescriptor data = XMLUtils.convertAttributesToDescriptor(attributes); 
		setData( data );
		String identifier = data.get(IDescriptor.Attributes.ID.name());
		set( IConcept.Attributes.IDENTIFIER.name(), identifier);
		data.set(IDescriptor.Attributes.ID.name(), String.valueOf(-1));
		String name = getName();
		if(!StringUtils.isEmpty( name )) {
			data.set( IDescriptor.Attributes.NAME.name(), name);
		}
	}
}