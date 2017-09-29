package org.aieonf.graph;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.provider.IModelProvider;

public interface IGraphModelProvider<D extends IDomainAieon, U extends IDescribable<IDescriptor>> extends IModelProvider<D, U>{

	public static final String S_GRAPH_MODEL_PROVIDER_ID = "org.aieonf.model.graph.provider";
}