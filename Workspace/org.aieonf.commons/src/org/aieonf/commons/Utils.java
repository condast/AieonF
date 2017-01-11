/*******************************************************************************
 * Copyright (c) 2014, 2016 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons;

import java.util.Collection;

public class Utils
{

	/**
	 * returns true if the string is null or empty
	 * @param str
	 * @return
	 * @deprecated Use assertNull
	 */
	public static final boolean isNull( String str ){
		return (( str == null) || ( str.trim().length() == 0 ));
	}

	/**
	 * returns true if the string is null or empty
	 * @param str
	 * @return
	 */
	public static final boolean assertNull( String str ){
		return (( str == null) || ( str.trim().length() == 0 ));
	}

	/**
	 * Returns true if the collection is null or empty
	 * @param collection
	 * @return
	 * @deprecated Use assertNull
	 */
	public static boolean isNull( Collection<?> collection ){
		return (( collection == null ) || ( collection.isEmpty() )); 
	}

	/**
	 * Returns true if the collection is null or empty
	 * @param collection
	 * @return
	 */
	public static boolean assertNull( Collection<?> collection ){
		return (( collection == null ) || ( collection.isEmpty() )); 
	}

	/**
	 * Returns true if the collection is null or empty
	 * @param collection
	 * @return
	 * @deprecated Use assertNull
	 */
	public static boolean isNull( Object[] collection ){
		return (( collection == null ) || ( collection.length == 0 )); 
	}

	/**
	 * Returns true if the collection is null or empty
	 * @param collection
	 * @return
	 */
	public static boolean assertNull( Object[] collection ){
		return (( collection == null ) || ( collection.length == 0 )); 
	}

}
