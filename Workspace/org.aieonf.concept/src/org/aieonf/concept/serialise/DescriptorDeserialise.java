package org.aieonf.concept.serialise;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;

public class DescriptorDeserialise extends AbstractDescriptorDeserialise<IDescriptor> {

	private DescriptorDeserialise() {
	}

	@Override
	protected IDescriptor onConstruct(IConceptBase base) {
		return new SDescriptor( base );
	}
	
	public static IDescriptor create( String str ) {
		DescriptorDeserialise cs = new DescriptorDeserialise();
		return cs.deserialise(str);
	}

	private class SDescriptor extends Descriptor{
		private static final long serialVersionUID = -3506641611729256709L;

		public SDescriptor(IConceptBase base) {
			super( base );
		}
	}
}
