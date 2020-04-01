package org.aieonf.commons.http;

public interface IDatabaseRequests {

	public enum Requests{
		ADD,
		GET,
		UPDATE,
		DELETE;

		@Override
		public String toString() {
			return super.name().toLowerCase();
		}
	}

	public enum Attributes{
		ID,
		TOKEN;

		@Override
		public String toString() {
			return super.name().toLowerCase();
		}
	}
}
