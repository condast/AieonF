package test.aieonf.orientdb.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.ModelLeaf;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IProvider.DefaultModels;
import org.aieonf.osgi.service.AbstractServiceComponent;

import test.aieonf.orientdb.context.TestFactory;
import test.aieonf.orientdb.suite.TestSuite;

public class ServiceComponent extends AbstractServiceComponent{

	public ServiceComponent() {
		super( TestFactory.getInstance() );
	}

	@Override
	public void addProvider(IFunctionProvider<IDescriptor, IModelDelegate<IModelLeaf<IContextAieon>>> function) {
		super.addProvider(function);
		IDescriptor descriptor = new Descriptor( DefaultModels.DATABASE.toString());
		IModelLeaf<IDescriptor> leaf = new ModelLeaf<IDescriptor>( descriptor );
		leaf.set( IModelLeaf.Attributes.ID, DefaultModels.DATABASE.toString());
		if( function.canProvide(leaf))
			TestSuite.runTests( function.getFunction(leaf));
	}
	
	
}
