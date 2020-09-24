package org.aieonf.sketch.factory;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.library.URLAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.sketch.Activator;
import org.aieonf.template.context.AbstractProviderContextFactory;

public class SketchFactory extends AbstractProviderContextFactory<IDescriptor, IModelLeaf<IDescriptor>>{

	private static SketchFactory selected = new SketchFactory();
	
	private SketchFactory() {
		super( Activator.BUNDLE_ID,  SketchFactory.class );
	}
	
	public static SketchFactory getInstance(){
		return selected;
	}

	/**
	 * Get the factory of the active domain, or null if the domain does not belong to the skteches
	 * @return
	 * @throws MalformedURLException
	 */
	public synchronized SketchModelFactory getFactory() throws MalformedURLException {
		createTemplate();
		IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) createModel();
		URLAieon urlAieon = new URLAieon( model.getData());
		URI path = ProjectFolderUtils.getParsedAieonFDir( urlAieon.getURIPath(), Activator.BUNDLE_ID);
		SketchModelFactory factory = SketchModelFactory.getFactory( path.getRawPath(), getDomain() );
		return factory;
	}

	/**
	 * Get all the factories that are supported by sketch
	 * @return
	 * @throws MalformedURLException
	 */
	public synchronized Map<IDomainAieon,SketchModelFactory> getFactories() throws MalformedURLException {
		IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) createModel();
		URLAieon urlAieon = new URLAieon( model.getData());
		URI path = ProjectFolderUtils.getParsedAieonFDir( urlAieon.getURIPath(), Activator.BUNDLE_ID);
		return SketchModelFactory.getFactories( path.getRawPath());
	}
}
