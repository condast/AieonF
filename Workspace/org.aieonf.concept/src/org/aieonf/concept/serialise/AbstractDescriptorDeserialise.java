package org.aieonf.concept.serialise;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.IConceptBase;

import com.google.gson.Gson;

public abstract class AbstractDescriptorDeserialise<D extends IDescriptor> implements IDeserialise<D> {

	protected AbstractDescriptorDeserialise() {}

	protected abstract D onConstruct( IConceptBase base );
	
	@Override
	public D deserialise(String str) {
		Gson gson = new Gson();
		IConceptBase base = gson.fromJson(str, ConceptBase.class);
		return onConstruct(base);
	}
	
	public static <D extends IDescriptor> String serialise( D descriptor ) {
		Gson gson = new Gson();
		String str = gson.toJson(descriptor.getBase(), ConceptBase.class);
		return str;
	}
}
