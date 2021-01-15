package org.aieonf.search.core;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.template.context.AbstractProviderContextFactory;

/**
 * The factory contains all the necessary info to create the application
 * @author Kees Pieters
 */
public class ContextFactory extends AbstractProviderContextFactory<IDescriptor, IModelLeaf<IDescriptor>>{
	
	private static final String S_BUNDLE_ID = "org.aieonf.forum";
	
	private static ContextFactory factory = new ContextFactory();
	
	private ContextFactory() {
		super( S_BUNDLE_ID, ContextFactory.class );
	}

	/**
	 * Get the factory instance
	 * @return
	 */
	public static ContextFactory getInstance(){
		factory.createTemplate();
		return factory;
	}
}