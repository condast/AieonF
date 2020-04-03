package org.aieonf.commons.db;

import org.aieonf.commons.strings.StringStyler;

public interface IDatabaseConnection {

	public static String REST_URL = "http://localhost:10080/aieonf/orientdb";

	public enum Requests{
		ADD,
		GET,
		SEARCH,
		UPDATE,
		REMOVE;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString(super.name());
		}
	}
}
