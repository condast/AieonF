package org.aieonf.commons.ui.table;

import org.aieonf.commons.strings.StringStyler;

public interface ITableEventListener<D, C extends Object> {

	public enum TableEvents{
		VIEW_TABLE,
		EDIT,
		DELETE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	public void notifyTableEvent( TableEvent<D,C> event );
}
