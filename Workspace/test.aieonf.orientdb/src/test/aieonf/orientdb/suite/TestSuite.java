package test.aieonf.orientdb.suite;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.provider.IModelProvider;
import org.condast.commons.test.AbstractTestSuite;

import test.aieonf.orientdb.context.TestFactory;

public class TestSuite extends AbstractTestSuite {

	private static TestSuite suite = new TestSuite();
	
	private static IModelProvider<IDomainAieon,IModelLeaf<IDescriptor>> provider;
	
	public static TestSuite getInstance(){
		return suite;
	}
	
	public void runTests( IModelProvider<IDomainAieon,IModelLeaf<IDescriptor>> func ){
		provider = func;
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
}
