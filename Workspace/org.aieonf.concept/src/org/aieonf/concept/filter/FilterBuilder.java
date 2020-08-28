/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.concept.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.aieonf.commons.Utils;
import org.aieonf.commons.filter.FilterChain;
import org.aieonf.commons.filter.IAttributeFilter;
import org.aieonf.commons.filter.IFilter;
import org.aieonf.commons.filter.WildcardFilter;
import org.aieonf.commons.filter.IFilter.Mode;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.commons.xml.AbstractSimpleXmlHandler;
import org.aieonf.commons.xml.AbstractXMLBuilder;
import org.aieonf.concept.IDescribable;
import org.xml.sax.Attributes;

public class FilterBuilder<D extends IDescribable> extends AbstractXMLBuilder<IFilter<D>,  String> {

	public static final String S_DEFAULT_FILTER_CONFIG = "filter.xml";
	
	public static final String S_WRN_NO_DATA_PROVIDED = "The test event does not contain data for test: ";

	public enum Nodes{
		FILTERS,
		FILTER,
		FILTER_CHAIN;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString(name());
		}
		
		public static boolean isValid( String str ) {
			if( StringUtils.isEmpty(str))
				return false;
			for( Nodes node: values()) {
				if( node.name().equals(str))
					return true;
			}
			return false;
		}
	}
	
	private enum AttributeNames{
		ID,
		NAME,
		RULE,
		TYPE,
		MODE,
		AMOUNT,
		REFERENCE;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	private enum FilterTypes{
		CHAIN,
		ATTRIBUTE,
		WILDCARD;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	private String name;

	public FilterBuilder( Class<?> clss) {
		this( S_DEFAULT_FILTER_CONFIG, clss );
	}
	
	public FilterBuilder( String name, Class<?> clss ) {
		super( new FilterHandler<D>(), clss.getResourceAsStream(name) );
	}
	
	public FilterBuilder( Class<?> clss, String name, String resource ) {
		super( new FilterHandler<D>(), clss.getResourceAsStream( resource ));
		this.name = name;
	}
	

	public String getName() {
		return name;
	}

	private static class FilterHandler<D extends IDescribable> extends AbstractSimpleXmlHandler<IFilter<D>, String>{
		
		private Map<String, IFilter<D>> filters;
		
		private Stack<IFilter<D>> current;
		
		private FilterHandler() {
			super();
			current = new Stack<>();
			filters = new HashMap<>();
		}

		@Override
		protected IFilter<D> parseNode( String identifier, Attributes attributes) {
			String id = getAttribute( attributes, AttributeNames.ID );
			String name = getAttribute( attributes, AttributeNames.NAME );
			String type_str = StringStyler.styleToEnum( getAttribute( attributes, AttributeNames.TYPE ));			
			FilterTypes type = StringUtils.isEmpty(type_str)? FilterTypes.WILDCARD: FilterTypes.valueOf( StringStyler.styleToEnum( getAttribute( attributes, AttributeNames.TYPE )));

			
			String rule_str = StringStyler.styleToEnum( getAttribute( attributes, AttributeNames.RULE ));
			String reference = StringStyler.styleToEnum( getAttribute( attributes, AttributeNames.REFERENCE ));
			String amountstr = getAttribute( attributes, AttributeNames.AMOUNT );
			int amount = StringUtils.isEmpty(amountstr)?-1:Integer.parseInt(amountstr);
			String modestr =getAttribute( attributes, AttributeNames.MODE );
			IFilter.Mode mode = StringUtils.isEmpty(modestr)? Mode.ENABLED:Mode.valueOf( StringStyler.styleToEnum( modestr));			
			if( !Nodes.isValid(identifier))
				return null;

			Nodes node = Nodes.valueOf(identifier);
			IFilter<D> filter = null;
			IFilter<D> parent = Utils.assertNull(current)? null: current.peek();
			switch( node) {
			case FILTERS:
				break;
			case FILTER:
				switch( type ) {
				case WILDCARD:
					break;
				case ATTRIBUTE:
					AttributeFilter.Rules rule = StringUtils.isEmpty(rule_str)? AttributeFilter.Rules.EQUALS: AttributeFilter.Rules.valueOf(StringStyler.styleToEnum(rule_str));
					filter = new AttributeFilter<D>(rule, name, reference, WildcardFilter.S_ALL);
					filter.setMode(mode);
					filter.setAmount(amount);
					break;
				default:
					break;
				}
				current.push(filter);
				break;
			case FILTER_CHAIN:
				FilterChain.Rules rule = FilterChain.Rules.valueOf(StringStyler.styleToEnum(rule_str));
				filter = new FilterChain<D>(rule );
				break;
			default: 
				break;
			}
			if( filter == null )
				return filter;
			
			if( !StringUtils.isEmpty(id))
				filters.put(id, filter);
			current.push(filter);
			if(( parent != null ) && ( parent instanceof FilterChain )) {
				FilterChain<D> chain = (FilterChain<D>) parent;
				chain.addFilter(filter);
			}
			return filter;
		}

		@Override
		protected void completeNode(String identifier) { 
			if( !Nodes.isValid(identifier))
				current.pop();
		}
		
		@Override
		protected void addValue(String identifier, String value) { 
			IFilter<D> filter = current.peek();
			if( filter instanceof IAttributeFilter) {
				IAttributeFilter<D> af = (IAttributeFilter<D>) filter;
				af.setValue( value );
			}
		}

		@Override
		public IFilter<D> getUnit(String id) {
			return filters.get(id);
		}

		@Override
		protected String getNode(String name) {
			return name;
		}

		@SuppressWarnings("unchecked")
		@Override
		public IFilter<D>[] getUnits() {
			return filters.values().toArray( new IFilter[ filters.size()]);
		}	
	}
}