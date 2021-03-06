package org.aieonf.concept.filter;

import java.util.Map;

import org.aieonf.commons.filter.IAttributeFilter;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;

public class FilterFactory {

	public enum Filters{
		ATTRIBUTE_LIST,
		ATTRIBUTES;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString( name() );
		}
	}

	public enum Attributes{
		FILTER,
		RULES,
		REFERENCE,
		VALUE;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString( name() );
		}
	}

	/**
	 * Create a filter by providing the correct attributes
	 * @param <D>
	 * @param name
	 * @param attributes
	 * @return
	 */
	public static <D extends IDescriptor> IAttributeFilter<D> createFilter( Filters name, Map<String, String> attributes){
		IAttributeFilter<D> filter = null;
		AttributeFilter.Rules rule = AttributeFilter.Rules.valueOf( StringStyler.styleToEnum( attributes.get(Attributes.RULES.toString())));
		String ref = attributes.get(Attributes.REFERENCE.toString());
		String value = attributes.get(Attributes.VALUE.toString());
		switch( name) {
		case ATTRIBUTE_LIST:
			filter = new AttributeFilter<D>( rule, ref, value );
			break;
		default:
			filter = new AttributeFilter<D>( rule, ref, value );
			break;
		}
		return filter;
	}

}
