/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.model.builder;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;

public interface IModelBuilderListener<M extends Object> {
	
	
	/**
	 * The keys of attributes with special meaning
	 * @author Kees
	 *
	 */
	public enum ModelAttributes{
		APPLICATION,
		MODELS,
		MODEL,
		CONTEXT,
		DESCRIPTOR,
		CHILDREN,
		LOCATION,
		PROPERTIES;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
		
		/**
		 * Returns true if the given string is a model attribute
		 * @param attr
		 * @return
		 */
		public static boolean isModelAttribute( String attr ){
			String str = StringStyler.styleToEnum( attr );
			if( StringUtils.isEmpty(str))
				return false;
			for( ModelAttributes ma: ModelAttributes.values() ){
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
	public void notifyChange( ModelBuilderEvent<M> event );
}
