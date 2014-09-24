package org.aieonf.graph;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelProvider;
import org.aieonf.template.ITemplateLeaf;

public interface IGraphModel<T extends IDescriptor, U extends Object> extends IModelProvider<T, U>{

	public static final String S_GRAPH_MODEL_PROVIDER_ID = "org.aieonf.model.graph.provider";

	public U create( ITemplateLeaf<T> template );

	public boolean add( U root );

	public boolean delete( U root );

	public boolean update( U root );
}