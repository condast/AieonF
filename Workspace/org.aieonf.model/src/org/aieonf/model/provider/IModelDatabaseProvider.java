package org.aieonf.model.provider;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IFunctionProvider;

/**
 * A model function provider provides delegates
 * @author Kees
 *
 * @param <T>
 * @param <U>
 */
public interface IModelDatabaseProvider<D extends IDomainAieon, T extends IDescribable<? extends IDescriptor>> extends IFunctionProvider<String, IModelProvider<T>>{

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

}