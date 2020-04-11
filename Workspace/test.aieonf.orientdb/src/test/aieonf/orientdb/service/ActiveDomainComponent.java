package test.aieonf.orientdb.service;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.domain.IDomainListener;
import org.aieonf.osgi.selection.IActiveDomainProvider;
import org.osgi.service.component.annotations.Component;

import test.aieonf.orientdb.core.Dispatcher;

@Component( name=ActiveDomainComponent.S_SAIGHT_SELECTION_ID, immediate=true)
public class ActiveDomainComponent implements IActiveDomainProvider {

	public static final String S_SAIGHT_SELECTION_ID = "test.aironf.orientdb.domain.service";
	
	Dispatcher dispatcher = Dispatcher.getInstance();
	
	public void activate(){/* nothing */}
	public void deactivate(){/* nothing */}
	
	
	@Override
	public void addDomainListener(IDomainListener listener) {
		dispatcher.addDomainListener(listener);
	}
	@Override
	public void removeDomainListener(IDomainListener listener) {
		dispatcher.removeDomainListener(listener);
	}
	
	@Override
	public boolean isRegistered(long id, String name) {
		return dispatcher.isRegistered(id, name);
	}

	@Override
	public IDomainAieon getDomain(long id, long token, String name) {
		return dispatcher.getDomain(id, token, name);
	}
	
	@Override
	public IDomainAieon getActiveDomain() {
		return dispatcher.getActiveDomain();
	}
	
	@Override
	public IDomainAieon[] getDomains() {
		return dispatcher.getDomains();
	}
}