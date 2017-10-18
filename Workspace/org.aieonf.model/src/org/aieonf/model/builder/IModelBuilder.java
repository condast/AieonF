package org.aieonf.model.builder;

public interface IModelBuilder<T extends Object> {

	public static String S_DEFAULT_FOLDER = "/AIEONF-INF";
	public static String S_DEFAULT_LOCATION = S_DEFAULT_FOLDER + "/aieonf-1.0.0.xml";
	public static String S_SCHEMA_LOCATION =  S_DEFAULT_FOLDER + "/aieonf-schema.xsd";
	public static String S_DEFAULT_MODEL_LOCATION = S_DEFAULT_FOLDER + "/models.xml";

	/**
	 * Build a model
	 * @return
	 */
	public void build();
	
	/**
	 * Get the model after building
	 * @return
	 */
	public T getModel();
	
	/**
	 * Returns true if the build has completed
	 * @return
	 */
	public abstract boolean isCompleted();
}
