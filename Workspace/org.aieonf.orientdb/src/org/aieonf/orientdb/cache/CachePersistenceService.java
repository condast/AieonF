package org.aieonf.orientdb.cache;

import org.aieonf.concept.security.IPasswordAieon;
import org.aieonf.concept.security.PasswordAieon;
import org.aieonf.orientdb.core.AbstractPersistenceService;

import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

/**
 * Handles the Orient Databae
 * @See :https://orientdb.com/docs/2.2/documenttx-Database-Tinkerpop.html
 * @author Kees
 *
 * @param <D>
 * @param <Descriptor>
 */
public class CachePersistenceService extends AbstractPersistenceService {
	
	protected static final String S_CACHE = "Cache";

	private ODatabaseDocumentTx database;

	private static CachePersistenceService cache = new CachePersistenceService();
	
	private CachePersistenceService() {
		super( Types.DOCUMENT, S_CACHE );
	}
	
	public static CachePersistenceService getInstance() {
		return cache;
	}
	
	/**
	 * Create a database
	 * @return
	 */
	public ODatabaseDocumentTx getDatabase() {
		if(!isConnected())
			return null;
		return database; 
	}
	
	@Override
	protected boolean onConnect() {
		boolean result = false;
		try {
			String source = super.getSource();
			IPasswordAieon password = new PasswordAieon( getLoader() );
			database = new ODatabaseDocumentTx(source);
			if( !database.exists())
				database.create(); 
			database = database.open(password.getUserName(), password.getPassword());
			database.activateOnCurrentThread();
			result = true;
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	protected boolean onDisconnect() {
		this.database.close();
		return false;
	}
	
	public boolean open() {
		boolean result = false;
		if(!isConnected())
			return result;
		try {
			String source = super.getSource();
			IPasswordAieon password = new PasswordAieon( getLoader() );
			OPartitionedDatabasePool pool =  new OPartitionedDatabasePool(source , password.getUserName(), password.getPassword() );
			database = pool.acquire();
			if (!database.isActiveOnCurrentThread()) {
				database.activateOnCurrentThread();
			}
			result = true;
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public void close() {
		database.close();
	}
}