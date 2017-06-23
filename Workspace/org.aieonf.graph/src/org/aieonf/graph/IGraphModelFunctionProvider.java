package org.aieonf.graph;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.builder.IFunctionProvider;

public interface IGraphModelFunctionProvider<D extends IDescribable<IContextAieon>, T extends IDescriptor> extends IFunctionProvider<T, IGraphModelProvider<D, T>>{
}