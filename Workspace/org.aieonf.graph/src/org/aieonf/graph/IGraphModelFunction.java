package org.aieonf.graph;

import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.util.graph.IVertex;

public interface IGraphModelFunction<T extends ILoaderAieon> extends IFunctionProvider<T, IGraphModel<T, IVertex<T>>>{
}