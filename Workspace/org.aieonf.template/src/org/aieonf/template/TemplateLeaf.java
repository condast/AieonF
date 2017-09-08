package org.aieonf.template;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.template.property.ITemplateProperty;
import org.aieonf.template.property.TemplateProperty;

public class TemplateLeaf<T extends IDescriptor> extends ModelLeaf<T> implements ITemplateLeaf<T>
{
	private Map<Enum<?>,TemplateProperty<T, Enum<?>, String>> attributes;
	
	public TemplateLeaf( T descriptor )
	{
		super( descriptor );
		attributes = new HashMap<Enum<?>, TemplateProperty<T,Enum<?>,String>>();
	}

	
	@Override
	public ITemplateProperty.Attributes[] attributes(Enum<?> key) {
		TemplateProperty<T, Enum<?>,?> prop = attributes.get( key );
		return prop.attributes();
	}


	@Override
	public void addAttribute( Enum<?> key, ITemplateProperty.Attributes attr, String value ){		
		TemplateProperty<T, Enum<?>,String> prop = null;
		if( attributes.containsKey(key) )
			prop = attributes.get( key );
		else{
			prop = new TemplateProperty<T, Enum<?>, String>( this, key, value );
			attributes.put( key, prop );
		}
		prop.setAttribute(attr, value);
	}

	@Override
	public void addAttributes( Enum<?> key, Map<ITemplateProperty.Attributes, String> attrs ){
		TemplateProperty<T, Enum<?>, String> prop = null;
		if( attributes.containsKey(key) )
			prop = attributes.get( key );
		else{
			prop = new TemplateProperty<T, Enum<?>, String>( this, key, super.get( key ));
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
		TemplateProperty<T, Enum<?>, String> prop = attributes.get( key );
		if( prop != null )
			prop.removeAttribute(attr);
	}
}