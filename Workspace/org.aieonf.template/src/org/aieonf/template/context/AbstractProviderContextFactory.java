package org.aieonf.template.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import org.aieonf.commons.Utils;
import org.aieonf.commons.transaction.AbstractTransaction;
import org.aieonf.commons.transaction.ITransaction;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.xml.IXMLModelBuilder;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.context.AbstractModelContextFactory;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public abstract class AbstractProviderContextFactory<T extends IDescribable<?>> extends AbstractModelContextFactory<IContextAieon> implements IProviderContextFactory<IContextAieon, T> 
{
	public static final String S_DATABASE_ID = "org.aieonf.database";

	private static final String S_MODEL = "Model";
	private String bundle_id;
	
	private Stack<ITransaction<T, IModelProvider<T>>> transactionStack;
	private Collection<IFunctionProvider<IDescriptor, IModelProvider<T> >> providers;
	
	
	private IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator;

	protected AbstractProviderContextFactory( String bundle_id, IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator ) {
		this.bundle_id = bundle_id;
		providers = new ArrayList<IFunctionProvider<IDescriptor, IModelProvider<T>>>();
		transactionStack = new Stack<ITransaction<T, IModelProvider<T>>>();
		this.creator = creator;
	}

	protected String getBundleId() {
		return bundle_id;
	}

	public void addProvider( IFunctionProvider<IDescriptor,IModelProvider<T>> function ){
		this.providers.add( function );
	}
	

	public void removeProvider( IFunctionProvider<IDescriptor,IModelProvider<T>> function ){
		this.providers.remove( function );
	}

	protected IModelProvider<T> getProvider( String id ){
		for(IFunctionProvider<IDescriptor, IModelProvider<T>> provider: this.providers ){
			if( provider.canProvide( super.getTemplate( id )))
					return provider.getFunction(super.getTemplate(id));
		}
		return null;
	}

	/**
	 * Push a transaction to the stack
	 * @param transaction
	 */
	public void push( ITransaction<T, IModelProvider<T>> transaction ){
		this.transactionStack.push( transaction );	
	}
	
	/**
	 * Pop the most recent transaction
	 * @return
	 */
	public ITransaction<T, IModelProvider<T>> pop(){
		return this.transactionStack.pop();
	}
	
	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		ITemplateLeaf<IContextAieon> template  = this.createDefaultTemplate( bundle_id, this.creator );	
		IDescriptor descriptor = template.getDescriptor();
		String source = descriptor.get( IConcept.Attributes.SOURCE );
		if( Utils.isNull( source ))
			descriptor.set( IConcept.Attributes.SOURCE, this.bundle_id + File.separator + S_MODEL );
		return template;
	}
	
	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#getModelProvider()
	 */
	@Override
	public abstract IModelProvider<T> getModelProvider();

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#getDatabase()
	 */
	@Override
	public abstract IModelProvider<T> getDatabase();

	/**
	 * Search the default model provider for 
	 * @param factory
	 * @param filter
	 * @return
	 */
	@Override
	public ITransaction<T, IModelProvider<T>> search( IModelFilter<IDescriptor> filter ){
		IModelProvider<T> provider = null;
		ITransaction<T, IModelProvider<T>> transaction = null;
		try {
			provider = getDatabase();
			if( provider == null )
				return null;
			Collection<T> models = null;
			transaction = createTransaction( provider );

			provider.open();
			models = provider.search( filter);
			for( T model: models )
				transaction.addData( model );
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if( provider != null )
				provider.close();
		}
		return transaction;
	}

	/**
	 * Create a transaction for the given provider
	 * @param provider
	 * @return
	 */
	public ITransaction<T, IModelProvider<T>> createTransaction( IModelProvider<T> provider) {
		return new AbstractTransaction<T, IModelProvider<T>>( provider ){

			@Override
			protected boolean onCreate(IModelProvider<T> provider) {
				provider.open();
				return provider.isOpen();
			}

			@Override
			public void close() {
				super.getProvider().close();
				super.close();
			}
		};
	}
}