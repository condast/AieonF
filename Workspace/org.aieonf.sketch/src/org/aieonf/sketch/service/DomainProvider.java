package org.aieonf.sketch.service;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.function.IDescribablePredicate;
import org.aieonf.sketch.factory.SketchFactory;
import org.aieonf.sketch.factory.SketchModelFactory;
import org.condast.aieonf.osgi.concept.IDomainProvider;
import org.osgi.service.component.annotations.Component;

@Component( name="org.aieonf.sketch.domain.provider.service",
immediate=true)
public class DomainProvider implements IDomainProvider {

	SketchFactory factory = SketchFactory.getInstance();
		
	@Override
	public IDomainAieon[] getDomains(ILoginUser user) {
		Map<IDomainAieon, SketchModelFactory> factories = new HashMap<>();
		try {
			factories = factory.getFactories();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return factories.keySet().toArray( new IDomainAieon[factories.keySet().size()]);
	}
	
	public static IDomainAieon getDomain() {
		IDomainAieon domain = null;
		try{
			SketchFactory factory = SketchFactory.getInstance();
			factory.createTemplate();
			domain = factory.getDomain();
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