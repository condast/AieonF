package test.aieonf.orientdb.suite;

import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.model.provider.IProvider.DefaultModels;
import org.condast.commons.test.AbstractTestSuite;

import test.aieonf.orientdb.context.TestFactory;

public class TestSuite extends AbstractTestSuite {

	private static TestSuite suite = new TestSuite();
	
	private static IModelDelegate<IModelLeaf<IContextAieon>> function;
	
	public static TestSuite getInstance(){
		return suite;
	}
	
	public static void runTests( IModelDelegate<IModelLeaf<IContextAieon>> func ){
		function = func;
		suite.performTests();
	}
	
	
	@Override
	protected void testSuite() throws Exception {
		try{
			testOpenDatabase();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		logger.info("Tests completed");
	}

	private final void testOpenDatabase() throws Exception{
		IModelProvider<IModelLeaf<IContextAieon>> provider = function.getFunction( DefaultModels.DATABASE.toString());
		TestFactory factory = TestFactory.getInstance();
		IDomainAieon domain = factory.getDomain();
		try{
			provider.open( domain );
		}
		finally{
			provider.close(domain);
			provider.deactivate();
		}
		//GraphModelTreeModel tm = model;
	}
}
