package org.aieonf.orientdb.tree;

import java.net.URI;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.function.AbstractFunction;
import org.aieonf.orientdb.OrientGraphContextAieon;

public class TreeModelFunction<T extends ILoaderAieon> extends AbstractFunction<T, IGraphModel<T,IModelLeaf<IDescriptor>>> {

	public static final String S_DATABASE_ID = "org.aieonf.database";

	public TreeModelFunction() {
		super( IModelProvider.S_MODEL_PROVIDER_ID, new OrientGraphContextAieon());
	}
	
	@Override
	public boolean canProvide(IModelLeaf<T> leaf) {
		String id = leaf.getID();
		return IModelProvider.S_MODEL_PROVIDER_ID.equals(id) || S_DATABASE_ID.equals( id );
	}

	@Override
	protected IGraphModel<T,IModelLeaf<IDescriptor>> onCreateFunction(IModelLeaf<T> leaf) {
		IModelLeaf<ILoaderAieon> model = getDefaultModel( leaf );
		ILoaderAieon baseLoader = model.getDescriptor();
		URI uri = ProjectFolderUtils.getDefaultUserDatabase( baseLoader );
		baseLoader.setURI( uri );

		IGraphModel<T,IModelLeaf<IDescriptor>> gdb = new TreeModel<T>( baseLoader );
		return gdb;
	}
}