/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons.filter.xml;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;

public interface IFilterBuilderListener<M extends Object> {
		
	/**
	 * The keys of attributes with special meaning
	 * @author Kees
	 *
	 */
	public enum FilterNodes{
		FILTERS,
		FILTER,
		CHAIN;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
		
		/**
		 * Returns true if the given string is a model attribute
		 * @param attr
		 * @return
		 */
		public static boolean isNode( String attr ){
			String str = StringStyler.styleToEnum( attr );
			if( StringUtils.isEmpty(str))
				return false;
			for( FilterNodes ma: FilterNodes.values() ){
				if( str.equals( ma.name()))
					return true;
			}
			return false;
		}
	}

	/**
	 * The keys of attributes with special meaning
	 * @author Kees
	 *
	 */
	public enum FilterAttributes{
		ID,
		TYPE,
		RULE,
		REFERENCE,
		VALUE;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
		
		/**
		 * Returns true if the given string is a model attribute
		 * @param attr
		 * @return
		 */
		public static boolean isAttribute( String attr ){
			String str = StringStyler.styleToEnum( attr );
			if( StringUtils.isEmpty(str))
				return false;
			for( FilterAttributes ma: FilterAttributes.values() ){
				if( str.equals( ma.name()))
					return true;
			}
			return false;
		}
	}

	/**
	 * The keys of attributes with special meaning
	 * @author Kees
	 *
	 */
	public enum FilterTypes{
		AND,
		OR,
		ATTRIBUTE,
		WILDCARD;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
		
		/**
		 * Returns true if the given string is a model attribute
		 * @param attr
		 * @return
		 */
		public static boolean isType( String attr ){
			String str = StringStyler.styleToEnum( attr );
			if( StringUtils.isEmpty(str))
				return false;
			for( FilterTypes ma: FilterTypes.values() ){
				if( str.equals( ma.name()))
					return true;
			}
			return false;
		}
	}

	/**
	 * Notify relevant events duribng the build process 
	 * @param event
	 */
	public void notifyChange( FilterBuilderEvent<M> event );
}
