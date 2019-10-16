package org.aieonf.orientdb.core;

import org.aieonf.orientdb.core.AbstractPersistenceService;

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
		String source = super.getSource();
		database = new ODatabaseDocumentTx( source );
		return true;
	}

	@Override
	protected boolean onDisconnect() {
		this.database.close();
		return true;
	}
}