package test.aieonf.model.suite;

import java.util.Collection;
import java.util.logging.Logger;

import org.aieonf.commons.filter.IFilter;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.filter.FilterBuilder;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.utils.PrintModel;
import org.aieonf.model.xml.XMLModelBuilder;
import org.aieonf.template.def.ITemplateLeaf;
import org.condast.commons.test.core.AbstractTestSuite;
import org.condast.commons.test.core.ITestEvent;

import test.aieonf.model.context.TestFactory;
import test.aieonf.model.interpreter.AuthenticationInterpreter;

public class TestSuite extends AbstractTestSuite<String, String> {

	public TestSuite() {
		super("TEST");
	}

	public enum Tests{
		TEST_FACTORY,
		TEST_MODEL_BUILDER,
		TEST_FILTER_CONFIG;
	}
	private static TestSuite suite = new TestSuite();
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	public static TestSuite getInstance(){
		return suite;
	}
	
	public void runTests(  ){
		suite.performTests();
	}

	@Override
	protected void testSuite() throws Exception {
		Tests test = Tests.TEST_FILTER_CONFIG;
		try{
			switch( test ){
			case TEST_FACTORY:
				testFactory();
				break;
			case TEST_MODEL_BUILDER:
				testModelBuilder();
				break;
			case TEST_FILTER_CONFIG:
				testFilterConfig();
				break;
			default:
				break;
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		logger.info("Tests completed");
		System.exit(0);
	}

	private void testFactory() {
		TestFactory factory = TestFactory.getInstance();
		ITemplateLeaf<IContextAieon> template = factory.createTemplate();
		String print = PrintModel.printModel(template, false );
		logger.info( print );
	}
	
	private void testModelBuilder() {
		TestFactory factory = TestFactory.getInstance();
		factory.createTemplate();
		
		AuthenticationInterpreter interpreter = new AuthenticationInterpreter(factory.getDomain());
		
		XMLModelBuilder<IDescriptor,IModelLeaf<IDescriptor>> builder = 
				new XMLModelBuilder<IDescriptor, IModelLeaf<IDescriptor>>( factory.getDomain().getDomain(), interpreter );
		builder.build();
		Collection<IModelLeaf<IDescriptor>> models = builder.getModel();
		logger.info("Models found: " + models.size());
		for( IModelLeaf<IDescriptor> leaf: models ) {
			String print = PrintModel.printModel(leaf, false );
			logger.info( print );
		}
	}

	private final void testFilterConfig() { 
		FilterBuilder<IModelLeaf<IDescriptor>> builder = new FilterBuilder<IModelLeaf<IDescriptor>>( this.getClass(), "test","/test/filter.xml");
		try {
			builder.build();
			IFilter<IModelLeaf<IDescriptor>>[] filters = builder.getUnits();
			logger.info(filters.toString());
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		logger.info("TEST COMPLETE");
	}

	@Override
	protected void onPrepare(ITestEvent<String, String> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPerform(ITestEvent<String, String> event) {
		// TODO Auto-generated method stub
		
	}
}
