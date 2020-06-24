package org.aieonf.template.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.template.builder.TemplateInterpreter;
import org.aieonf.template.context.AbstractModelContextFactory;
import org.aieonf.template.def.ITemplateLeaf;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public abstract class AbstractProviderContextFactory<D extends IDescriptor, M extends IModelLeaf<D>> 
extends AbstractModelContextFactory<D,M> implements IProviderContextFactory<String, IDescriptor, M>
{
	private static final String S_MODEL = "Model";
	private String bundle_id;
	
	private Collection<IFunctionProvider<String, IModelProvider<IDescriptor, M>>> functions;
	
	private IXMLModelInterpreter<IDescriptor> creator;

	protected AbstractProviderContextFactory( String bundle_id, Class<?> clss ) {
		this( bundle_id, new TemplateInterpreter(clss));
	}
	
	protected AbstractProviderContextFactory( String bundle_id, IXMLModelInterpreter<IDescriptor> creator ) {
		this.bundle_id = bundle_id;
		functions = new ArrayList<>();
		this.creator = creator;
	}

	protected String getBundleId() {
		return bundle_id;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#hasFunction(java.lang.String)
	 */
	@Override
	public boolean hasFunction( String name ){
		if( Utils.assertNull( name ))
			return false;
		for( IFunctionProvider<String, IModelProvider<IDescriptor, M>> function: functions ){
			if( function.canProvide( name ))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#removeProvider(org.aieonf.model.builder.IFunctionProvider)
	 */
	@Override
	public void addProvider(IFunctionProvider<String, IModelProvider<IDescriptor, M>> function) {
		IDomainAieon domain = super.getDomain();
		if( function.supportsDomain( domain )){
			this.functions.add( function );
		}
	}

	@Override
	public IModelProvider<IDescriptor, M> getFunction(String key) {
		for( IFunctionProvider<String, IModelProvider<IDescriptor, M>> function: functions ){
			if( function.canProvide( key ))
				return function.getFunction(key);
		}
		return null;
	}

	@Override
	public void removeProvider( IFunctionProvider<String,IModelProvider<IDescriptor, M>> function ){
		this.functions.remove( function );
	}

	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		ITemplateLeaf<IContextAieon> template  = this.createDefaultTemplate( bundle_id, this.creator );	
		IDescriptor descriptor = template.getDescriptor();
		String source = descriptor.get( IConcept.Attributes.SOURCE );
		if( Utils.assertNull( source ))
			descriptor.set( IConcept.Attributes.SOURCE, template.getID()+ File.separator + S_MODEL );
		return template;
	}
	
	/*
	public void get(IDescriptor descriptor) throws ParseException {
		IModelLeaf<IDescriptor> leaf = super.getTemplate( provider_id );
		IDomainAieon domain = (IDomainAieon) leaf.getDescriptor();
		for( IModelFunctionProvider<IModelLeaf<T>> delegate: providers ){
			delegate.open( domain);
			delegate.get(descriptor);
			delegate.close( domain);
		}
	}

	public void search(IModelFilter<IDescriptor> filter) throws ParseException {
		IModelLeaf<IDescriptor> leaf = super.getTemplate( provider_id );
		IDomainAieon domain = (IDomainAieon) leaf.getDescriptor();
		for( IModelFunctionProvider<IModelLeaf<T>> delegate: providers ){
			delegate.open( domain );
			delegate.search( filter );
			delegate.close( domain );
		}
	}
	*/

}