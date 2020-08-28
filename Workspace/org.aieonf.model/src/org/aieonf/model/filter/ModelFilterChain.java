package org.aieonf.model.filter;

import org.aieonf.commons.filter.FilterChain;
import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.IAttributeFilter;

public class ModelFilterChain<D extends Object> extends FilterChain<D> implements IAttributeFilter<D>{

	public ModelFilterChain() throws FilterException {
		super();
	}

	public ModelFilterChain(Rules chainRule) throws FilterException {
		super(chainRule);
	}

	@Override
	public String getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		
	}

}
