package org.aieonf.graph;

import org.aieonf.template.IWriteModelProvider;

public interface IGraphModel<U extends Object> extends IWriteModelProvider<U>{

	public static final String S_GRAPH_MODEL_PROVIDER_ID = "org.aieonf.model.graph.provider";
}