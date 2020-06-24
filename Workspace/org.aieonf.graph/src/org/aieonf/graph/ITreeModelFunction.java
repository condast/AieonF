package org.aieonf.graph;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.builder.IFunctionProvider;

public interface ITreeModelFunction<T extends ILoaderAieon> extends IFunctionProvider<T, IGraphModelProvider<IDescriptor, IDescriptor>>{
}