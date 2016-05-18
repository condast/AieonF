package org.aieonf.orientdb.graph;

import java.net.URI;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.orientdb.OrientGraphContextAieon;
import org.aieonf.template.ITemplateLeaf;

public class GraphModelFunction<T extends IDescriptor> extends AbstractFunctionProvider<T, IGraphModel<IVertex<T>>>{

	private ITemplateLeaf<IContextAieon> template;
	
	public GraphModelFunction( ITemplateLeaf<IContextAieon> template) {
		super( IGraphModel.S_GRAPH_MODEL_PROVIDER_ID, new OrientGraphContextAieon());
		this.template = template;
	}
	
	@Override
	public boolean canProvide(IModelLeaf<T> leaf) {
		String id = leaf.getID();
		return IGraphModel.S_GRAPH_MODEL_PROVIDER_ID.equals(id);
	}

	@Override
	protected IGraphModel<IVertex<T>> onCreateFunction(IModelLeaf<T> leaf) {
		IModelLeaf<IDescriptor> model = getDefaultModel( leaf );
		ILoaderAieon baseLoader = (ILoaderAieon) model.getDescriptor();
		URI uri = ProjectFolderUtils.getDefaultUserDatabase( baseLoader, template.getDescriptor().getContext() );
		baseLoader.setURI( uri );

		IGraphModel<IVertex<T>> gdb = new GraphModel<T>( baseLoader, template );
		return gdb;
	}
}