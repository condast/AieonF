package org.aieonf.model.builder;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;

public interface IModelBuilder<T extends IDescriptor> {

	public static String S_DEFAULT_FOLDER = "/AIEONF-INF";
	public static String S_DEFAULT_LOCATION = S_DEFAULT_FOLDER + "/aieonf-1.0.0.xml";
	public static String S_SCHEMA_LOCATION =  S_DEFAULT_FOLDER + "/aieonf-schema.xsd";
	public static String S_DEFAULT_MODEL_LOCATION = S_DEFAULT_FOLDER + "/models.xml";

	/**
	 * Build a model
	 * @return
	 */
	public IModelLeaf<T> build();
	
	/**
	 * Returns true if the build has completed
	 * @return
	 */
	public abstract boolean isCompleted();
}
