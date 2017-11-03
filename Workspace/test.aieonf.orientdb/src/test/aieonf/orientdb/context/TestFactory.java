package test.aieonf.orientdb.context;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.Concept;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.template.context.AbstractProviderContextFactory;
import org.aieonf.template.def.ITemplateLeaf;

/**
 * The application configurator is defined by an
 * application aieon that contains all the necessary
 * info to create the application
 * @author Kees Pieters
 */
public class TestFactory extends AbstractProviderContextFactory<IContextAieon, IModelLeaf<IDescriptor>>{
	
	private static final String S_BUNDLE_ID = "org.test.orientdb";

	private static TestFactory factory;

	private IModelBuilderListener<IDescribable<?>> listener = new IModelBuilderListener<IDescribable<?>>() {

		private IConcept concept;
		
		@Override
		public void notifyChange(ModelBuilderEvent<IDescribable<?>> event) {
			switch( event.getAttribute()) {
			case DESCRIPTOR:
				concept = new Concept();
				event.setModel( new ModelLeaf<IDescriptor>( concept ));
				break;
			case PROPERTIES:
				//concept.set( event., value);model.getDescriptor().set(key, value);e
			}
		}	
	};

	private TestFactory() {
		super( S_BUNDLE_ID, TestFactory.class );
		factory = new TestFactory();
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