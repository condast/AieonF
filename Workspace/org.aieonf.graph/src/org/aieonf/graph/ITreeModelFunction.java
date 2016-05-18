package org.aieonf.graph;

import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IFunctionProvider;

public interface ITreeModelFunction<T extends ILoaderAieon> extends IFunctionProvider<T, IGraphModel<IModelLeaf<T>>>{
}