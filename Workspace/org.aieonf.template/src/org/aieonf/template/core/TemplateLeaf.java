package org.aieonf.template.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;
import org.aieonf.template.def.ITemplateLeaf;
import org.aieonf.template.property.ITemplateProperty;
import org.aieonf.template.property.TemplateProperty;

public class TemplateLeaf<T extends IDescriptor> extends Model<T> implements ITemplateLeaf<T>
{
	private Map<Enum<?>,TemplateProperty<T, String>> attributes;

	protected TemplateLeaf( org.xml.sax.Attributes attrs ){
		attributes = new HashMap<Enum<?>, TemplateProperty<T,String>>();
		fill( attrs );
	}

	public TemplateLeaf( T descriptor ){
		attributes = new HashMap<Enum<?>, TemplateProperty<T,String>>();
		setData( descriptor );
	}

	public TemplateLeaf( T descriptor, org.xml.sax.Attributes attrs ){
		this( attrs );
		setData( descriptor );
	}
	
	@Override
	public ITemplateProperty.Attributes[] attributes(Enum<?> key) {
		TemplateProperty<T,?> prop = attributes.get( key );
		return prop.attributes();
	}

	@Override
	public void setData(T descriptor) {
		super.setData(descriptor);
	}

	private void fill( org.xml.sax.Attributes attributes ){
		String name = attributes.getValue( StringStyler.xmlStyleString( IDescriptor.Attributes.NAME.name() ));
		if(!Utils.assertNull( name )) {
			super.set( IModelLeaf.Attributes.NAME.name().toLowerCase(), name );
			super.set( IModelLeaf.Attributes.IDENTIFIER.name().toLowerCase(), name );
		}
		String id = attributes.getValue( StringStyler.xmlStyleString( IDescriptor.Attributes.NAME.name() ));
		if(!Utils.assertNull( id )) {
			set( IDescriptor.Attributes.ID, id);
		}
	}

	@Override
	public void addAttribute( Enum<?> key, ITemplateProperty.Attributes attr, String value ){		
		TemplateProperty<T, String> prop = null;
		if( attributes.containsKey(key) )
			prop = attributes.get( key );
		else{
			prop = new TemplateProperty<T, String>( this, StringStyler.xmlStyleString( key.name()).toLowerCase(), value );
			attributes.put( key, prop );
		}
		prop.setAttribute(attr, value);
	}

	@Override
	public void addAttributes( Enum<?> key, Map<ITemplateProperty.Attributes, String> attrs ){
		TemplateProperty<T, String> prop = null;
		if( attributes.containsKey(key) )
			prop = attributes.get( key );
		else{
			prop = new TemplateProperty<T, String>( this, StringStyler.xmlStyleString( key.name()), super.get( key ));
			attributes.put( key, prop );
		}
		Set<Map.Entry<ITemplateProperty.Attributes, String>> entries = attrs.entrySet();
		Iterator<Map.Entry<ITemplateProperty.Attributes, String>> iterator = entries.iterator();
		while( iterator.hasNext() ){
			Map.Entry<ITemplateProperty.Attributes, String> entry  = iterator.next();
			prop.setAttribute( entry.getKey(), entry.getValue());
		}
		
	}

	@Override
	public void removeAttribute( Enum<?> key, ITemplateProperty.Attributes attr ){
		TemplateProperty<T, String> prop = attributes.get( key );
		if( prop != null )
			prop.removeAttribute(attr);
	}
}