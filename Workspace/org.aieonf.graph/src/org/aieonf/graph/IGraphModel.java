package org.aieonf.graph;

import org.aieonf.concept.IDescriptor;
import org.aieonf.template.IWriteModelProvider;

public interface IGraphModel<T extends IDescriptor, U extends Object> extends IWriteModelProvider<T, U>{

	public static final String S_GRAPH_MODEL_PROVIDER_ID = "org.aieonf.model.graph.provider";
}