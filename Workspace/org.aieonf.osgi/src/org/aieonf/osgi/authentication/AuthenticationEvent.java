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
package org.aieonf.osgi.authentication;

import java.util.EventObject;

import org.aieonf.osgi.authentication.IAuthenticationListener.AuthenticationEvents;

public class AuthenticationEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private AuthenticationEvents event;
	
	private ILoginUser user;
	
	private String password;

	public AuthenticationEvent(Object arg0, AuthenticationEvents event) {
		super(arg0);
		this.event = event;
	}

	public AuthenticationEvent(Object arg0, AuthenticationEvents event, ILoginUser user ) {
		this(arg0, event );
		this.user = user;
	}

	public AuthenticationEvent(Object arg0, AuthenticationEvents event, ILoginUser user, String password ) {
		this(arg0, event );
		this.user = user;
		this.password = password;
	}

	public AuthenticationEvents getEvent(){
		return this.event;
	}
	
	public ILoginUser getUser(){
		return this.user;
	}

	public String getPassword() {
		return password;
	}
}
