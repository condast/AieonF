package org.aieonf.graph;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.builder.IFunctionProvider;

public interface IGraphModelFunction<T extends ILoaderAieon> extends IFunctionProvider<T, IGraphModel<T, IVertex<T>>>{
}