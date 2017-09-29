package org.aieonf.orientdb.tree;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.orientdb.cache.CacheDatabase;
import org.aieonf.orientdb.core.OrientDBNode;
import org.aieonf.orientdb.graph.AbstractOrientGraphModel;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.ITemplateNode;
import org.aieonf.template.builder.TemplateModelBuilderEvent;

import com.orientechnologies.common.util.OCallable;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class TreeModelDatabase extends AbstractOrientGraphModel<IDomainAieon, IModelLeaf<IDescriptor>> implements IModelDatabase<IDomainAieon, IModelLeaf<IDescriptor>> {
	
	public static final String S_ERR_NO_TYPE = "The leaf has no type! Please provide one";
	
	private ITemplateLeaf<IContextAieon> template;
	private CacheDatabase<IDomainAieon> cache;
	
	public TreeModelDatabase( ITemplateLeaf<IContextAieon> template ) {
		super();
		cache = new CacheDatabase<IDomainAieon>();
		this.template = template;
	}

	@Override
	public void open(IDomainAieon domain) {
		this.cache.open(domain);
		if( this.cache.isOpen() )
			super.open(domain);
	}

	@Override
	public void close() {
		this.cache.close();
		super.close();
	}

	@Override
	public boolean hasFunction(String function) {
		if(!DefaultModels.isValid( function ))
			return false;
		return !DefaultModels.GRAPH.equals( DefaultModels.getModel(function ));
	}

	public IModelLeaf<IDescriptor> create() {
		Vertex root = super.getGraph().getVerticesOfClass( S_ROOT).iterator().next();
		root.setProperty( ConceptBase.getAttributeKey( IDescriptor.Attributes.NAME ), S_ROOT );
		root.setProperty( ConceptBase.getAttributeKey( IDescriptor.Attributes.VERSION ), 1 );
		IModelLeaf<IDescriptor> parent = new OrientDBNode( super.getGraph(), root ); 
		this.notifyListeners( new TemplateModelBuilderEvent<IContextAieon, IModelLeaf<IDescriptor>>(this, template, new OrientDBNode( super.getGraph(), root )));
		create( root, template );
		return parent;
	}

	@SuppressWarnings({ "unchecked" })
	protected void create( Vertex vertex, ITemplateLeaf<? extends IDescriptor> leaf ) {
		String date = String.valueOf( Calendar.getInstance().getTimeInMillis());
		Vertex child = super.getGraph().addVertex( leaf.getID());
		IModelLeaf<IDescriptor> node = new OrientDBNode( super.getGraph(), child );
		IDescriptor descriptor = node.getDescriptor();
		descriptor.set( ConceptBase.getAttributeKey( IDescriptor.Attributes.CREATE_DATE ), date );
		descriptor.set( IModelLeaf.Attributes.IDENTIFIER, leaf.getIdentifier() );
		super.getGraph().addEdge(null, vertex, child, leaf.getIdentifier());
		this.notifyListeners( new TemplateModelBuilderEvent<IContextAieon, IModelLeaf<IDescriptor>>(this, (ITemplateLeaf<IContextAieon>) leaf, new OrientDBNode( super.getGraph(), child )));
		if( !leaf.isLeaf()){
			ITemplateNode<IDescriptor> nd = (ITemplateNode<IDescriptor>) leaf;
			for( IModelLeaf<? extends IDescriptor> next: nd.getChildren() )
				create( child, (ITemplateLeaf<? extends IDescriptor>) next );
		}
	}

	/**
	 * Returns true if the given leaf is contained in the provider 
	 * @param descriptor
	 * @return
	 */
	@Override
	public boolean contains( IModelLeaf<IDescriptor> leaf ){
		for (Vertex v : super.getGraph().getVertices()) {
		    IModelLeaf<IDescriptor> node = new OrientDBNode( super.getGraph(), v );
		    if( leaf.getDescriptor().equals( node.getDescriptor() ))
		    	return true;
		}		
		return false;
	}
	@Override
	public void remove(IModelLeaf<IDescriptor> leaf) {
		for (Vertex v : super.getGraph().getVertices()) {
		    IModelLeaf<IDescriptor> result = new OrientDBNode( super.getGraph(), v );
		    if( result.getDescriptor().equals( leaf.getDescriptor() ))
		    	getGraph().removeVertex(v);
		}		
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> get(IDescriptor descriptor) {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		for (Vertex v : super.getGraph().getVertices()) {
		    IModelLeaf<IDescriptor> leaf = new OrientDBNode( super.getGraph(), v );
		    if( leaf.getDescriptor().equals( descriptor ))
		    	results.add( leaf );
		}		
		return results;
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> search(IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter) {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		Iterable<Vertex> iter = super.getGraph().getVertices();
		if( iter.iterator() == null )
			return results;
		Iterator<Vertex> iterator = iter.iterator();
		while( iterator.hasNext() ){
			Vertex child = iterator.next();
			IModelLeaf<IDescriptor> leaf = new OrientDBNode( super.getGraph(), child );
			if( filter.accept( leaf ))
				results.add( leaf );
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	protected boolean accept( IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter, IModelLeaf<IDescriptor> leaf ){
		if( !filter.accept( leaf ))
			return false;
		if( leaf.isLeaf() )
			return true;
		IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) leaf;
		for( IModelLeaf<? extends IDescriptor> child: node.getChildren()){
			if( filter.acceptChild((IModelLeaf<IDescriptor>) child))
				return true;
		}
		return false;
	}

	public boolean add(IModelLeaf<IDescriptor> leaf) {
		try{
			IDescriptor descriptor = leaf.getDescriptor();
			String type = leaf.getType();
			if( StringUtils.isEmpty( type ))
				throw new NullPointerException( S_ERR_NO_TYPE + ": " + descriptor.getID() ); 
					
			cache.add( descriptor );
			Vertex base = super.getGraph().addVertex( descriptor.getName());
			super.createDescriptor( super.getGraph(), base);
			return add( (IModelLeaf<IDescriptor>) leaf, base );
		}
		catch( Exception e ){
			e.printStackTrace();
			super.getGraph().rollback();
			return false;
		}
		finally{
			super.getGraph().commit();
		}
	}

	@SuppressWarnings("unchecked")
	protected boolean add(final IModelLeaf<IDescriptor> leaf, final Vertex parent ) {
		super.getGraph().executeOutsideTx( new OCallable<Object, OrientBaseGraph>(){

			@Override
			public Object call(OrientBaseGraph arg0) {
				Vertex vertex = null;
				Iterator<Vertex> iterator = arg0.getVertices().iterator();
				while( iterator.hasNext() ){
				    Vertex vtx = iterator.next();
					IDescriptor descriptor = new VertexDescriptor( vtx );
					if( leaf.implies(descriptor) != 0 )
						continue;
					vertex = vtx;
					break;
				}
				if( vertex == null ){
					vertex = convert( (OrientGraph) arg0, leaf.getDescriptor(), leaf.getIdentifier() );
				}
			    String identifier = ( Utils.assertNull( leaf.getIdentifier()) ?  S_IS_CHILD: leaf.getIdentifier() );
				String edgeId = parent.getId() + ", " + vertex.getId();
			    arg0.addEdge( edgeId, parent, vertex, identifier );
				boolean retval = true;
			    if( !( leaf instanceof IModelNode) || leaf.isLeaf())
			    	return true;
				IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) leaf;
			    for (IModelLeaf<?> child: node.getChildren()) {
					retval &= add( (IModelLeaf<IDescriptor>) child, vertex );
				}
				return retval;
			}
	    	
	    });
	    return true;
	}

	public boolean delete(IModelLeaf<IDescriptor> model) {
		try{
			String grph_id = model.getDescriptor().get( VertexDescriptor.GRAPH_ID );
			if( Utils.assertNull(grph_id))
				return false;
			Edge remove = null;
			Iterator<Edge> iterator = super.getRoot().getEdges( com.tinkerpop.blueprints.Direction.BOTH, new String[0]).iterator();
			while( iterator.hasNext() ){
				Edge edge = iterator.next();
				if( !grph_id.equals( edge.getVertex( com.tinkerpop.blueprints.Direction.OUT).getId().toString() ))
					continue;
				remove = edge;
				break;
			}
			if( remove != null ){
				super.getGraph().removeEdge(remove);
			}
			return (remove != null );
		}
		catch( Exception e ){
			e.printStackTrace();
			super.getGraph().rollback();
			return false;
		}
		finally{
			super.getGraph().commit();
		}
	}

	public boolean update(IModelLeaf<IDescriptor> root) {
		try{
			Iterator<Vertex> iterator = super.getRoot().getVertices( com.tinkerpop.blueprints.Direction.BOTH, new String[0]).iterator();
			Vertex update = null;
			while( iterator.hasNext() ){
				Vertex vtx = iterator.next();
				IDescriptor descriptor = new VertexDescriptor( vtx );
				if( !descriptor.equals( root.getDescriptor() ))
					return false;
				update = vtx;
				break;
			}
			if( update == null )
				return false;
		}
		catch( Exception e ){
			e.printStackTrace();
			super.getGraph().rollback();
			return true;
		}
		finally{
			super.getGraph().commit();
		}
		return true;
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}
	
	public static void printVertex( Vertex vertex ){
		Iterator<String> iterator = vertex.getPropertyKeys().iterator();
		while( iterator.hasNext() ){
			String key = iterator.next();
			System.out.println( StringStyler.toPackageString( key )  + ": "+ vertex.getProperty( key ) );
		}
	}
}
