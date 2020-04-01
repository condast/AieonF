package org.aieonf.concept.serialise;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.core.IConceptBase;

public class ConceptDeserialise extends AbstractDescriptorDeserialise<IConcept> {

	private ConceptDeserialise() {
	}

	@Override
	protected IConcept onConstruct(IConceptBase base) {
		return new SConcept( base );
	}
	
	public static IConcept create( String str ) {
		ConceptDeserialise cs = new ConceptDeserialise();
		return cs.deserialise(str);
	}

	private class SConcept extends Concept{
		private static final long serialVersionUID = -3506641611729256709L;

		protected SConcept(IConceptBase base) {
			super(base);
		}
	}
}
