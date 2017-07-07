package org.aieonf.orientdb.factory;

import java.io.File;
import java.net.URI;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.wrapper.ConceptWrapper;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.template.context.AbstractProviderContextFactory;

public class OrientDBFactory extends AbstractProviderContextFactory<IContextAieon, IModelLeaf<IDescriptor>>{

	private static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	
	public static final String S_AIEONF_INF = "AIEONF-INF";
	
	private static OrientDBFactory factory = new OrientDBFactory();
	
	private OrientDBFactory() {
		super( S_BUNDLE_ID, OrientDBFactory.class );
	}
	
	public static final OrientDBFactory getInstance(){
		return factory;
	}

	public File getOrientDBRoot(){
		return ProjectFolderUtils.getDefaultRootFolder( S_BUNDLE_ID );	
	}
	
	@SuppressWarnings("unchecked")
	public File getConfigFile() {
		File file = null;
		try{
			IModelNode<IContextAieon> template = (IModelNode<IContextAieon>) createTemplate();
			IModelNode<? extends IDescriptor> category = (IModelNode<? extends IDescriptor>) template.getChildren().iterator().next();
			IModelLeaf<? extends IDescriptor> child = category.getChildren().iterator().next();
			IConcept loader = new ConceptWrapper( child.getDescriptor() );
			URI uri = ProjectFolderUtils.getDefaultResource( S_BUNDLE_ID, loader.getURIPath());
			file =  new File( uri.getPath() );
		}
		catch( Exception e ){
			e.printStackTrace();
		}
		return file;
	}
}
