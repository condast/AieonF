package org.aieonf.orientdb.tree;

import java.net.URI;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.file.ProjectFolderUtils;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.function.AbstractFunctionProvider;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.orientdb.OrientGraphContextAieon;
import org.aieonf.template.ITemplateLeaf;

public class TreeModelFunction<T extends IDescriptor> extends AbstractFunctionProvider<T, IGraphModel<IModelLeaf<IDescriptor>>> {

	public static final String S_DATABASE_ID = "org.aieonf.database";

	private ITemplateLeaf<IContextAieon> template;
	
	public TreeModelFunction( ITemplateLeaf<IContextAieon> template ) {
		super( IModelProvider.S_MODEL_PROVIDER_ID, new OrientGraphContextAieon());
		this.template = template;
	}
	
	@Override
	public boolean canProvide(IModelLeaf<T> leaf) {
		String id = leaf.getID();
		return IModelProvider.S_MODEL_PROVIDER_ID.equals(id) || S_DATABASE_ID.equals( id );
	}

	@Override
	protected IGraphModel<IModelLeaf<IDescriptor>> onCreateFunction(IModelLeaf<T> leaf) {
		IModelLeaf<IDescriptor> model = getDefaultModel( leaf );
		ILoaderAieon baseLoader = (ILoaderAieon) model.getDescriptor();
		URI uri = ProjectFolderUtils.getDefaultUserDatabase( baseLoader );
		baseLoader.setURI( uri );

		IGraphModel<IModelLeaf<IDescriptor>> gdb = new TreeModel<T>( baseLoader, template );
		return gdb;
	}
}