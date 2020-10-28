package test.aieonf.orientdb.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import org.aieonf.commons.security.ILoginListener;
import org.aieonf.commons.security.ISecureGenerator;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.domain.IDomainListener;
import org.aieonf.concept.function.IDescribablePredicate;
import org.aieonf.osgi.selection.IActiveDomainProvider;

public class Dispatcher implements ISecureGenerator, IActiveDomainProvider
{
	private ISecureGenerator generator;
	
	private static Dispatcher dispatcher = new Dispatcher();
	
	private Collection<ILoginListener> listeners;

	private TestFactory factory = TestFactory.getInstance(); 

	private Dispatcher() {
		factory.createTemplate();
		this.listeners = new ArrayList<ILoginListener>();
	}

	/**
	 * Get an instance of the bar link service
	 * @return
	 */
	public static Dispatcher getInstance(){
		return dispatcher;
	}


	public ISecureGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(ISecureGenerator generator) {
		this.generator = generator;
	}

	@Override
	public Entry<Long, Long> createIdAndToken(String domain) {
		return this.generator.createIdAndToken(domain);
	}

	@Override
	public void addDomainListener(IDomainListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDomainListener(IDomainListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRegistered(long id, String name) {
		return true;
	}

	@Override
	public IDomainAieon getActiveDomain() {
		return factory.getDomain();
	}

	@Override
	public IDomainAieon getDomain(long id, long token, String name) {
		return factory.getDomain();
	}

	@Override
	public IDomainAieon[] getDomains() {
		IDomainAieon[] domains = new IDomainAieon[1];
		domains[0] = getActiveDomain();
		return domains;
	}

	@Override
	public IDescribablePredicate<IDescriptor> getPredicates() {
		return factory.createPredicates();
	}

	public void addLoginListener(ILoginListener listener) {
		this.listeners.add(listener);
	}

	public void removeLoginListener(ILoginListener listener) {
		this.listeners.remove(listener);
	}
	
	protected void notifyLoginEvent( LoginEvent event ){
		for( ILoginListener listener: this.listeners )
			listener.notifyLoginEvent(event);
	}
	
}
