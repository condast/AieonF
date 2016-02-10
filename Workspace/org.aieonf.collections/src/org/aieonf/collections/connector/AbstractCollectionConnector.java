package org.aieonf.collections.connector;

import org.aieonf.collections.CollectionException;
import org.aieonf.collections.IAccessible;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.loader.ILoaderAieon;

public abstract class AbstractCollectionConnector<T extends IDescribable<?>> implements ICollectionConnector<T>
{

	private boolean create;
	private boolean connected;
	private IAccessible collection;

	/**
   * The loader aieon contains info for the manifest
   */
  private ILoaderAieon loader;

	protected AbstractCollectionConnector( ILoaderAieon loader, boolean create )
	{
		this.create = create;
		this.connected = false;
		this.loader = loader;
	}

	public AbstractCollectionConnector( ILoaderAieon loader, IAccessible collection, boolean create )
	{
		this( loader, create );
		this.collection = collection;
	}
	
	/**
	 * In initialise the connector. If all goes well, then a connection has been established
	 * @throws CollectionException
	 */
	@Override
	public void connect() throws ConnectionException{
    if( !( this.collection instanceof IAccessible )){
    	this.connected = true;
    	return;
    }
    IAccessible accessable = this.collection;   
    try {
			accessable.prepare(loader);
		}
		catch (CollectionException e) {
			throw new ConnectionException( e );
		}
    this.connected = accessable.canAccess();		
	}
	
  /**
   * Get the loader
   * @return LoaderAieon
  */
  protected ILoaderAieon getLoader()
  {
    return this.loader;
  }

  /**
	 * @return the create
	 */
	protected boolean isCreateable()
	{
		return create;
	}

	/**
	 * @return the connected
	 */
	@Override
	public boolean isConnected()
	{
		return connected;
	}

	/**
	 * @param connected the connected to set
	 */
	protected void setConnected(boolean connected)
	{
		if( this.collection != null )
			this.connected = connected;
	}
	
	@Override
	public void disconnect()
	{
		this.connected =  false;
	}

	/**
	 * @return the collection
	 */
	@Override
	public IAccessible getAccessible()
	{
		return collection;
	}


	/**
	 * @param collection the collection to set
	 */
	protected void setAccessible(IAccessible collection)
	{
		this.collection = collection;
	}  
}