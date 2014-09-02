package org.aieonf.template.graph;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelProvider;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.util.graph.IModel;
import org.aieonf.util.graph.IVertex;

public interface IGraphModelProvider<T extends IDescriptor, U extends IDescriptor> extends IModelProvider<T, IModel<U,U>>{

	public static final String S_GRAPH_MODEL_PROVIDER_ID = "org.aieonf.model.graph.provider";

	public IVertex<T> create( ITemplateLeaf<T> template );

	public boolean add( IVertex<T> root );

	public boolean delete( IVertex<T> root );

	public boolean update( IVertex<T> root );
}