package org.aieonf.concept.serialise;

import org.aieonf.concept.IDescribable;

@FunctionalInterface
public interface IDeserialise<D extends IDescribable> {

	public D deserialise( String str );
}
