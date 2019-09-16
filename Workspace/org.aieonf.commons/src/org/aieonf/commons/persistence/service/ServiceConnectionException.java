/*******************************************************************************
 * Copyright (c) 2016 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Condast                - EetMee
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.aieonf.commons.persistence.service;

public class ServiceConnectionException extends RuntimeException {

	public static final String S_ERR_COULD_NOT_OPEN_CONNECTION = "Could not open the service, entity manasger not found: ";
	public static final String S_ERR_COULD_NOT_CLOSE_CONNECTION = "Could not close the service, entity manasger not found: ";
	public static final String S_ERR_SERVICE_NOT_OPENED = "The service is not open. Please do so first ";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceConnectionException() {
	}

	public ServiceConnectionException(String arg0) {
		super(arg0);
	}

	public ServiceConnectionException(Throwable arg0) {
		super(arg0);
	}

	public ServiceConnectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ServiceConnectionException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
