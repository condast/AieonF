package test.aieonf.serialise.context;

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
public class TestFactory extends AbstractProviderContextFactory<IDescriptor, IModelLeaf<IDescriptor>>{
	
	private static final String S_BUNDLE_ID = "org.test.serialise";

	private static TestFactory factory = new TestFactory();

	private IModelBuilderListener<IDescribable> listener = new IModelBuilderListener<IDescribable>() {

		private IConcept concept;
		
		@Override
		public void notifyChange(ModelBuilderEvent<IDescribable> event) {
			switch( event.getAttribute()) {
			case DESCRIPTOR:
				concept = new Concept();
				event.setModel( new ModelLeaf<IDescriptor>( concept ));
				break;
			case PROPERTIES:
				//concept.set( event., value);model.getDescriptor().set(key, value);e
			default:
				break;
			}
		}	
	};

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