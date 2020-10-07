package org.aieonf.orientdb.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.aieonf.commons.persistence.service.IPersistenceService;
import org.aieonf.commons.persistence.service.IPersistenceServiceListener;
import org.aieonf.commons.persistence.service.PersistencyServiceEvent;
import org.aieonf.commons.persistence.service.ServiceConnectionException;

import org.aieonf.commons.persistence.service.IPersistenceServiceListener.Services;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.model.search.ModelScanner;
import org.aieonf.orientdb.factory.OrientDBFactory;
import org.aieonf.template.def.ITemplateLeaf;

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

	public enum Types{
		DOCUMENT,
		GRAPH,
		OBJECT;

		@Override
		public String toString() {
			String result = super.toString();
			switch( this ){
			case DOCUMENT:
				result = "documenttxModel";
				break;
			default:
				break;
			}
			return result;
		}
	}

	private String id;
	private String name;
	private boolean connected;
	
	private String source;
	private ILoaderAieon loader;
	private IDomainAieon domain;
	private ITemplateLeaf<IContextAieon> template;

	private List<IPersistenceServiceListener> listeners;

	private final Logger logger = Logger.getLogger( this.getClass().getCanonicalName());

	protected AbstractPersistenceService( Types type, String name ) {
		this.id = type.toString();
		this.name = name;
		this.connected = false;
		OrientDBFactory factory = OrientDBFactory.getInstance();
		template=  factory.createTemplate();
		domain = factory.getDomain();
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

	protected String getSource() {
		return source;
	}

	public ILoaderAieon getLoader() {
		return loader;
	}

	protected IDomainAieon getDomain() {
		return domain;
	}
	
	protected void setDomain(IDomainAieon domain) {
		this.domain = domain;
	}

	
	protected abstract boolean onConnect();
	protected abstract boolean onDisconnect();
	
	@Override
	public synchronized void connect() {
		if( !this.isEnabled() )
			throw new ServiceConnectionException( "The " + this.name + S_ERR_NO_SERVICE_FOUND );
		if( this.connected )
			return;	
		try{
			logger.info("CONNECTING Manager " + name );
			ModelScanner<IContextAieon> search = new ModelScanner<IContextAieon>( template );
			loader = new LoaderAieon( search.getDescriptors( ILoaderAieon.Attributes.LOADER.toString())[0]);
			loader.set( IConcept.Attributes.SOURCE.name(), S_BUNDLE_ID);
			loader.setIdentifier( name);
			File file = ProjectFolderUtils.getDefaultUserFile( loader, true);
			file.setReadable(true);
			file.setWritable(true);
			source = file.toURI().toString();
			source = source.replace( S_FILE, S_LOCAL);		
			connected = onConnect();
			logger.info("Manager CONNECTED " + name + ": " + connected );
			notifyListeners( Services.OPEN);

		}catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	@Override
	public EntityManager getManager() {
		//if( this.factory == null )
		return null;
		//return//database.getnull;
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
		this.connected = onDisconnect();
		logger.info("DISCONNECTING Manager  " + name + ": ");
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
		listeners.add(persistencyServiceListener);
	}

	@Override
	public synchronized void removeListener(
			IPersistenceServiceListener persistencyServiceListener) {
		listeners.remove(persistencyServiceListener);
	}

	protected synchronized void notifyListeners( Services action) {
		for (IPersistenceServiceListener l : listeners) {
			l.notifyServiceChanged( new PersistencyServiceEvent( this, action ));
		}
	}
}