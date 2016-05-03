package org.aieonf.orientdb.graph;

import java.net.URI;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.graph.IGraphModelFunction;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.function.AbstractFunction;
import org.aieonf.orientdb.OrientGraphContextAieon;
import org.condast.aieonf.osgi.utils.ProjectFolderUtils;

public class GraphModelFunction<T extends ILoaderAieon> extends AbstractFunction<T, IGraphModel<T,IVertex<T>>> implements IGraphModelFunction<T> {

	public GraphModelFunction() {
		super( IGraphModel.S_GRAPH_MODEL_PROVIDER_ID, new OrientGraphContextAieon());
	}
	
	@Override
	public boolean canProvide(IModelLeaf<T> leaf) {
		String id = leaf.getID();
		return IGraphModel.S_GRAPH_MODEL_PROVIDER_ID.equals(id);
	}

	@Override
	protected IGraphModel<T,IVertex<T>> onCreateFunction(IModelLeaf<T> leaf) {
		IModelLeaf<ILoaderAieon> model = getDefaultModel( leaf );
		ILoaderAieon baseLoader = model.getDescriptor();
		URI uri = ProjectFolderUtils.getDefaultUserDatabase( baseLoader );
		baseLoader.setURI( uri );

		IGraphModel<T,IVertex<T>> gdb = new GraphModel<T>( baseLoader );
		return gdb;
	}
}