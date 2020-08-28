package org.aieonf.commons.filter;

public interface IAttributeFilter<D extends Object> extends IFilter<D> {

	public String getReference();
	
	public String getValue();
	
	public void setValue( String value );
	
}
