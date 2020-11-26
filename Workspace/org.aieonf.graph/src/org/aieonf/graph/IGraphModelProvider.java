package org.aieonf.graph;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.provider.IModelProvider;

public interface IGraphModelProvider<K extends Object, D extends IDescriptor, U extends IModelLeaf<D>> extends IModelProvider<K, D, U>{

	public static final String S_GRAPH_MODEL_PROVIDER_ID = "org.aieonf.model.graph.provider";
}