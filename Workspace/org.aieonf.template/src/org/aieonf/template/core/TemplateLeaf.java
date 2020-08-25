package org.aieonf.template.core;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;
import org.aieonf.template.def.ITemplateLeaf;

public class TemplateLeaf<T extends IDescriptor> extends Model<T> implements ITemplateLeaf<T>
{
	protected TemplateLeaf( org.xml.sax.Attributes attrs ){
		super( -1 );
		fill( attrs );
	}

	public TemplateLeaf( T descriptor ){
		super( descriptor);
	}

	public TemplateLeaf( T descriptor, org.xml.sax.Attributes attrs ){
		super( descriptor);
		fill( attrs );
	}
	
	private void fill( org.xml.sax.Attributes attributes ){
		String name = attributes.getValue( StringStyler.xmlStyleString( IDescriptor.Attributes.NAME.name() ));
		if(!Utils.assertNull( name )) {
			super.set( IModelLeaf.Attributes.NAME.name(), name );
		}
		String id = attributes.getValue( StringStyler.xmlStyleString( IDescriptor.Attributes.ID.name() ));
		if(!Utils.assertNull( id )) {
			set( IModelLeaf.Attributes.IDENTIFIER.name(), id);
		}
	}
}