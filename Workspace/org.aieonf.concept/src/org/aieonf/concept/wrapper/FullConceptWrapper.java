package org.aieonf.concept.wrapper;

import java.util.Collection;
import java.util.List;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.IFullConcept;
import org.aieonf.concept.IRelationship;
import org.aieonf.concept.core.ConceptException;

public class FullConceptWrapper extends FixedConceptWrapper implements
		IFullConcept
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6054148708411631124L;

	public FullConceptWrapper(IDescriptor descriptor)
	{
		super(descriptor);
	}

	public FullConceptWrapper(String id, IDescriptor descriptor)
			throws ConceptException
	{
		super(id, descriptor);
	}

	@Override
	public boolean addRelationship(IRelationship relationship)
	{
		Collection<IRelationship> relationships = super.getRelationships();
		return relationships.add( relationship );
	}

	@Override
	public boolean addRelationship(Collection<IRelationship> newrelationships)
	{
		List<IRelationship> relationships = super.getRelationships();
		return relationships.addAll( newrelationships );
	}

	@Override
	public boolean removeRelationship(IRelationship relationship)
	{
		Collection<IRelationship> relationships = super.getRelationships();
		return relationships.remove( relationship );
	}

	@Override
	public void removeRelationships()
	{
		Collection<IRelationship> relationships = super.getRelationships();
		relationships.clear();
	}
}
