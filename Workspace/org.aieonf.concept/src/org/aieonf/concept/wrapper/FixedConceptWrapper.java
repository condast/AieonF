package org.aieonf.concept.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.IFixedConcept;
import org.aieonf.concept.IRelationship;
import org.aieonf.concept.core.ConceptException;

public class FixedConceptWrapper extends ConceptWrapper implements
		IFixedConcept
{

	private List<IRelationship> relationships;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3879521640837912183L;

	public FixedConceptWrapper(IDescriptor descriptor)
	{
		super(descriptor);
		relationships = new ArrayList<IRelationship>();
	}

	public FixedConceptWrapper(String id, IDescriptor descriptor)
			throws ConceptException
	{
		super(id, descriptor);
		relationships = new ArrayList<IRelationship>();
	}

	@Override
	public List<IRelationship> getRelationship(IDescriptor descriptor)
	{
		IDescriptor parent = super.getDescriptor();
		if( parent instanceof IFixedConcept ){
			IFixedConcept fixed = ( IFixedConcept )parent;
			return fixed.getRelationship( parent );
		}
		List<IRelationship>results = new ArrayList<IRelationship>();;
		for( IRelationship relation: relationships ){
			if( relation.getConceptDescriptor().equals( descriptor ))
					results.add( relation );
		}
		return results;
	}

	@Override
	public List<IRelationship> getRelationships()
	{
		IDescriptor parent = super.getStoredDescriptor();
		if( parent instanceof IFixedConcept ){
			IFixedConcept fixed = ( IFixedConcept )parent;
			return fixed.getRelationships();
		}
		return this.relationships;
	}

	@Override
	public int relationshipSize()
	{
		IDescriptor parent = super.getStoredDescriptor();
		if( parent instanceof IFixedConcept ){
			IFixedConcept fixed = ( IFixedConcept )parent;
			return fixed.relationshipSize();
		}
		return this.relationships.size();
	}

	@Override
	public boolean hasNoRelationships()
	{
		IDescriptor parent = super.getStoredDescriptor();
		if( parent instanceof IFixedConcept ){
			IFixedConcept fixed = ( IFixedConcept )parent;
			return fixed.hasNoRelationships();
		}
		return this.relationships.isEmpty();
	}

}
