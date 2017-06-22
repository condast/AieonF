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
public interface IModelFunctionProvider<D extends IDescribable<? extends IDescriptor>, U extends IDescriptor> extends IFunctionProvider<D, IModelDelegate<D, U>>{

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

}