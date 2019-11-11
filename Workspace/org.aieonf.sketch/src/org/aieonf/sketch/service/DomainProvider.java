package org.aieonf.sketch.service;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.concept.domain.IDomainAieon;
import org.condast.aieonf.osgi.concept.IDomainProvider;
import org.osgi.service.component.annotations.Component;

@Component( name="org.aieonf.sketch.domain.provider.service",
immediate=true)
public class DomainProvider implements IDomainProvider {

		
	@Override
	public IDomainAieon getDomain(ILoginUser user) {
		IDomainAieon domain = null;
		try{
			domain = null;
		}
		catch( Exception e ){
			e.printStackTrace();
		}
		return domain;
	}
}