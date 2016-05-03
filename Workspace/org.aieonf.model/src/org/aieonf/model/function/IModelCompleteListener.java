package org.aieonf.model.function;

import org.aieonf.commons.strings.StringStyler;

public interface IModelCompleteListener {

	public enum ParseEvents{
		COMPLETE,
		INCOMPLETE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
	public void notifyModelCompleted( ModelCompleteEvent event );
}
