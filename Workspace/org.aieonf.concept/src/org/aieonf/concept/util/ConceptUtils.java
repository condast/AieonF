package org.aieonf.concept.util;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IFixedConcept;
import org.aieonf.concept.IRelationship;

public class ConceptUtils
{
	/**
	 * Returns true if the given describable is a child of the reference 
	 * @param describable
	 * @param reference
	 * @return
	 */
  public final static boolean isChild( IDescribable<?> describable, IDescribable<?> reference ){
		if( reference.equals( describable ))
			return false;
		if( !( reference.getDescriptor() instanceof IFixedConcept ))
			return false;
		IFixedConcept concept = ( IFixedConcept )reference.getDescriptor();
  	
		for( IRelationship relation: concept.getRelationships() ){
  		if( describable.equals( relation.getConceptDescriptor()))
  				return true;
  	}
		return false;
  }
  
  /**
   * Returns the parent of the given describable in the collection, or null if none were found
   * @param describable
   * @param collection
   * @return
   */
   public final static IConcept getParent( IDescribable<?> describable, Collection<IConcept> concepts ){
   	for( IConcept concept: concepts ){
   		if( ConceptUtils.isChild(describable, concept ))
   			return concept;
   	}
   	return null;
   }
  
   /**
    * Returns the child of the given describable in the collection, or null if none were found
    * @param describable
    * @param concepts
    * @return
    */
   public final static Collection<IConcept> getChildren( IDescribable<?> describable, Collection<IConcept> concepts ){
   	 Collection<IConcept> results = new ArrayList<IConcept>();   	
   	 for( IConcept concept: concepts ){
    		if( ConceptUtils.isChild(concept, describable ))
    			results.add( concept );
    	}
    	return results;
    }

}
