package org.aieonf.orientdb.cache;

import java.io.File;

import javax.persistence.EntityManager;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.concept.security.IPasswordAieon;
import org.aieonf.concept.security.PasswordAieon;
import org.aieonf.model.search.ModelScanner;
import org.aieonf.orientdb.core.AbstractPersistenceService;
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
public class CachePersistenceService extends AbstractPersistenceService {
	
	public static final String S_IDENTIFIER = "documenttxModel";
	
	protected static final String S_CACHE = "Cache";
	protected static final String S_DESCRIPTORS = "Descriptors";

	private String source;
	private IDomainAieon domain;
	private OrientGraphFactory factory;
	private ITemplateLeaf<IContextAieon> template;
	private ODatabaseDocumentTx database;

	private static CachePersistenceService service = new CachePersistenceService();
	
	private CachePersistenceService() {
		super( S_IDENTIFIER, S_CACHE );
		OrientDBFactory factory = OrientDBFactory.getInstance();
		template=  factory.createTemplate();
		domain = factory.getDomain();
	}

	public static CachePersistenceService getInstance() {
		return service;
	}
	
	public IDomainAieon getDomain() {
		return domain;
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
		loader.setIdentifier( S_CACHE );
		File file = ProjectFolderUtils.getDefaultUserFile( loader, true); 
		source = file.toURI().toString();
		source = source.replace( S_FILE, S_LOCAL);		
		IPasswordAieon password = new PasswordAieon( loader );
		factory = new OrientGraphFactory(source, password.getUserName(), password.getPassword() ).setupPool(1, 10);	
		database = factory.getDatabase();
		if( !database.exists())
			database = database.create();
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