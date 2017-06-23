package org.aieonf.graph;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.provider.IModelDatabaseProvider;

public interface IGraphModelProvider<D extends IDomainAieon, U extends IDescribable<? extends IDescriptor>> extends IModelDatabaseProvider<D, U>{

	public static final String S_GRAPH_MODEL_PROVIDER_ID = "org.aieonf.model.graph.provider";
}