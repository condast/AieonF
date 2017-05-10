package org.aieonf.orientdb.factory;

import java.io.File;
import java.net.URI;
import java.util.Scanner;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.wrapper.ConceptWrapper;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.builder.DefaultModelBuilder;
import org.aieonf.template.context.AbstractProviderContextFactory;

public class OrientDBFactory extends AbstractProviderContextFactory<IModelLeaf<IDescriptor>>{

	private static final String S_MODEL_ID = "org.aieonf.orientdb";
	private static final String S_BUNDLE_ID = "org.aieonf.orientdb";
	
	public static final String S_AIEONF_INF = "AIEONF-INF";
	
	private static OrientDBFactory factory = new OrientDBFactory();
	
	private OrientDBFactory() {
		super( S_MODEL_ID, new DefaultModelBuilder( OrientDBFactory.class ));
	}
	
	public static final OrientDBFactory getInstance(){
		return factory;
	}

	@Override
	protected void onBuildEvent(ModelBuilderEvent<IModelLeaf<IDescriptor>> event) {
		System.out.println( event.isCompleted());	
	}

	@SuppressWarnings("unchecked")
	public String getConfigFile() {
		StringBuffer buffer = new StringBuffer();
		try{
			IModelNode<IContextAieon> template = (IModelNode<IContextAieon>) createTemplate();
			IModelNode<? extends IDescriptor> category = (IModelNode<? extends IDescriptor>) template.getChildren().iterator().next();
			IModelLeaf<? extends IDescriptor> child = category.getChildren().iterator().next();
			IConcept loader = new ConceptWrapper( child.getDescriptor() );
			URI uri = ProjectFolderUtils.getDefaultResource( S_BUNDLE_ID, loader.getURIPath());
			File file = new File( uri.getPath() );
			Scanner scanner = new Scanner( file );
			try{
			while( scanner.hasNextLine() )
				buffer.append( scanner.nextLine() );
			}
			finally{
				scanner.close();
			}
		}
		catch( Exception e ){
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
