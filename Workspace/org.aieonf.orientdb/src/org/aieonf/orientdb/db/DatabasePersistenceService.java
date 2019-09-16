package org.aieonf.orientdb.db;

import java.io.File;

import javax.persistence.EntityManager;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.orientdb.core.PersistenceService;
import org.aieonf.orientdb.service.LoginDispatcher;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

/**
 * Handles the Orient Databae
 * @See :https://orientdb.com/docs/2.2/documenttx-Database-Tinkerpop.html
 * @author Kees
 *
 * @param <D>
 * @param <Descriptor>
 */
public class DatabasePersistenceService extends PersistenceService {
	
	public static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	public static final String S_IDENTIFIER = "documenttxModel";
	
	private static final String S_LOCAL = "plocal:";
	private static final String S_FILE = "file:";
	protected static final String S_ROOT = "Root";
	protected static final String S_CACHE = "Cache";
	protected static final String S_DESCRIPTORS = "Descriptors";

	private String source;
	private IDomainAieon domain;
	private OrientGraphFactory factory;
	private long userId;
	private ODatabaseDocumentTx database;

	private static LoginDispatcher login = LoginDispatcher.getInstance();

	public DatabasePersistenceService(IDomainAieon domain ) {
		super( S_IDENTIFIER, S_CACHE );
		this.domain = domain;
		this.userId = -1;
	}

	@Override
	public EntityManager getManager() {
		//if( this.factory == null )
		return null;
		//return//database.getnull;
	}

	/**
	 * Connect to the database
	 * 
	 * @param loader
	 */
	public void connect( long userId ){
		this.userId = userId;
		super.connect();
	}

	public ILoginUser getUser() {
		return login.getUser(userId);
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
		ILoaderAieon loader = new LoaderAieon( domain);
		loader.set( IConcept.Attributes.SOURCE, S_BUNDLE_ID);
		loader.setIdentifier( S_CACHE );
		File file = ProjectFolderUtils.getDefaultUserFile( loader, true); 
		source = file.toURI().toString();
		source = source.replace( S_FILE, S_LOCAL);		
		ILoginUser user = login.getUser(userId);
		if( user == null )
			factory = new OrientGraphFactory(source).setupPool(1, 10);
		else
			factory = new OrientGraphFactory(source, user.getUserName(), String.valueOf(user.getToken() )).setupPool(1, 10);	
		database = factory.getDatabase().create();
		return true;
	}

	@Override
	protected boolean onDisconnect() {
		boolean result = ( factory != null );
		if( result )
			factory.close();
		return result;
	}
}