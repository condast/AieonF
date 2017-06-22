package org.aieonf.model.provider;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.builder.IFunctionProvider;

/**
 * A model function provider provides delegates
 * @author Kees
 *
 * @param <T>
 * @param <U>
 */
public interface IModelDatabaseProvider<D extends IDescribable<IContextAieon>, T extends IDescriptor> extends IFunctionProvider<D, IModelDelegate<D, T>>{

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

}