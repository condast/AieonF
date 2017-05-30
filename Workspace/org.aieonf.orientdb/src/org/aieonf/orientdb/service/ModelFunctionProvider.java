package org.aieonf.orientdb.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.model.provider.IModelFunctionProvider;
import org.aieonf.orientdb.graph.GraphModelFunction;
import org.aieonf.orientdb.tree.TreeModelFunction;
import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.AbstractProvider;

public class ModelFunctionProvider extends AbstractFunctionProvider<IDescriptor, IModelDelegate<IModelLeaf<IDescriptor>>> implements IModelFunctionProvider<IDescriptor,IModelLeaf<IDescriptor>> {

	private static ModelFunctionProvider provider = new ModelFunctionProvider();
	
	protected ModelFunctionProvider() {
		super( new Palaver() );
	}
	
	protected static ModelFunctionProvider getInstance() {
		return provider;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDataReceived( Object datum) {
		if(!( datum instanceof IModelLeaf ))
			return;
		IModelLeaf<ILoaderAieon> leaf = (IModelLeaf<ILoaderAieon>) datum;
		super.onDataReceived(datum);
		if(IGraphModel.S_GRAPH_MODEL_PROVIDER_ID.equals( leaf.getID())){
			GraphModelFunction<ILoaderAieon> function = new GraphModelFunction<ILoaderAieon>();
			super.provide( function );
			return;
		}
		if(IModelProvider.S_MODEL_PROVIDER_ID.equals( leaf.getID())){
			TreeModelFunction<ILoaderAieon> function = new TreeModelFunction<ILoaderAieon>();
			super.provide( function );
			return;
		}
		if( TreeModelFunction.S_DATABASE_ID.equals( leaf.getID())){
			TreeModelFunction<ILoaderAieon> function = new TreeModelFunction<ILoaderAieon>();
			super.provide( function );
			return;
		}
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
