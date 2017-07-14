package org.aieonf.model.provider;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IFunctionProvider;

/**
 * A model function provider provides delegates for the given descriptor
 * @author Kees
 *
 * @param <T>
 * @param <U>
 */
public interface IModelFunctionProvider<T extends Object, K extends Object, U extends IDescribable<IDescriptor>> extends IFunctionProvider<T, IModelProvider<K,U>>{

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

}