package org.aieonf.osgi.sketch.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.template.builder.DefaultModelBuilder;
import org.aieonf.template.context.AbstractProviderContextFactory;

/**
 * The factory contains all the necessary info to create the application
 * @author Kees Pieters
 */
public class SketchFactory extends AbstractProviderContextFactory<IModelLeaf<IDescriptor>>{
	
	private static final String S_MODEL_ID = "org.aieonf.sketch";
	public static final String S_SKETCH = "Sketch";
	
	private static SketchFactory factory = new SketchFactory();
	
	private SketchFactory() {
		super( S_MODEL_ID, new DefaultModelBuilder( SketchFactory.class ));
	}

	/**
	 * Get the factory instance
	 * @return
	 */
	public static SketchFactory getInstance(){
		return factory;
	}

	@Override
	protected void onBuildEvent(ModelBuilderEvent event) {
		// TODO Auto-generated method stub
		
	}
}