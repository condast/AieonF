package org.aieonf.sketch.service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.wrapper.ConceptWrapper;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.osgi.service.AbstractAieonFServiceProvider;
import org.aieonf.osgi.swt.IViewFactory;
import org.aieonf.sketch.Activator;
import org.aieonf.sketch.factory.SelectedFactory;
import org.aieonf.sketch.factory.SketchFactory;
import org.aieonf.sketch.factory.SketchModelFactory;
import org.eclipse.swt.widgets.Composite;

public class AieonFServiceProvider extends AbstractAieonFServiceProvider<Composite>{

	public static final String S_AIEONF_INF = "AIEONF-INF";
	//public static final String S_SKETCH = "Sketch";
	
	private SketchFactory factory = SketchFactory.getInstance();
	
	private SelectedFactory selected = SelectedFactory.getInstance();
	
	private Collection<SketchModelFactory> modelFactories;
	
	private final Logger logger = Logger.getLogger( this.getClass().getName() );

	public AieonFServiceProvider() {
		modelFactories = new ArrayList<SketchModelFactory>();
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void initSelection() {
		try{
			IModelNode<IContextAieon> template = (IModelNode<IContextAieon>) factory.createTemplate();
			IModelNode<? extends IDescriptor> category = (IModelNode<? extends IDescriptor>) template.getChildren().iterator().next();
			IModelLeaf<? extends IDescriptor> child = category.getChildren().iterator().next();
			IConcept loader = new ConceptWrapper( child.getDescriptor() );
			URI uri = ProjectFolderUtils.getParsedAieonFDir( loader.getURIPath(), Activator.BUNDLE_ID );
			File file = new File( uri.getPath() );
			if( !file.exists() )
				file.mkdirs();
			findRoots( file );
			if( Utils.assertNull( this.modelFactories ))
				return;
			for( SketchModelFactory mfactory: modelFactories ){
				mfactory.createTemplate();
				super.createBean( mfactory.getDomain() );				
			}
			selected.setFactory( this.modelFactories.iterator().next());
		}
		catch( Exception e ){
			logger.severe( factory.getDomain().getDomain());
			e.printStackTrace();
		}
	}
	
	/**
	 * first find all the root directories for sketch
	 * @param file
	 */
	protected void findRoots( File file ){
		if( !file.isDirectory() )
			return;
		for( File child: file.listFiles() ){
			if( !S_AIEONF_INF.equals( child.getName()))
				continue;
			try {
				modelFactories.add( new SketchModelFactory( file ));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return;
		}
		for( File child: file.listFiles() ){
			findRoots( child );
		}
	}
	
	@Override
	public IViewFactory<Composite, Composite> getCompositeFactory(String id) {
		// TODO Auto-generated method stub
		return null;
	};
}
