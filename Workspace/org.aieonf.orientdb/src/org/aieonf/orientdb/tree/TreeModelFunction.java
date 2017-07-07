package org.aieonf.orientdb.tree;

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
import org.aieonf.model.provider.IProvider.DefaultModels;
import org.aieonf.orientdb.OrientGraphContextAieon;
import org.aieonf.template.ITemplateLeaf;

public class TreeModelFunction extends AbstractFunctionProvider<IContextAieon, String, IModelProvider<IModelLeaf<IDescriptor>>> {

	public static final String S_DATABASE_ID = "org.aieonf.database";

	private ITemplateLeaf<IContextAieon> template;
	
	public TreeModelFunction( ITemplateLeaf<IContextAieon> template ) {
		super( IModelProvider.S_MODEL_PROVIDER_ID, new OrientGraphContextAieon());
		this.template = template;
	}
	
	@Override
	public boolean canProvide(String id ) {
		return DefaultModels.PROVIDER.toString().equals(id) || 
				DefaultModels.DATABASE.toString().equals( id ) ||
				DefaultModels.TREE.toString().equals(id);
	}

	
	@Override
	protected IGraphModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> onCreateFunction( String id ) {
		IModelLeaf<IDescriptor> model = getDefaultModel( super.getAieon() );
		ILoaderAieon baseLoader = (ILoaderAieon) model.getDescriptor();
		URI uri = ProjectFolderUtils.getDefaultUserDatabase( baseLoader );
		baseLoader.setURI( uri );
		return new TreeModel( baseLoader, template );
	}
}