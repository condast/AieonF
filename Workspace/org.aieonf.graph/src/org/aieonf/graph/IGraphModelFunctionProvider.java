package org.aieonf.graph;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IFunctionProvider;

public interface IGraphModelFunctionProvider<T extends IDescriptor> extends IFunctionProvider<T, IGraphModel<IVertex<T>>>{
}