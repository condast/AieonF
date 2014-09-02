package org.aieonf.graph;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.template.graph.IGraphModelProvider;

public interface IGraphModelFunction<T extends ILoaderAieon, U extends IDescriptor > extends IFunctionProvider<T, IGraphModelProvider<T,U>>{
}