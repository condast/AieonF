package org.aieonf.sketch.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import java.util.logging.Logger;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.concept.library.URLAieon;
import org.aieonf.concept.wrapper.ConceptWrapper;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.search.ModelScanner;
import org.aieonf.osgi.service.AbstractAieonFServiceProvider;
import org.aieonf.osgi.swt.IViewFactory;
import org.aieonf.sketch.Activator;
import org.aieonf.sketch.factory.SketchFactory;
import org.aieonf.sketch.factory.SketchModelFactory;
import org.eclipse.swt.widgets.Composite;

public class AieonFServiceProvider extends AbstractAieonFServiceProvider<Composite>{

	public static final String S_AIEONF_INF = "AIEONF-INF";
	//public static final String S_SKETCH = "Sketch";
	
	private SketchFactory factory = SketchFactory.getInstance();
	
	private final Logger logger = Logger.getLogger( this.getClass().getName() );

	public AieonFServiceProvider() {
	}

	@Override
	public void setActiveDomain(IDomainAieon domain) {
		factory.setSelection(domain);
		super.setActiveDomain(domain);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initSelection() {
		try{
			IModelNode<IContextAieon> template = (IModelNode<IContextAieon>) factory.createTemplate();
			ModelScanner<? extends IDescriptor> scanner = new ModelScanner<>( template );
			Collection<IModelLeaf<IDescriptor>> categories = scanner.search( IDescriptor.Attributes.NAME.name(), CategoryAieon.Attributes.CATEGORY.toString());
			scanner = new ModelScanner<>( categories.iterator().next() );
			Collection<IModelLeaf<IDescriptor>> category = scanner.search( IDescriptor.Attributes.NAME.name(), URLAieon.Attributes.URL.toString());
			IConcept loader = new ConceptWrapper( category.iterator().next().getDescriptor() );

			URI uri = ProjectFolderUtils.getParsedAieonFDir( loader.getURIPath(), Activator.BUNDLE_ID );
			File file = new File( uri.getPath() );
			if( !file.exists() )
				file.mkdirs();
			findRoots( file );
			if( factory.isEmpty())
				return;
			for( SketchModelFactory mfactory: factory.getSketchFactories() ){
				mfactory.createTemplate();
				super.createBean( mfactory.getDomain() );				
			}
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
			if( !isValidStructure( child ))
				continue;
			try {
				factory.addSketchModelFactory( new SketchModelFactory( child ));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for( File child: file.listFiles() ){
			findRoots( child );
		}
	}

	/**
	 * first find all the root directories for sketch
	 * @param file
	 */
	protected  boolean isValidStructure( File file ){
		if( !file.isDirectory() )
			return false;
		for( File child: file.listFiles() ){
			if( S_AIEONF_INF.equals( child.getName()))
				return true;
		}
		return false;
	}

	@Override
	public IViewFactory<Composite, Composite> getCompositeFactory(String id) {
		// TODO Auto-generated method stub
		return null;
	};
}
