package org.aieonf.concept.domain;

import org.aieonf.concept.IDescriptor;
import org.aieonf.util.StringStyler;

public interface IDomainAieon extends IDescriptor
{
	//Supported attributes
	public enum Attributes{
		SHORT_NAME,
		DOMAIN,
		ACTIVE,
		SORT,
		PARENT;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValid( String str ){
			for( Attributes attr: values() ){
				if( attr.name().equals( str ))
					return true;
			}
			return false;
		}
	}
	
	/**
	 * Get the domain name
	 * @return
	*/
	public String getDomain();
	
	/**
	 * Get the short name for this domain
	 */
	public String getShortName();
	
	/**
	 * returns true if the domain is hidden from view
	 * @return
	 */
	public boolean isHidden();
}
