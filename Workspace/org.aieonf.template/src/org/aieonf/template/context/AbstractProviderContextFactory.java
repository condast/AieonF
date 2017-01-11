package org.aieonf.template.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.xml.IXMLModelBuilder;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.context.AbstractModelContextFactory;

/**
 * The simple context factory creates a default context and model
 * @author Kees Pieters
 */
public abstract class AbstractProviderContextFactory<T extends IDescribable<?>> extends AbstractModelContextFactory<IContextAieon>
{
	public static final String S_DATABASE_ID = "org.aieonf.database";

	private static final String S_MODEL = "Model";
	private String bundle_id;
	private String provider_id;
	
	private Collection<IModelDelegate<T>> delegates;
	
	private IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator;
	
	private IModelBuilderListener<T> listener = new IModelBuilderListener<T>() {
		
		@SuppressWarnings("unchecked")
		@Override
		public void notifyChange(ModelBuilderEvent<T> event) {
			onBuildEvent(event);
			notifyListeners((ModelBuilderEvent<IModelLeaf<?>>) event);
		}
	};

	protected AbstractProviderContextFactory( String bundle_id, 
			IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator ) {
		this( bundle_id, S_DATABASE_ID, creator );
	}
	
	protected AbstractProviderContextFactory( String bundle_id, String provider_id, 
			IXMLModelBuilder<IDescriptor,ITemplateLeaf<IDescriptor>> creator ) {
		this.bundle_id = bundle_id;
		this.provider_id = provider_id;
		delegates = new ArrayList<IModelDelegate<T>>();
		this.creator = creator;
	}

	protected String getBundleId() {
		return bundle_id;
	}

	protected abstract void onBuildEvent( ModelBuilderEvent<T> event );
	
	public void addProvider( IFunctionProvider<IDescriptor,IModelDelegate<T>> function ){
		IModelLeaf<IDescriptor> leaf = super.getTemplate( provider_id );
		if( function.canProvide(super.getTemplate( provider_id ))){
			IModelDelegate<T> delegate = function.getFunction( leaf );
			delegate.addListener(listener);
			this.delegates.add( delegate );
		}
	}
	

	public void removeProvider( IFunctionProvider<IDescriptor,IModelDelegate<T>> function ){
		IModelLeaf<IDescriptor> leaf = super.getTemplate( provider_id );
		if( function.canProvide(super.getTemplate( provider_id ))){
			IModelDelegate<T> delegate = function.getFunction( leaf );
			delegate.removeListener(listener);
			this.delegates.remove( delegate );
		}
	}

	@Override
	public ITemplateLeaf<IContextAieon> onCreateTemplate() {
		ITemplateLeaf<IContextAieon> template  = this.createDefaultTemplate( bundle_id, this.creator );	
		IDescriptor descriptor = template.getDescriptor();
		String source = descriptor.get( IConcept.Attributes.SOURCE );
		if( Utils.assertNull( source ))
			descriptor.set( IConcept.Attributes.SOURCE, this.bundle_id + File.separator + S_MODEL );
		return template;
	}

	public void get(IDescriptor descriptor) throws ParseException {
		for( IModelDelegate<T> delegate: delegates ){
			delegate.open();
			delegate.get(descriptor);
			delegate.close();
		}
	}

	public void search(IModelFilter<IDescriptor> filter) throws ParseException {
		for( IModelDelegate<T> delegate: delegates ){
			delegate.open();
			delegate.search( filter );
			delegate.close();
		}
	}

}