package org.aieonf.graph;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.provider.IModelProvider;

public interface IGraphModelProvider<D extends IDescriptor, U extends IDescribable> extends IModelProvider<D, U>{

	public static final String S_GRAPH_MODEL_PROVIDER_ID = "org.aieonf.model.graph.provider";
}