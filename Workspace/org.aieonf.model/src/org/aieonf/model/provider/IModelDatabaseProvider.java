package org.aieonf.model.provider;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.core.IModelLeaf;

/**
 * A model function provider provides delegates
 * @author Kees
 *
 * @param <T>
 * @param <U>
 */
public interface IModelDatabaseProvider<K extends IDomainAieon, D extends IDescriptor, M extends IModelLeaf<D>> extends IFunctionProvider<String, IModelProvider<K, D, M>>{

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

}