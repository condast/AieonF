package org.aieonf.sketch.service;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.function.IDescribablePredicate;
import org.condast.aieonf.osgi.concept.IDomainProvider;
import org.osgi.service.component.annotations.Component;

@Component( name="org.aieonf.sketch.domain.provider.service",
immediate=true)
public class DomainProvider implements IDomainProvider {

		
	@Override
	public IDomainAieon getDomain(ILoginUser user) {
		return getDomain();
	}
	
	public static IDomainAieon getDomain() {
		IDomainAieon domain = null;
		try{
			domain = null;
		}
		catch( Exception e ){
			e.printStackTrace();
		}
		return domain;		
	}

	/**
	 * Get the predicates that have been registered
	 * @return
	 */
	@Override
	public IDescribablePredicate<IDescriptor> getPredicates(){
		// TODO Auto-generated method stub
		return null;
	}
}