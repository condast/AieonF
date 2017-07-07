package org.aieonf.orientdb.graph;

import java.net.URI;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.graph.IGraphModelProvider;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.orientdb.OrientGraphContextAieon;
import org.aieonf.template.ITemplateLeaf;

public class GraphModelFunction extends AbstractFunctionProvider<IContextAieon, String, IModelProvider<IModelLeaf<IDescriptor>>>{

	private ITemplateLeaf<IContextAieon> template;
	
	public GraphModelFunction( ITemplateLeaf<IContextAieon> template) {
		super( IGraphModelProvider.S_GRAPH_MODEL_PROVIDER_ID, new OrientGraphContextAieon());
		this.template = template;
	}

	
	@Override
	public boolean canProvide( String key) {
		return IGraphModelProvider.S_GRAPH_MODEL_PROVIDER_ID.equals(key);
	}

	
	@Override
	protected IGraphModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> onCreateFunction( String key ) {
		IModelLeaf<IDescriptor> model = getDefaultModel( template.getDescriptor() );
		ILoaderAieon baseLoader = (ILoaderAieon) model.getDescriptor();
		URI uri = ProjectFolderUtils.getDefaultUserDatabase( baseLoader, template.getDescriptor().getContext() );
		baseLoader.setURI( uri );

		return new GraphModel( baseLoader, template );
	}
}