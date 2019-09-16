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

import javax.persistence.EntityManager;

//import javax.persistence.EntityManager;

public interface IPersistenceService{
	
	public static final String S_ERR_NO_SERVICE_FOUND = " Service was not found. The persistency bundle may not be initialised correctly";
	public static final String S_ERR_SERVICE_ALREADY_OPEN = " Service is already open. The workflow may not be correct";

	public String getName();

	public String getId(); 

	/**
	 * Returns true if the service is connected to the entity manager
	 * @return
	 */
	public boolean isEnabled();

	/**
	 * Connect the service. this creates an entity manager that can be used by 
	 * the system for SQL
	 * @return
	 */
	public void connect();

	/**
	 * Returns true if the service is open
	 * @return
	 */
	public boolean isConnected();

	/**
	 * Close the service
	 */
	public void disconnect();

	/**
	 * Get the entity manager
	 * @return
	 */
	public EntityManager getManager();

	/**
	 * Get the entity manager
	 * @return
	 */
	//public EntityManager getManager();
	
	void addListener(IPersistenceServiceListener persistencyServiceListener);

	void removeListener(IPersistenceServiceListener persistencyServiceListener);
}
