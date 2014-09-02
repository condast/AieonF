package org.aieonf.graph;

import java.util.*;

import org.aieonf.concept.*;
import org.aieonf.util.filter.*;
import org.aieonf.util.graph.*;
import org.aieonf.util.logger.*;

/**
 * <p>Title: Concept Framework</p>
 *
 * <p>Description: This is a lightweight framework to process concepts.</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public abstract class AbstractConceptGraph implements IConceptGraph
{
	//Error messages
	public static final String S_ERR_NO_GRAPH_CREATED = 
		"No graph was created. Call createGraph first";
	
	/**
	 * The selection filters for this graph
	 */
  private Selection selection;

  //Add an additional filter to filter concept relations
  private AbstractFilter<IDescriptor> parentFilter;
  private AbstractFilter<IDescriptor> relationshipFilter;
  private AbstractFilter<IDescriptor> childFilter;
  
  private DirectedEdgeList<IConcept, IRelationship> graph;

  private Logger logger;

  /**
   * Create a default concept graph
  */
  public AbstractConceptGraph()
  {
    super();
    this.graph = null;
    this.logger = Logger.getLogger( this.getClass() );
    this.logger.trace( "Creating graph processor");
    this.parentFilter = null;
    this.relationshipFilter = null;
    this.childFilter = null;
    this.selection = Selection.Parent;
  }
  
  /**
   * Create the concept graph and include a filter for the relationships
   * 
   * @param parentFilter
   * @param childFilter
  */
  public AbstractConceptGraph( AbstractFilter<IDescriptor> parentFilter, 
  		AbstractFilter<IDescriptor> childFilter )
  {
    this();
    this.parentFilter = parentFilter;
    this.relationshipFilter = null;
    this.childFilter = childFilter;
  }

  /**
   * Create the concept graph and include a filter for the relationships
   * 
   * @param parentFilter
   * @param childFilter
   * @param relationshipFilter
  */
  public AbstractConceptGraph( AbstractFilter<IDescriptor> parentFilter, 
  		AbstractFilter<IDescriptor> childFilter, AbstractFilter<IDescriptor> relationshipFilter )
  {
    this();
    this.parentFilter = parentFilter;
    this.relationshipFilter = relationshipFilter;
    this.childFilter = childFilter;
  }

  /**
   * Create a concept graph from the given list
   * @param concepts List
   * @throws Exception
  */
  @Override
	public abstract void createGraph() throws Exception;

  /**
   * Clear the graph
  */
  @Override
	public void clear()
  {
  	if( this.graph != null )
  		this.graph.clear();
  }

  /**
   * Get the relationship filter
   * @return
   */
  public AbstractFilter<IDescriptor> getRelationshipFilter()
  {
  	return this.relationshipFilter;
  }
  
  /**
   * Set the relationship filter
   * @param filter
  */
  @Override
	public void setRelationshipFilter( AbstractFilter<IDescriptor> filter )
  {
  	this.relationshipFilter = filter;
  }
  
  /**
   * Gets the parent of the graph
   *
   * @return List
  */
  @Override
	public List<IConcept>getParents()
  {
    if( this.graph == null )
    	throw new NullPointerException( S_ERR_NO_GRAPH_CREATED );
    
    Collection<IVertex<IConcept>> origins = graph.getOrigins();
    Iterator<IVertex<IConcept>> iterator =  origins.iterator();
    List<IConcept> parents = new ArrayList<IConcept>();
    IConcept concept;
    IVertex<IConcept> vertex;
    while( iterator.hasNext()){
      vertex = iterator.next();
      concept = vertex.get();
      if(( concept == null ) || ( parents.contains( concept ) == true ))
        continue;
      parents.add( concept );
    }
    return parents;
  }

  /**
   * Gets the relationships of the given concept.
   * note that this is the graph representation, which may not
   * necessarily be equal to the relationships stored in the concept
   *
   * @param concept IConcept
   * @return List
  */
  @Override
	public List<IRelationship>getRelationships( IDescriptor descriptor )
  {
    if( this.graph == null )
    	throw new NullPointerException( S_ERR_NO_GRAPH_CREATED );

    List<IVertex<IConcept>>vertices = graph.getVertices( descriptor );
    if( vertices == null )
    	return null;
    List<IEdge<IConcept, IRelationship>> edges = new ArrayList<IEdge<IConcept, IRelationship>>();
    for( IVertex<IConcept> vertex: vertices )
    	edges.addAll( graph.incidentEdges( vertex ));

    if( edges.size() == 0 )
    	return null;

    List<IRelationship>relations = new ArrayList<IRelationship>();
    for( IEdge<IConcept,IRelationship> edge: edges )
    	relations.add( edge.get() );
    return relations;
  }

  /**
   * Gets the parent of the graph
   *
   * @return List
  */
  public List<IConcept>getChildren()
  {
    if( this.graph == null )
    	throw new NullPointerException( S_ERR_NO_GRAPH_CREATED );

    EdgeList<IConcept,IRelationship> edgeList = graph;
    Collection<IEdge<IConcept, IRelationship>> edges = edgeList.edges();
    Iterator<IEdge<IConcept, IRelationship>> iterator=  edges.iterator();
    List<IConcept> children = new ArrayList<IConcept>();
    IEdge<IConcept, IRelationship> edge;
    while( iterator.hasNext()){
      edge = iterator.next();
      if( children.contains( edge.first().get() ) == true )
        continue;
      if( edge.last().get() != null ){
        logger.trace( "Edge contents: " + edge.last().get().toString() );
        children.add( edge.last().get() );
      }
    }
    return children;
  }

  /**
   * Gets the child concept beloning to the given concept-relationship pair
   *
   * @param concept IConcept
   * @return List
  */
  @Override
	public List<IConcept> getChildren( IConcept concept )
  {
    if( this.graph == null )
    	throw new NullPointerException( S_ERR_NO_GRAPH_CREATED );
  	logger.trace( "Getting children" );

    List<IConcept> children = new ArrayList<IConcept>();
    List<IVertex<IConcept>>vertices = graph.getVertices( concept );
    if( vertices == null )
    	return children;
    List<IEdge<IConcept, IRelationship>> edges = new ArrayList<IEdge<IConcept, IRelationship>>();
    for( IVertex<IConcept> vertex: vertices )
    	edges.addAll( graph.incidentEdges( vertex ));

    if( edges.size() == 0 )
    	return children;
    
    IVertex<IConcept>last;
    for( IEdge<IConcept,IRelationship> edge: edges ){
    	last = edge.last();
    	if(( last == null ) || ( last.get() == null ))
    		return null;
    	children.add( edge.last().get() );
    }
  	logger.trace( "Returning children" + children.size() );
    return children;
  }

  /**
   * Gets the child concept belonging to the given concept-relationship pair
   *
   * @param concept IConcept
   * @return List
  */
  @Override
	public List<IConcept> getChildren( IConcept concept, IRelationship relationship )
  {
    if( this.graph == null )
    	throw new NullPointerException( S_ERR_NO_GRAPH_CREATED );

    List<IConcept> children = new ArrayList<IConcept>();
    List<IVertex<IConcept>>vertices = graph.getVertices( concept );
    if( vertices == null )
    	return children;
    List<IEdge<IConcept, IRelationship>> edges = new ArrayList<IEdge<IConcept, IRelationship>>();
    for( IVertex<IConcept> vertex: vertices )
    	edges.addAll( graph.incidentEdges( vertex ));

    if( edges.size() == 0 )
    	return children;
    
    IVertex<IConcept>last;
    for( IEdge<IConcept,IRelationship> edge: edges ){
    	if( edge.get().equals( relationship ) == false )
    		continue;
    	
    	last = edge.last();
    	if(( last == null ) || ( last.get() == null ))
    		return null;
    	children.add( edge.last().get() );
    }
    return children;
  }
  
  /**
   * Get the concepts in this graph that match the given descriptor
   * @param descriptor
   * @return
   */
  public Collection<IConcept> getConcepts( IDescriptor descriptor )
  {
  	Collection<IConcept> results = new TreeSet<IConcept>();
  	Iterator<IVertex<IConcept>> iterator = this.graph.vertices().iterator();
  	IConcept concept;;
  	while( iterator.hasNext() ){
  		concept = iterator.next().get();
  		if(( concept == null ) || 
  				( concept.equals( descriptor ) == false ))
  			continue;
  		if( results.contains( concept ))
  		results.add( concept );
  	}
  	return results;
  }
  
  /**
   * Add a concept to the graph
   * @param concept
   * @throws Exception
   */
  public void add( IConcept concept ) throws Exception
  {
  	this.clear();
  	this.createGraph();
  }
  
  /**
   * Update an existing concept
   * @param concept
   * @throws Exception
   */
  public void update( IConcept concept )  throws Exception
  {
  	this.clear();
  	this.createGraph();
  }
  
  /**
   * Remove a concept from the graph
   * @param concept
   * @throws Exception
  */
  public void remove( IConcept concept )  throws Exception
  {
  	this.clear();
  	this.createGraph();
  }
  
  /**
   * Get the graph
   * @return IGraph
  */
  @Override
	public DirectedEdgeList<IConcept, IRelationship> getGraph()
  {
    return this.graph;
  }

  /**
   * Set the graph
   * @param graph
  */
  protected void setGraph( DirectedEdgeList<IConcept, IRelationship> graph )
  {
  	this.graph = graph;
  }

  /**
   * Get the parent filter
   * @return
  */
  public AbstractFilter<IDescriptor> getParentFilter()
  {
  	return this.parentFilter;
  }
  
  /**
   * If a parent filter is added, only the related parent concepts 
   * that pass the filter are added to the graph
   *
   * @param filter Filter
  */
  @Override
	public void setParentFilter( AbstractFilter<IDescriptor> parentFilter )
  {
    this.parentFilter = parentFilter;
  }

  /**
   * Get the child filter
   * @return
  */
  public AbstractFilter<IDescriptor> getChildFilter()
  {
  	return this.childFilter;
  }
  
  /**
   * If a child filter is added, only the related parent concepts 
   * that pass the filter are added to the graph
   *
   * @param filter Filter
  */
  @Override
	public void setChildFilter( AbstractFilter<IDescriptor> childFilter )
  {
    this.childFilter = childFilter;
  }

  /**
   * Get the search selection of the graph (parent, child, or all)
   * @return
   */
  @Override
	public Selection getSelectedSearch()
  {
    return this.selection;
  }

  /**
   * Set the search selection of the graph
   * @param selection
  */
  @Override
	public void setSelectedSearch( Selection selection )
  {
    this.selection = selection;
  }

  /**
   * If true, the parents need to be filtered
   * @return
  */
  public boolean filterParents() 
  {
		return !(( parentFilter == null ) || ( selection.equals( Selection.Children )));
  }

  /**
   * If true, the children need to be filtered
   * @return
  */
  public boolean filterChildren() 
  {
		return !(( childFilter == null ) || ( selection.equals( Selection.Parent )));
  }

  /**
   * If true, the given concept is accepted as parent in the graph
   * @param concept
   * @return
   * @throws FilterException
  */
  public boolean acceptParent( IConcept concept ) throws FilterException 
  {
		if(( parentFilter == null ) || ( selection.equals( Selection.Children )))
			return true;
		logger.trace( "Accepting filter: " + concept.getName() + parentFilter.accept( concept ));
		return parentFilter.accept( concept );
  }

  /**
   * If true, the given concept is accepted as child in the graph
   * @param concept
   * @return
   * @throws FilterException
  */
  public boolean acceptChild( IConcept concept ) throws FilterException 
  {
		if(( childFilter == null ) || ( selection.equals( Selection.Parent )))
			return true;
		return childFilter.accept( concept );
  }

  /**
   * Get the opposite concept of the given concept-relationship pair.
   * Returns null if the pair wasn't found.
   *
   * @param concept IConcept
   * @param relationship IRelationship
   * @return IConcept
  */
  public IConcept opposite( IConcept concept, IRelationship relationship )
  {
    if( this.graph == null )
    	throw new NullPointerException( S_ERR_NO_GRAPH_CREATED );

    List<IVertex<IConcept>> vertices = this.graph.getVertices( concept );
  	List<IEdge<IConcept, IRelationship>> edges = this.graph.getEdges( relationship );
  	if( edges == null )
  		return null;

  	for( IEdge<IConcept, IRelationship> edge: edges ){
    	for( IVertex<IConcept> vertex: vertices )
    		if( edge.opposite(vertex) != null )
    			return edge.opposite( vertex ).get();
  	}
  	return null;
  }

  /**
   * Get all the vertices that contain the given concept
   * @param concept IConcept
   * @return List
  */
  protected List<IVertex<IConcept>>getVertices( IConcept concept )
  {
    if( this.graph == null )
    	throw new NullPointerException( S_ERR_NO_GRAPH_CREATED );

    List<IVertex<IConcept>> vertices = new ArrayList<IVertex<IConcept>>();
    Iterator<IVertex<IConcept>> iterator = this.graph.vertices().iterator();
    IVertex<IConcept> vertex;
    while( iterator.hasNext() ){
      vertex = iterator.next();
      if(( vertex.get() != null ) && ( vertex.get().equals( concept )))
        vertices.add( vertex );
    }
    return vertices;
  }

  protected List<IVertex<IConcept>>getVertices( List<IConcept> concepts )
  {
    if( this.graph == null )
    	throw new NullPointerException( S_ERR_NO_GRAPH_CREATED );
    List<IVertex<IConcept>> vertices = new ArrayList<IVertex<IConcept>>();
    for( int i = 0; i < concepts.size(); i++ )
      vertices.addAll( this.getVertices( concepts.get( i )));
    return vertices;
  }

  protected List<IConcept>getConcepts( List<IVertex<IConcept>> vertices )
  {
    List<IConcept> concepts = new ArrayList<IConcept>();
    for( int i = 0; i < vertices.size(); i++ ){
      if( concepts.contains( vertices.get( i )) == true )
        continue;
      concepts.add(vertices.get( i ).get() );
    }
    return concepts;
  }

  /**
   * adjacentConcepts
   *
   * @param concept IConcept
   * @return Enumeration
   */
  public List<IConcept> adjacentConcepts( IConcept concept )
  {
    if( this.graph == null )
    	throw new NullPointerException( S_ERR_NO_GRAPH_CREATED );

    List<IVertex<IConcept>> vertices = this.getVertices( concept );
    List<IVertex<IConcept>> vResults = new ArrayList<IVertex<IConcept>>();
    Collection<IVertex<IConcept>>adjacent;
    Iterator<IVertex<IConcept>> iterator;
    IVertex<IConcept> next;
    for( IVertex<IConcept> vertex: vertices ){
      adjacent = this.graph.adjacentVertices( vertex );
      iterator = adjacent.iterator();
      while( iterator.hasNext() ){
        next = iterator.next();
        if( next.get() != null )
          vResults.add( next );
      }
    }
    return this.getConcepts( vResults );
  }
}
