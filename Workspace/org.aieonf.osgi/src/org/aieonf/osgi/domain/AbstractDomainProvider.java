package org.aieonf.osgi.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.domain.IDomainProvider;
import org.aieonf.template.context.IProviderContextFactory;

public abstract class AbstractDomainProvider<C extends IContextAieon, U extends IDescribable<IDescriptor>> implements IDomainProvider{

	private Collection<IDomainAieon> domains;
	
	protected AbstractDomainProvider() {
		super();
		domains = new ArrayList<>();
		init();
	}

	protected abstract IProviderContextFactory<C, IDomainAieon, String, U> getFactory();
	
	public void init( ) {
		try{
			IProviderContextFactory<C, IDomainAieon, String, U> factory = getFactory();
			factory.createTemplate();
			domains.add( factory.getDomain());
		}
		catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	@Override
	public IDomainAieon getDomain( String userName, String password, String domain ) {
		if( StringUtils.isEmpty(domain))
			return null;
		for( IDomainAieon aieon: domains ) {
			if( domain.equals(aieon.getDomain() ))
				return aieon;				
		}
		return null;
	}

	@Override
	public Map<String,String> getDomains() {
		Map<String,String> results = new HashMap<>();
		for( IDomainAieon aieon: domains ) {
			results.put(aieon.getDomain(),aieon.getShortName() );
		}
		return results;
	}
	
	@Override
	public String[] getDomainNames() {
		Collection<String> results = new ArrayList<>();
		for( IDomainAieon aieon: domains ) {
			results.add(aieon.getShortName() );
		}
		return results.toArray( new String[ results.size()]);
	}
	
}