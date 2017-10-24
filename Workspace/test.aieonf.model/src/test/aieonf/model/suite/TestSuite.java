package test.aieonf.model.suite;

import java.util.Collection;
import java.util.logging.Logger;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.xml.XMLModelBuilder;
import org.aieonf.template.ITemplateLeaf;
import org.condast.commons.test.AbstractTestSuite;

import test.aieonf.model.context.TestFactory;
import test.aieonf.model.interpreter.AuthenticationInterpreter;

public class TestSuite extends AbstractTestSuite {

	public enum Tests{
		TEST_MODEL_BUILDER;
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
		Tests test = Tests.TEST_MODEL_BUILDER;
		try{
			switch( test ){
			case TEST_MODEL_BUILDER:
				testModelBuilder();
				break;
			default:
				break;
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		logger.info("Tests completed");
	}
	
	public void testModelBuilder() {
		TestFactory factory = TestFactory.getInstance();
		factory.createTemplate();
		
		AuthenticationInterpreter interpreter = new AuthenticationInterpreter(factory.getDomain());
		
		XMLModelBuilder<IDescriptor,IModelLeaf<IDescriptor>> builder = 
				new XMLModelBuilder<IDescriptor, IModelLeaf<IDescriptor>>( factory.getDomain().getDomain(), interpreter );
		builder.build();
		Collection<IModelLeaf<IDescriptor>> models = builder.getModel();
		logger.info("Models found: " + models.size());
	}
}
