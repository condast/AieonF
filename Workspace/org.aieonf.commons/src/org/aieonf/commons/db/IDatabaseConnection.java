package org.aieonf.commons.db;

import org.aieonf.commons.strings.StringStyler;

public interface IDatabaseConnection {

	public static String REST_URL = "http://localhost:10080/aieonf/orientdb";

	public enum Requests{
		PREPARE,
		CREATE,
		ADD,
		ADD_NODE,
		ADJACENT,
		CONTAINS,
		FIND,
		FIND_ON_DESCRIPTOR,
		GET,
		GET_ALL,
		GET_OPTIONS,
		SEARCH,
		SEARCH_MODELS,
		UPDATE,
		REMOVE,
		REMOVE_ALL,
		REMOVE_CHILDREN,
		REMOVE_CHILDREN_ON_DESCRIPTOR;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString(super.name());
		}
	}
}
