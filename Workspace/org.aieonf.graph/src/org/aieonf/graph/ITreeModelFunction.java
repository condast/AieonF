package org.aieonf.graph;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.builder.IFunctionProvider;

public interface ITreeModelFunction<D extends IDescribable<IContextAieon>, T extends ILoaderAieon> extends IFunctionProvider<T, IGraphModelProvider<D, IDescriptor>>{
}