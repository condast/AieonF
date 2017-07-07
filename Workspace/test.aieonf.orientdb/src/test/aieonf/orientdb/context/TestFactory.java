package test.aieonf.orientdb.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.template.context.AbstractProviderContextFactory;

/**
 * The application configurator is defined by an
 * application aieon that contains all the necessary
 * info to create the application
 * @author Kees Pieters
 */
public class TestFactory extends AbstractProviderContextFactory<IContextAieon, IModelLeaf<IDescriptor>>{
	
	private static final String S_BUNDLE_ID = "org.test.orientdb";

	private static TestFactory factory = new TestFactory();
	
	private TestFactory() {
		super( S_BUNDLE_ID, TestFactory.class );
		super.createTemplate();
	}

	public static TestFactory getInstance(){
		return factory;
	}
}