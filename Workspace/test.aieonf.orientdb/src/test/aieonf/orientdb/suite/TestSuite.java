package test.aieonf.orientdb.suite;

import java.util.Collection;
import java.util.logging.Logger;

import org.aieonf.commons.security.ILoginListener.LoginEvents;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.filter.ModelFilter;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.model.xml.InterpreterCollection;
import org.aieonf.model.xml.XMLModelBuilder;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.builder.TemplateInterpreter;
import org.condast.commons.test.AbstractTestSuite;

import test.aieonf.orientdb.context.TestFactory;
import test.aieonf.orientdb.model.ModelInterpreter;
import test.aieonf.orientdb.service.LoginDispatcher;

public class TestSuite extends AbstractTestSuite {

	public enum Tests{
		TEST_OPEN_AND_CLOSE,
		TEST_ADD_AND_READ,
		TEST_REGISTER,
		TEST_MODEL_BUILDER;
	}
	private static TestSuite suite = new TestSuite();
	
	private static IModelDatabase<IDomainAieon,IModelLeaf<IDescriptor>> database;
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	public static TestSuite getInstance(){
		return suite;
	}
	
	public void runTests( IModelDatabase<IDomainAieon,IModelLeaf<IDescriptor>> func ){
		database = func;
		suite.performTests();
	}

	@Override
	protected void testSuite() throws Exception {
		Tests test = Tests.TEST_MODEL_BUILDER;
		try{
			switch( test ){
			case TEST_ADD_AND_READ:
				testAddReadDatabase();
				break;
			case TEST_REGISTER:
				testRegister();
				break;
			case TEST_MODEL_BUILDER:
				testModelBuilder();
				break;
			default:
				testOpenDatabase();
				break;
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		logger.info("Tests completed");
	}

	private final void testRegister() throws Exception{
		LoginDispatcher dispatcher = LoginDispatcher.getInstance();
		LoginEvent le = new LoginEvent( this, LoginEvents.REGISTER, "test", "test_pwd" );
		dispatcher.setLoginEvent(le);
		le = new LoginEvent( this );
		dispatcher.setLoginEvent(le);
		le = new LoginEvent( this, LoginEvents.LOGIN, "test", "test_pwd" );
		dispatcher.setLoginEvent(le);
		le = new LoginEvent( this );
		dispatcher.setLoginEvent(le);
	}

	private final void testOpenDatabase() throws Exception{
		TestFactory factory = TestFactory.getInstance();
		try{
			database.open( factory.getDomain());
		}
		finally{
			database.close();
			database.deactivate();
		}
		//GraphModelTreeModel tm = model;
	}

	private final void testAddReadDatabase() throws Exception{
		TestFactory factory = TestFactory.getInstance();
		try{
			database.open( factory.getDomain());
			IModelLeaf<IDescriptor> model = new Model<IDescriptor>( factory.getDomain(), "TEST");
			database.add(model);
			IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter = new ModelFilter<IDescriptor, IModelLeaf<IDescriptor>>( new AttributeFilter<IDescriptor>( AttributeFilter.Rules.Wildcard, IDescriptor.Attributes.NAME, "*"));
			Collection<IModelLeaf<IDescriptor>> results = database.search(filter);
			for( IModelLeaf<IDescriptor> leaf: results )
				logger.info( leaf.getDescriptor().toString() );
			if( results.isEmpty()){
				logger.info( "No Results. Stopping");
			}
			logger.info( "Results found: " + results.size() );
			database.remove( results.iterator().next());
			results = database.search(filter);
			logger.info( "Results found: " + results.size() );
				
		}
		finally{
			database.close();
			database.deactivate();
		}
		//GraphModelTreeModel tm = model;
	}

	public void testModelBuilder() {
		TestFactory factory = TestFactory.getInstance();
		factory.createTemplate();
		ModelInterpreter interpreter = new ModelInterpreter(); 
		IXMLModelInterpreter<IDescriptor, ITemplateLeaf<IDescriptor>> tinterpreter = new TemplateInterpreter( this.getClass() ); 
		InterpreterCollection<IDescriptor, IModelLeaf<IDescriptor>> cinterpreter = new InterpreterCollection<IDescriptor, IModelLeaf<IDescriptor>>( tinterpreter ); 
		cinterpreter.addInterpreter( interpreter );
		XMLModelBuilder<IDescriptor, IModelLeaf<IDescriptor>> builder = new XMLModelBuilder<IDescriptor, IModelLeaf<IDescriptor>>( factory.getDomain().getDomain(), cinterpreter);
		builder.build();
	}
}
