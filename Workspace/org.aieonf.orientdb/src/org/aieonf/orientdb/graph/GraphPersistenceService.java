package org.aieonf.orientdb.graph;

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
public class GraphPersistenceService extends AbstractPersistenceService {
	
	public static final String S_IDENTIFIER = "graphtxModel";
	
	protected static final String S_GRAPH = "Graph";
	protected static final String S_DESCRIPTORS = "Descriptors";

	private String source;
	private IDomainAieon domain;
	private OrientGraphFactory factory;
	private ITemplateLeaf<IContextAieon> template;
	private OrientGraph graph;

	private GraphPersistenceService( IDomainAieon domain ) {
		super( S_IDENTIFIER, S_GRAPH );
		OrientDBFactory factory = OrientDBFactory.getInstance();
		template=  factory.createTemplate();
		this.domain = domain;
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
	public OrientGraph getGraph() {
		if(!isConnected())
			return null;
		return graph; 
	}
	
	@Override
	protected boolean onConnect() {
		ModelScanner<IContextAieon> search = new ModelScanner<IContextAieon>( template );
		ILoaderAieon loader = new LoaderAieon( search.getDescriptors( ILoaderAieon.Attributes.LOADER.toString())[0]);
		loader.set( IConcept.Attributes.SOURCE, S_BUNDLE_ID);
		loader.setIdentifier( S_GRAPH );
		File file = ProjectFolderUtils.getDefaultUserFile( loader, true); 
		source = file.toURI().toString();
		source = source.replace( S_FILE, S_LOCAL);		
		IPasswordAieon password = new PasswordAieon( loader );
		factory = new OrientGraphFactory(source, password.getUserName(), password.getPassword() ).setupPool(1, 10);	
		graph = factory.getTx();
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