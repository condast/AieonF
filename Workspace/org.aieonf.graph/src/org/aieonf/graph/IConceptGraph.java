package org.aieonf.graph;

import java.util.List;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.IRelationship;
import org.aieonf.util.filter.AbstractFilter;
import org.aieonf.util.graph.IGraph;

/**
 * Defines the minimal methods that a concept graph should contain
 * @author Kees Pieters
 *
 */
public interface IConceptGraph 
{
	/**
   * The selection for the relation filter
  */
  public enum Selection
  {
    All,
    Parent,
    Children
  }

  /**
   * clear the graph
  */
  public void clear();
  
  /**
   * Create a graph
   * @throws Exception
  */
	public void createGraph() throws Exception;
  
  /**
   * Gets the parent of the graph
   *
   * @return List
  */
  public List<IConcept>getParents();

  /**
   * Gets the relationships of the given concept.
   * note that this is the graph representation, which may not
   * necessarily be equal to the relationships stored in the concept
   *
   * @param concept IConcept
   * @return List
  */
  public List<IRelationship>getRelationships( IDescriptor descriptor );

  /**
   * Gets the child concept beloning to the given concept-relationship pair
   *
   * @param concept IConcept
   * @return List
  */
  public List<IConcept> getChildren( IConcept concept );

  /**
   * Gets the child concept beloning to the given concept-relationship pair
   *
   * @param concept IConcept
   * @return List
  */
  public List<IConcept> getChildren( IConcept concept, IRelationship relationship );

  /**
   * If a parent filter is added, only the related parent concepts 
   * that pass the filter are added to the graph
   *
   * @param filter Filter
  */
  public void setParentFilter( AbstractFilter<IDescriptor> parentFilter );

  /**
   * If a relationship filter is added, only the related child concepts of parents
   * that pass the filter are added to the graph. If used, this filter has precedence
   * over the child filter. 
   *
   * @param filter Filter
  */
  public void setRelationshipFilter( AbstractFilter<IDescriptor> relationshipFilter );

  /**
   * If a child filter is added, only the related parent concepts 
   * that pass the filter are added to the graph
   *
   * @param filter Filter
  */
  public void setChildFilter( AbstractFilter<IDescriptor> childFilter );

  /**
   * Get the search selection of the graph (parent, child, or all)
   * @return
   */
  public Selection getSelectedSearch();
  
  /**
   * Set a new selection
   * @param selection
  */
  public void setSelectedSearch( Selection selection );

  /**
   * Get the underlying graph
   * @return graph IConceptGraph
   * @return
   */
  public IGraph<IConcept, IRelationship> getGraph();
}
