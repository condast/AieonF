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

import org.aieonf.commons.strings.StringStyler;

public interface IAuthenticationListener{

	public enum AuthenticationEvents{
		CLEAR,
		REGISTER,
		LOGIN,
		LOGOUT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	public void notifyLoginChanged( AuthenticationEvent event );
}
