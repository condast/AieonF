package org.aieonf.sketch.service;

import java.io.File;
import java.net.URI;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.library.URLAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.concept.locator.ILocatorAieon;
import org.aieonf.concept.wrapper.ConceptWrapper;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.osgi.service.AbstractAieonFServiceProvider;
import org.aieonf.osgi.swt.IViewFactory;
import org.aieonf.template.builder.DefaultModelBuilder;
import org.aieonf.template.context.AbstractProviderContextFactory;
import org.eclipse.swt.widgets.Composite;

public class AieonFServiceProvider extends AbstractAieonFServiceProvider<Composite>{

	private static final String S_MODEL_ID = "org.aieonf.sketch";
	private static final String S_BUNDLE_ID = "org.aieonf.sketch";
	//public static final String S_SKETCH = "Sketch";
	
	private SketchFactory factory = new SketchFactory();

	public AieonFServiceProvider() {
	}

	public void addProvider( IFunctionProvider<IDescriptor,IModelDelegate<IModelLeaf<IDescriptor>>> function ){

	}
	
	public void removeProvider( IFunctionProvider<IDescriptor,IModelDelegate<IModelLeaf<IDescriptor>>> function ){
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
			URI uri = ProjectFolderUtils.getParsedUserDir( loader.getURIPath(), S_BUNDLE_ID );
			File file = new File( uri.getPath() );
			super.createBean( factory.getDomain() );
		}
		catch( Exception e ){
			e.printStackTrace();
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
	private static class SketchFactory extends AbstractProviderContextFactory<IModelLeaf<IDescriptor>>{
		
		
		private SketchFactory() {
			super( S_MODEL_ID, new DefaultModelBuilder( SketchFactory.class ));
		}

		@Override
		protected void onBuildEvent(ModelBuilderEvent<IModelLeaf<IDescriptor>> event) {
			// TODO Auto-generated method stub
			
		}
	}
}
