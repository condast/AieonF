package test.aieonf.orientdb.service;

import org.aieonf.commons.security.AbstractSecureProvider;
import org.aieonf.commons.security.ISecureCall;
import org.aieonf.commons.security.ISecureGenerator;
import org.osgi.service.component.annotations.Component;

@Component( name="test.aieonf.orientdb.secure.provider",
			immediate=true)
public class SecureRestProvider extends AbstractSecureProvider implements ISecureCall, ISecureGenerator{

	public SecureRestProvider() {
		super();
	}

	@Override
	protected long createId(String domain) {
		return domain.hashCode();
	}

	@Override
	protected long createToken(String domain) {
		return domain.hashCode();
	}

}