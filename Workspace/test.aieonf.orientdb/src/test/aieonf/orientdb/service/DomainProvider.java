package test.aieonf.orientdb.service;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.function.IDescribablePredicate;
import org.aieonf.osgi.domain.IDomainProvider;
import org.osgi.service.component.annotations.Component;

import test.aieonf.orientdb.core.TestFactory;

@Component( name="test.aionf.orientdb.service",
immediate=true)
public class DomainProvider implements IDomainProvider {

	public DomainProvider() {
	}
		
	@Override
	public IDomainAieon[] getDomains( ILoginUser user) {
		IDomainAieon[] domains = new IDomainAieon[1];
		domains[0] = getDomain();
		return domains;
	}

	public static IDomainAieon getDomain() {
		IDomainAieon domain = null;
		try{
			TestFactory factory = TestFactory.getInstance();
			factory.createTemplate();
			domain = factory.getDomain();
		}
		catch( Exception e ){
			e.printStackTrace();
		}
		return domain;
	}

	/**
	 * Register the supported predicates
	 * @return
	 */
	@Override
	public IDescribablePredicate<IDescriptor> getPredicates(){
		TestFactory factory = TestFactory.getInstance();
		return factory.createPredicates();
	}
}