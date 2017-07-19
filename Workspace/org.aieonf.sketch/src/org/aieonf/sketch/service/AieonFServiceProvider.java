package org.aieonf.sketch.service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.wrapper.ConceptWrapper;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.osgi.service.AbstractAieonFServiceProvider;
import org.aieonf.osgi.swt.IViewFactory;
import org.aieonf.sketch.factory.SelectedFactory;
import org.aieonf.sketch.factory.SketchModelFactory;
import org.aieonf.template.builder.DefaultModelBuilder;
import org.aieonf.template.context.AbstractProviderContextFactory;
import org.eclipse.swt.widgets.Composite;

public class AieonFServiceProvider extends AbstractAieonFServiceProvider<Composite>{

	private static final String S_MODEL_ID = "org.aieonf.sketch";
	private static final String S_BUNDLE_ID = "org.aieonf.sketch";
	
	public static final String S_AIEONF_INF = "AIEONF-INF";
	//public static final String S_SKETCH = "Sketch";
	
	private SketchFactory factory = new SketchFactory();
	private SelectedFactory selected = SelectedFactory.getInstance();
	
	private Collection<SketchModelFactory> modelFactories;

	public AieonFServiceProvider() {
		modelFactories = new ArrayList<SketchModelFactory>();
	}

	public void addProvider( IFunctionProvider<String,IModelProvider<IDomainAieon, IDescriptor>> function ){
		this.factory.addProvider(function);
	}
	
	public void removeProvider( IFunctionProvider<String,IModelProvider<IDomainAieon, IDescriptor>> function ){
		this.factory.removeProvider(function);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initSelection() {
		try{
			IModelNode<IContextAieon> template = (IModelNode<IContextAieon>) factory.createTemplate();
			IModelNode<? extends IDescriptor> category = (IModelNode<? extends IDescriptor>) template.getChildren().iterator().next();
			IModelLeaf<? extends IDescriptor> child = category.getChildren().iterator().next();
			IConcept loader = new ConceptWrapper( child.getDescriptor() );
			URI uri = ProjectFolderUtils.getParsedAieonFDir( loader.getURIPath(), S_BUNDLE_ID );
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

	/**
	 * The factory contains all the necessary info to create the application
	 * @author Kees Pieters
	 */
	private static class SketchFactory extends AbstractProviderContextFactory<IContextAieon, IDescriptor>{
		
		
		private SketchFactory() {
			super( S_MODEL_ID, new DefaultModelBuilder( SketchFactory.class ));
		}
	}
}
