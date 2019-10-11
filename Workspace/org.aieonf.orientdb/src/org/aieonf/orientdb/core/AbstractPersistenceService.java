package org.aieonf.orientdb.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import org.aieonf.commons.persistence.service.IPersistenceService;
import org.aieonf.commons.persistence.service.IPersistenceServiceListener;
import org.aieonf.commons.persistence.service.PersistencyServiceEvent;
import org.aieonf.commons.persistence.service.ServiceConnectionException;

import org.aieonf.commons.persistence.service.IPersistenceServiceListener.Services;

/**
 * This utility class is provided with the example to execute the JDBC code that is 
 * required as part of the example.  It provides a main method that can be used to run the code
 * outside of OSGi and several methods that actually populate the DB.
 * 
 * @author keesp
 *
 */
public abstract class AbstractPersistenceService implements IPersistenceService{

	protected static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	protected static final String S_LOCAL = "plocal:";
	protected static final String S_FILE = "file:";	
	protected static final String S_ROOT = "Root";

	private String id;
	private String name;
	private boolean connected;
	
	private List<IPersistenceServiceListener> listeners;

	private Lock lock;
		
	private final Logger logger = Logger.getLogger( this.getClass().getCanonicalName());

	protected AbstractPersistenceService( String id, String name ) {
		this.id = id;
		this.name = name;
		this.connected = false;
		lock = new ReentrantLock();
		listeners = new ArrayList<IPersistenceServiceListener>();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * returns true if the service is connected
	 * @return
	 */
	@Override
	public boolean isEnabled(){
		return true;  
	}

	protected abstract boolean onConnect();
	protected abstract boolean onDisconnect();
	
	@Override
	public synchronized void connect() {
		if( !this.isEnabled() )
			throw new ServiceConnectionException( "The " + this.name + S_ERR_NO_SERVICE_FOUND );
		if( this.connected )
			return;	
		lock.lock();
		try{
			logger.info("CONNECTING Manager " + name );
			connected = onConnect();
			logger.info("Manager CONNECTED " + name + ": " + connected );
			notifyListeners( Services.OPEN);

		}catch( Exception ex ){
			ex.printStackTrace();
		}
		finally{
			lock.unlock();
		}
	}

	@Override
	public boolean isConnected() {
		return this.connected;
	}

	/**
	 * Returns true if the service is open, and throws an exception otherwise
	 * @return
	 */
	public boolean checkOpen(){
		if( !connected )
			throw new ServiceConnectionException( ServiceConnectionException.S_ERR_SERVICE_NOT_OPENED );
		return this.connected;
	}

	@Override
	public synchronized void disconnect() {
		if( !this.connected )
			return;
		lock.lock();
		try{
			this.connected = onDisconnect();
			logger.info("DISCONNECTING Manager  " + name + ": ");
		}
		finally{
			lock.unlock();
		}
		this.notifyListeners( Services.CLOSE);
	}	

	/**
	 * Reopen the service. This is needed after an external commit, for instance when persisting a new object
	 */
	public void reopen() {
		this.connected = false;
		this.connect();
	}	
	
	@Override
	public void addListener(
			IPersistenceServiceListener persistencyServiceListener) {
		lock.lock();
		try{
			listeners.add(persistencyServiceListener);
		}
		finally{
			lock.unlock();
		}
	}

	@Override
	public synchronized void removeListener(
			IPersistenceServiceListener persistencyServiceListener) {
		lock.lock();
		try{
			listeners.remove(persistencyServiceListener);
		}
		finally{
			lock.unlock();
		}
	}

	protected synchronized void notifyListeners( Services action) {
		lock.lock();
		try{
			for (IPersistenceServiceListener l : listeners) {
				l.notifyServiceChanged( new PersistencyServiceEvent( this, action ));
			}
		}
		finally{
			lock.unlock();
		}
	}
	
	public void shutdown() {
	}
}