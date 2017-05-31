package test.aieonf.orientdb.context;

import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.template.builder.DefaultModelBuilder;
import org.aieonf.template.context.AbstractProviderContextFactory;

/**
 * The application configurator is defined by an
 * application aieon that contains all the necessary
 * info to create the application
 * @author Kees Pieters
 */
public class TestFactory extends AbstractProviderContextFactory<IContextAieon>{
	
	private static final String S_MODEL_ID = "org.test.orientdb";

	private static TestFactory factory = new TestFactory();
	
	private TestFactory() {
		super( S_MODEL_ID, IModelProvider.S_MODEL_PROVIDER_ID, new DefaultModelBuilder( TestFactory.class ) );
	}

	public static TestFactory getInstance(){
		return factory;
	}
}