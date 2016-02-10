package org.aieonf.collections.connector;

import org.aieonf.collections.IAccessible;
import org.aieonf.concept.IDescribable;

public interface ICollectionConnector<T extends IDescribable<?>>
{
	public boolean exists();
	
	public void connect() throws ConnectionException;
	
	public boolean isConnected();

	public IAccessible getAccessible();
	
	public void disconnect();
}
