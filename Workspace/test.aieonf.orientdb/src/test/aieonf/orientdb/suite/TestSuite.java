package test.aieonf.orientdb.suite;

import java.util.Collection;
import java.util.logging.Logger;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.filter.ModelFilter;
import org.aieonf.model.filter.ModelFilterWrapper;
import org.aieonf.model.provider.IModelDatabase;
import org.condast.commons.test.AbstractTestSuite;

import test.aieonf.orientdb.context.TestFactory;

public class TestSuite extends AbstractTestSuite {

	public enum Tests{
		TEST_OPEN_AND_CLOSE,
		TEST_ADD_AND_READ;
	}
	private static TestSuite suite = new TestSuite();
	
	private static IModelDatabase<IDomainAieon,IModelLeaf<IDescriptor>> provider;
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	public static TestSuite getInstance(){
		return suite;
	}
	
	public void runTests( IModelDatabase<IDomainAieon,IModelLeaf<IDescriptor>> func ){
		provider = func;
		suite.performTests();
	}

	@Override
	protected void testSuite() throws Exception {
		Tests test = Tests.TEST_ADD_AND_READ;
		try{
			switch( test ){
			case TEST_ADD_AND_READ:
				testAddReadDatabase();
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

	private final void testOpenDatabase() throws Exception{
		TestFactory factory = TestFactory.getInstance();
		try{
			provider.open( factory.getDomain());
		}
		finally{
			provider.close();
			provider.deactivate();
		}
		//GraphModelTreeModel tm = model;
	}

	private final void testAddReadDatabase() throws Exception{
		TestFactory factory = TestFactory.getInstance();
		try{
			provider.open( factory.getDomain());
			IModelLeaf<IDescriptor> model = new Model<IDescriptor>( factory.getDomain());
			provider.add(model);
			IModelFilter<IDescriptor> filter = new ModelFilter<IDescriptor>( new AttributeFilter<IDescriptor>( AttributeFilter.Rules.Wildcard, IDescriptor.Attributes.NAME, "*"));
			Collection<IModelLeaf<IDescriptor>> results = provider.search(filter);
			for( IModelLeaf<IDescriptor> leaf: results )
				logger.info( leaf.getDescriptor().toString() );
			if( results.isEmpty()){
				logger.info( "No Results. Stopping");
			}
			logger.info( "Results found: " + results.size() );
			provider.remove( results.iterator().next());
			results = provider.search(filter);
			logger.info( "Results found: " + results.size() );
				
		}
		finally{
			provider.close();
			provider.deactivate();
		}
		//GraphModelTreeModel tm = model;
	}

}
