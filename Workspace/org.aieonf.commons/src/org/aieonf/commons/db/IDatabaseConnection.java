package org.aieonf.commons.db;

import org.aieonf.commons.strings.StringStyler;

public interface IDatabaseConnection {

	public static String REST_URL = "http://localhost:10080/aieonf/orientdb";

	public enum Requests{
		PREPARE,
		CREATE,
		ADD,
		ADD_NODE,
		CONTAINS,
		GET,
		SEARCH,
		UPDATE,
		REMOVE,
		REMOVE_ALL;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString(super.name());
		}
	}
}
