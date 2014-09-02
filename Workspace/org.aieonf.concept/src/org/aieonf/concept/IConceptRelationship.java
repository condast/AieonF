/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.concept;

//J2SE imports
import java.util.*;

/**
 * Defines a concept, basically a wrapper around the underlying XML definition
*/
public interface IConceptRelationship extends IFixedConcept
{

    /**
     * Add a relationship to this concept
     *
     * @param relationship IRelationship
     * @return boolean
    */
    public boolean addRelationship( IRelationship relationship );

    /**
     * Add a List of relationships to this concept
     *
     * @param relationships Collection
     * @return boolean
    */
    public boolean addRelationship( Collection<IRelationship> relationships );

    /**
     * Remove a relationship from this concept
     *
     * @param relationship IRelationship
     * @return boolean
    */
    public boolean removeRelationship( IRelationship relationship );
}
