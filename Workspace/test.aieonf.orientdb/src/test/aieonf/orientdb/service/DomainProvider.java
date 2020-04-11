package test.aieonf.orientdb.service;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.concept.domain.IDomainAieon;
import org.condast.aieonf.osgi.concept.IDomainProvider;
import org.osgi.service.component.annotations.Component;

import test.aieonf.orientdb.core.TestFactory;

@Component( name="test.aionf.orientdb.service",
immediate=true)
public class DomainProvider implements IDomainProvider {

	public DomainProvider() {
	}
		
	@Override
	public IDomainAieon getDomain( ILoginUser user) {
		return getDomain();
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

}