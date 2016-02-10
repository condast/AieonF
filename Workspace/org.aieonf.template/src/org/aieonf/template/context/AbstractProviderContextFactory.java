package org.aieonf.template.context;

import java.io.File;
import java.util.Collection;
import java.util.Stack;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.builder.DefaultModelCreator;
import org.aieonf.template.context.AbstractModelContextFactory;
import org.aieonf.util.Utils;
import org.aieonf.util.transaction.ITransaction;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public abstract class AbstractProviderContextFactory<T extends IDescribable<?>> extends AbstractModelContextFactory<IContextAieon> implements IProviderContextFactory<IContextAieon, T> 
{
	public static final String S_DATABASE_ID = "org.aieonf.database";

	private static final String S_MODEL = "Model";
	private String bundle_id;
	
	private Stack<ITransaction<T, IModelProvider<IContextAieon, T>>> transactionStack;
	
	protected AbstractProviderContextFactory( String bundle_id ) {
		this.bundle_id = bundle_id;
		transactionStack = new Stack<ITransaction<T, IModelProvider<IContextAieon, T>>>();
	}

	protected String getBundleId() {
		return bundle_id;
	}

	/**
	 * Push a transaction to the stack
	 * @param transaction
	 */
	public void push( ITransaction<T, IModelProvider<IContextAieon, T>> transaction ){
		this.transactionStack.push( transaction );	
	}
	
	/**
	 * Pop the most recent transaction
	 * @return
	 */
	public ITransaction<T, IModelProvider<IContextAieon, T>> pop(){
		return this.transactionStack.pop();
	}
	
	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		ITemplateLeaf<IContextAieon> template  = this.createDefaultTemplate( bundle_id, new DefaultModelCreator( this.getClass()));	
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
	public abstract IModelProvider<IContextAieon, T> getModelProvider();

	/* (non-Javadoc)
	 * @see org.aieonf.template.context.IProviderContextFactory#getDatabase()
	 */
	@Override
	public abstract IModelProvider<IContextAieon, T> getDatabase();

	/**
	 * Search the default model provider for 
	 * @param factory
	 * @param filter
	 * @return
	 */
	@Override
	public ITransaction<T, IModelProvider<IContextAieon, T>> search( IModelFilter<IDescriptor> filter ){
		IModelProvider<IContextAieon,T> provider = getDatabase();
		Collection<T> models = null;
		ITransaction<T, IModelProvider<IContextAieon, T>> transaction = provider.createTransaction();
		try {
			models = provider.search( filter);
			for( T model: models )
				transaction.addData( model );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transaction;
	}
}