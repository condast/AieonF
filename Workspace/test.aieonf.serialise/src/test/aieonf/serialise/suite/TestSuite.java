package test.aieonf.serialise.suite;

import java.util.Scanner;
import java.util.logging.Logger;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.serialisable.core.concept.ConceptDeserialise;
import org.aieonf.serialisable.core.concept.DescriptorDeserialise;
import org.aieonf.serialisable.model.ModelLeafDeserialise;
import org.aieonf.template.def.ITemplateLeaf;
import org.condast.commons.test.core.AbstractTestSuite;
import org.condast.commons.test.core.ITestEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import test.aieonf.serialise.context.TestFactory;
import test.aieonf.serialise.core.TestModelTypeAdapter;
import test.aieonf.serialise.core.TestObject;

public class TestSuite extends AbstractTestSuite<String, String> {

	public static final String S_RESOURCES = "/resources/serialised";
	public static final String S_TXT = ".txt";
	protected TestSuite() {
		super("TestSerialisation");
	}

	public enum Tests{
		TEST_FACTORY_DESCRIPTOR,
		TEST_FACTORY,
		TEST_REGISTER,
		TEST_MODEL_BUILDER,
		TEST_SERIALISATION;
	}
	
	private static TestSuite suite = new TestSuite();
	private TestFactory factory;
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	public static TestSuite getInstance(){
		return suite;
	}
	
	public void runTests( ){
		suite.performTests();
	}

	@Override
	protected void testSuite() throws Exception {
		this.factory = TestFactory.getInstance();
		Tests test = Tests.TEST_SERIALISATION;
		try{
			switch( test ){
			case TEST_FACTORY_DESCRIPTOR:
				testFactoryDescriptor();
				break;
			case TEST_FACTORY:
				testFactory();
				break;
			case TEST_REGISTER:
				//testRegister();
				break;
			case TEST_MODEL_BUILDER:
				//testModelBuilder();
				break;
			case TEST_SERIALISATION:
				testSerialisation();
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

	private void testSerialisation() {
		Scanner scanner = null;
		for( int i=10; i<11; i++ ) {
			logger.info( "Scanning: " + S_RESOURCES + i + S_TXT );
			scanner = new Scanner( this.getClass().getResourceAsStream(S_RESOURCES + i + S_TXT));
			StringBuilder builder = new StringBuilder();
			while( scanner.hasNextLine())
				builder.append(scanner.nextLine());
			logger.info( "Parsing: " + builder.toString());
			GsonBuilder gbuilder = new GsonBuilder(); 
			gbuilder.enableComplexMapKeySerialization();
			TestModelTypeAdapter adapter = new TestModelTypeAdapter();
			gbuilder.registerTypeAdapter( TestObject.class, adapter);
			Gson gson = gbuilder.create();
			TestObject[] models = gson.fromJson(builder.toString(), TestObject[].class ); 
			logger.info("Models found: " + models.length);	
			String serialised = gson.toJson(models, TestObject[].class);
			String condense = builder.toString().replaceAll("\\s", "");
			boolean result = serialised.equals(condense);
			
			StringBuilder results = new StringBuilder();
			results.append("\t   TESTING: ");
			results.append( condense);
			results.append("\n\tSERIALISED: ");
			results.append(serialised);
			results.append("\n RESULT: length = ");
			results.append( models.length );	
			results.append( "\n SIMILAR: " + result );	
			results.append( "\n\n" );	
			logger.info(results.toString());
		}
	}

	private final void testRegister() throws Exception{
		/*
		LoginEvent le = new LoginEvent( this, LoginEvents.REGISTER, "test", "test_pwd" );
		dispatcher.setLoginEvent(le);
		le = new LoginEvent( this );
		dispatcher.setLoginEvent(le);
		le = new LoginEvent( this, LoginEvents.LOGIN, "test", "test_pwd" );
		dispatcher.setLoginEvent(le);
		le = new LoginEvent( this );
		dispatcher.setLoginEvent(le);
		*/
	}

	private final void testFactoryDescriptor() throws Exception{
		ITemplateLeaf<IContextAieon> template = factory.createTemplate();
		String str = DescriptorDeserialise.serialise(template.getDescriptor());
		logger.info("SERIALISED:\n\t" + str);
		IDescriptor descriptor = DescriptorDeserialise.create(str); 
		logger.info(descriptor.toString());
		descriptor = ConceptDeserialise.create(str); 
		logger.info(descriptor.toString());
	}

	
	private final void testFactory() throws Exception{
		ITemplateLeaf<IContextAieon> template = factory.createTemplate();
		String str = ModelLeafDeserialise.serialise(template);
		logger.info("SERIALISED:\n\t" + str);
		IModelLeaf<IDescriptor> leaf = (IModelLeaf<IDescriptor>) ModelLeafDeserialise.create(str); 
		logger.info(leaf.toString());
		IDescriptor descriptor = leaf.getDescriptor(); 
		logger.info(descriptor.toString());
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
