package org.aieonf.graph;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.provider.IModelDatabaseProvider;

public interface IGraphModel<D extends IDescribable<IContextAieon>, U extends IDescriptor> extends IModelDatabaseProvider<D,U>{

	public static final String S_GRAPH_MODEL_PROVIDER_ID = "org.aieonf.model.graph.provider";
}