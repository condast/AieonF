package org.aieonf.orientdb.db;

import javax.persistence.EntityManager;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.orientdb.core.AbstractPersistenceService;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

/**
 * Handles the Orient Databae
 * @See :https://orientdb.com/docs/2.2/documenttx-Database-Tinkerpop.html
 * @author Kees
 *
 * @param <D>
 * @param <Descriptor>
 */
public class DatabasePersistenceService extends AbstractPersistenceService {
	
	protected static final String S_AIEONF = "AieonF";

	private OrientGraphFactory factory;
	private OrientGraph database;

	private static DatabasePersistenceService service = new DatabasePersistenceService();
	
	private DatabasePersistenceService() {
		super( Types.GRAPH, S_AIEONF );
	}

	public static DatabasePersistenceService getInstance() {
		return service;
	}
	
	public void setDomain(IDomainAieon domain) {
		IDomainAieon current = super.getDomain();
		if(( current != null ) && ( current.equals(domain) ))
			return;
		disconnect();
		super.setDomain(domain);
		if( domain != null )
			connect();
	}

	@Override
	public EntityManager getManager() {
		//if( this.factory == null )
		return null;
		//return//database.getnull;
	}

	/**
	 * Create a database
	 * @return
	 */
	public OrientGraph getDatabase() {
		if(!isConnected())
			return null;
		return database; 
	}
	
	@Override
	protected boolean onConnect() {
		String source = super.getSource();
		factory = new OrientGraphFactory(source);
		//factory = new OrientGraphFactory(source, user.getUserName(), String.valueOf(user.getToken() )).setupPool(1, 10);	
		database = factory.getTx();
		return true;
	}

	@Override
	protected boolean onDisconnect() {
		if( database != null )
			database.shutdown();

		boolean result = ( factory != null );
		if( result )
			factory.close();
		return result;
	}
}