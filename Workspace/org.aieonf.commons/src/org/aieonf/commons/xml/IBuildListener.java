package org.aieonf.commons.xml;

import org.aieonf.commons.strings.StringStyler;

public interface IBuildListener<D extends Object> {

	public enum BuildEvents{
		PREPARE,
		PERFORM,
		COMPLETE;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		public static boolean isBuildEvent( String event_str ) {
			for( BuildEvents event: values()) {
				if( event.name().equals(event_str))
					return true;
			}
			return false;
		}
	}
	
	public void notifyTestEvent( BuildEvent<D> event );
}
