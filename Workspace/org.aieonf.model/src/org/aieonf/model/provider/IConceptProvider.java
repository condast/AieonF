package org.aieonf.model.provider;

import org.aieonf.concept.datauri.IDataURI;

public interface IConceptProvider {

	public String getId();
	
	public String getName();
	
	/**
	 * Query for the given data uri's
	 * @param query
	 * @return
	 */
	IDataURI[] query(String query);

}