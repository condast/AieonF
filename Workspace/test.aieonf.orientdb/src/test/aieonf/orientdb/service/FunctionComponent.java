package test.aieonf.orientdb.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.condast.commons.thread.AbstractExecuteThread;

import test.aieonf.orientdb.context.TestFactory;
import test.aieonf.orientdb.suite.TestSuite;

public class FunctionComponent extends AbstractExecuteThread
{
	
	private TestFactory factory = TestFactory.getInstance(); 
	
	private TestSuite suite = TestSuite.getInstance();
		
	public FunctionComponent() {
		super();
	}

	public void activate(){};
	
	public void deactivate(){};

	public void addProvider( IFunctionProvider<String, IModelProvider<IDomainAieon,IModelLeaf<IDescriptor>>> function ){
		this.factory.addProvider( function );
		if( function.getFunction( IModelDatabase.S_MODEL_PROVIDER_ID) != null )
			super.start();
	}
	
	public void removeProvider( IFunctionProvider<String,IModelProvider<IDomainAieon, IModelLeaf<IDescriptor>>> function ){
		super.stop();
		this.factory.removeProvider(function);
	}

	@Override
	public boolean onInitialise() {
		return true;
	}

	@Override
	public void onExecute() {
		suite.runTests( (IModelDatabase<IDomainAieon, IModelLeaf<IDescriptor>>) factory.getFunction( IModelFunctionProvider.S_MODEL_PROVIDER_ID ));
	}
}
