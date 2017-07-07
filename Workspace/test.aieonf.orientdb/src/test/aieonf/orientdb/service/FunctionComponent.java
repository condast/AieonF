package test.aieonf.orientdb.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.condast.commons.thread.AbstractExecuteThread;

import test.aieonf.orientdb.context.TestFactory;
import test.aieonf.orientdb.suite.TestSuite;

public class FunctionComponent extends AbstractExecuteThread
{
	
	private TestFactory factory = TestFactory.getInstance(); 
	
	private TestSuite suite = TestSuite.getInstance();
	
	private int counter;
	
	public FunctionComponent() {
		super();
		this.counter = 0;
	}

	public void activate(){};
	
	public void deactivate(){};

	public void addProvider( IFunctionProvider<String, IModelProvider<IModelLeaf<IDescriptor>>> function ){
		this.factory.addProvider( function );
		counter += 1;
		if( counter > 1 )
			super.start();
	}
	
	public void removeProvider( IFunctionProvider<String,IModelProvider<IModelLeaf<IDescriptor>>> function ){
		super.stop();
		this.factory.removeProvider(function);
	}

	@Override
	public void onInitialise() {
		// NOTHING	
	}

	@Override
	public void onExecute() {
		suite.runTests( factory.getFunction( IModelFunctionProvider.S_MODEL_PROVIDER_ID ));
	}
}
