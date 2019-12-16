package org.aieonf.commons.ui.session;


public interface ISessionListener<T extends Object> {

	enum Parameters{
		TOKEN,
		TYPE,
		DATA;
	
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	public static enum EventTypes{
		INIT,
		UPDATE,
		COMPLETED;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
	
	public void notifySessionChanged( SessionEvent<T> event );
}
