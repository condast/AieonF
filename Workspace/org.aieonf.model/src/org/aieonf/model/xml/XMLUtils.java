package org.aieonf.model.xml;

import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;
import org.xml.sax.Attributes;

public class XMLUtils {

	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static Map<String, String> convertAttributes( Attributes attributes ){
		Map<String,String> attrs = new HashMap<String,String>();
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !StringUtils.isEmpty( attributes.getLocalName(i))){
				attrs.put( attributes.getLocalName( i ), attributes.getValue(i));
			}
		}
		return attrs;
	}

	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static Map<String, String> convertToEnumAttributes( Attributes attributes ){
		Map<String,String> attrs = new HashMap<String,String>();
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !StringUtils.isEmpty( attributes.getLocalName(i))){
				String str = StringStyler.styleToEnum( attributes.getLocalName( i ));
				attrs.put( str, attributes.getValue(i));
			}
		}
		return attrs;
	}

	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static Map<String, String> convertAttributesToProperties( Attributes attributes ){
		Map<String, String> attrs = new HashMap<String, String>();
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !StringUtils.isEmpty( attributes.getLocalName(i))){
				attrs.put( attributes.getLocalName( i ), attributes.getValue(i));
			}
		}
		return attrs;
	}

	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static IDescriptor convertAttributesToDescriptor( Attributes attributes ){
		IConceptBase base = new ConceptBase();
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !StringUtils.isEmpty( attributes.getLocalName(i))){
				base.set( attributes.getLocalName( i ), attributes.getValue(i));
			}
		}
		return new Descriptor( base );
	}

}
