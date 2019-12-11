package org.aieonf.model.provider;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IFunctionProvider;

/**
 * A model function provider provides delegates
 * @author Kees
 *
 * @param <T>
 * @param <U>
 */
public interface IModelDatabaseProvider<D extends IDomainAieon, M extends IDescribable> extends IFunctionProvider<String, IModelProvider<D, M>>{

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

}