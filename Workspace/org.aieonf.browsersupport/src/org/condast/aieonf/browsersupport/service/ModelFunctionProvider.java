package org.condast.aieonf.browsersupport.service;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelFunction;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.template.ITemplateLeaf;
import org.condast.aieonf.browsersupport.context.ContextFactory;
import org.condast.aieonf.browsersupport.context.ModelFunction;
import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.AbstractProvider;

public class ModelFunctionProvider extends AbstractProvider<String, Object, IModelFunction<ILoaderAieon, IConcept>> {

	private static ModelFunctionProvider provider = new ModelFunctionProvider();
	
	protected ModelFunctionProvider() {
		super( new Palaver() );
	}
	
	protected static ModelFunctionProvider getInstance() {
		return provider;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onDataReceived( Object datum) {
		if(!( datum instanceof IModelLeaf ))
			return;
		IModelLeaf<ILoaderAieon> leaf = (IModelLeaf<ILoaderAieon>) datum;
		if(!IModelProvider.S_MODEL_PROVIDER_ID.equals( leaf.getID()))
			return;
		super.onDataReceived(datum);
		
		ContextFactory factory = new ContextFactory();
		ITemplateLeaf<IContextAieon> template = factory.createTemplate();
		IModelFunction function = (IModelFunction) new ModelFunction( template.getDescriptor() );
		super.provide( function );
	}

	private static class Palaver extends AbstractPalaver<String>{

		private static final String S_INTRODUCTION = "org.condast.aieonf.function.introduction";
		private static final String S_TOKEN = "org.condast.aieonf.function.token";

		private String providedToken;

		protected Palaver() {
			super( S_INTRODUCTION );
		}

		protected Palaver( String introduction, String token ) {
			super(  introduction );
			this.providedToken = token;
		}

		
		@Override
		public String giveToken() {
			if( providedToken == null )
				return S_TOKEN;
			return providedToken;
		}

		@Override
		public boolean confirm(Object token) {
			boolean retval = false;
			if( providedToken == null )
				retval = S_TOKEN .equals( token );
			else
				retval = providedToken.equals(token);
			return retval;
		}		
	}
}
