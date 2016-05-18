package org.aieonf.model;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IFunctionProvider;

public interface IModelFunctionProvider<T extends IDescriptor, U extends Object > extends IFunctionProvider<T, IModelProvider<U>>{

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

}