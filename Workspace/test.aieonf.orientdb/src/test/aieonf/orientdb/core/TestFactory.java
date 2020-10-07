package test.aieonf.orientdb.core;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.template.context.AbstractProviderContextFactory;
import org.aieonf.template.def.ITemplateLeaf;

/**
 * The application configurator is defined by an
 * application aieon that contains all the necessary
 * info to create the application
 * @author Kees Pieters
 */
public class TestFactory extends AbstractProviderContextFactory<IDescriptor, IModelLeaf<IDescriptor>>{
	
	private static final String S_BUNDLE_ID = "org.test.orientdb";

	private static TestFactory factory = new TestFactory();


	private TestFactory() {
		super( S_BUNDLE_ID, TestFactory.class );
		super.createTemplate();
	}

	public static TestFactory getInstance(){
		return factory;
	}
	
	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		ITemplateLeaf<IContextAieon> result = super.onCreateTemplate();
		return result;
	}
	
	
}