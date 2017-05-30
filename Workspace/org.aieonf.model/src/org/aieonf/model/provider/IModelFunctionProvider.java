package org.aieonf.model.provider;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IFunctionProvider;

/**
 * A model function provider provides delegates
 * @author Kees
 *
 * @param <T>
 * @param <U>
 */
public interface IModelFunctionProvider<T extends IDescriptor, U extends Object > extends IFunctionProvider<T, IModelDelegate<U>>{

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

}