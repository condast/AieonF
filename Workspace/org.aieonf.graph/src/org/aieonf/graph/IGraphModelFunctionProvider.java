package org.aieonf.graph;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IFunctionProvider;

public interface IGraphModelFunctionProvider<T extends IDescriptor> extends IFunctionProvider<T, IGraphModelProvider<IDescriptor, T>>{
}