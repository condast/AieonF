package org.aieonf.model;

import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.builder.IFunctionProvider;

public interface IModelFunction<T extends ILoaderAieon, U extends Object > extends IFunctionProvider<T, IModelProvider<T,U>>{

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

}