package org.aieonf.orientdb.db;

import java.io.File;

import javax.persistence.EntityManager;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.model.search.ModelScanner;
import org.aieonf.orientdb.core.AbstractPersistenceService;
import org.aieonf.orientdb.core.Dispatcher;
import org.aieonf.orientdb.factory.OrientDBFactory;
import org.aieonf.template.def.ITemplateLeaf;

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
public class DatabasePersistenceService extends AbstractPersistenceService {
	
	public static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	public static final String S_IDENTIFIER = "documenttxModel";
	
	private static final String S_LOCAL = "plocal:";
	private static final String S_FILE = "file:";
	protected static final String S_ROOT = "Root";
	protected static final String S_AIEONF = "AieonF";
	protected static final String S_DESCRIPTORS = "Descriptors";

	private String source;
	private IDomainAieon domain;
	private OrientGraphFactory factory;
	private ITemplateLeaf<IContextAieon> template;
	private ODatabaseDocumentTx database;

	private static Dispatcher login = Dispatcher.getInstance();

	private static DatabasePersistenceService service = new DatabasePersistenceService();
	
	private DatabasePersistenceService() {
		super( S_IDENTIFIER, S_AIEONF );
		OrientDBFactory factory = OrientDBFactory.getInstance();
		template=  factory.createTemplate();
	}

	public static DatabasePersistenceService getInstance() {
		return service;
	}
	
	public IDomainAieon getDomain() {
		return domain;
	}

	public void setDomain(IDomainAieon domain) {
		if(( this.domain != null ) && ( this.domain.equals(domain) ))
			return;
		disconnect();
		this.domain = domain;
		if( this.domain != null )
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
	public ODatabaseDocumentTx getDatabase() {
		if(!isConnected())
			return null;
		return database; 
	}
	
	@Override
	protected boolean onConnect() {
		ModelScanner<IContextAieon> search = new ModelScanner<IContextAieon>( template );
		ILoaderAieon loader = new LoaderAieon( search.getDescriptors( ILoaderAieon.Attributes.LOADER.toString())[0]);
		loader.set( IConcept.Attributes.SOURCE, S_BUNDLE_ID);
		loader.setIdentifier(domain.getDomain() );
		File file = ProjectFolderUtils.getDefaultUserFile( loader, true); 
		source = file.toURI().toString();
		source = source.replace( S_FILE, S_LOCAL);		
		ILoginUser user = login.getUser();
		if( user == null )
			factory = new OrientGraphFactory(source).setupPool(1, 10);
		else
			factory = new OrientGraphFactory(source, user.getUserName(), String.valueOf(user.getToken() )).setupPool(1, 10);	
		database = factory.getDatabase();
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