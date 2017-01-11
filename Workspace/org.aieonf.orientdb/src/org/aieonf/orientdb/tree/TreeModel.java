package org.aieonf.orientdb.tree;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.orientdb.core.OrientDBNode;
import org.aieonf.orientdb.graph.AbstractOrientGraphModel;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.ITemplateNode;
import org.aieonf.template.builder.TemplateModelBuilderEvent;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class TreeModel<T extends IDescriptor> extends AbstractOrientGraphModel<T, IModelLeaf<IDescriptor>> implements IGraphModel<IModelLeaf<IDescriptor>> {
	
	private ITemplateLeaf<IContextAieon> template;
	
	public TreeModel( ILoaderAieon loader, ITemplateLeaf<IContextAieon> template ) {
		super( loader );
		this.template = template;
	}
	
	@Override
	public IModelLeaf<IDescriptor> create() {
		Vertex root = super.getGraph().getVerticesOfClass( S_ROOT).iterator().next();
		root.setProperty( ConceptBase.getAttributeKey( IDescriptor.Attributes.NAME ), S_ROOT );
		root.setProperty( ConceptBase.getAttributeKey( IDescriptor.Attributes.VERSION ), 1 );
		IModelLeaf<IDescriptor> parent = new OrientDBNode<IDescriptor>( super.getGraph(), root ); 
		this.notifyListeners( new TemplateModelBuilderEvent(this, template, new OrientDBNode<IDescriptor>( super.getGraph(), root )));
		create( root, template );
		return parent;
	}

	@SuppressWarnings({ "unchecked" })
	protected void create( Vertex vertex, ITemplateLeaf<? extends IDescriptor> leaf ) {
		String date = String.valueOf( Calendar.getInstance().getTimeInMillis());
		Vertex child = super.getGraph().addVertex( null );
		IModelLeaf<IDescriptor> node = new OrientDBNode<IDescriptor>( super.getGraph(), child );
		IDescriptor descriptor = node.getDescriptor();
		descriptor.set( ConceptBase.getAttributeKey( IDescriptor.Attributes.CREATE_DATE ), date );
		descriptor.set( IModelLeaf.Attributes.IDENTIFIER, leaf.getIdentifier() );
		super.getGraph().addEdge(null, vertex, child, leaf.getIdentifier());
		this.notifyListeners( new TemplateModelBuilderEvent(this, leaf, new OrientDBNode<IDescriptor>( super.getGraph(), child )));
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
	public boolean contains( IModelLeaf<? extends IDescriptor> leaf ){
		for (Vertex v : super.getGraph().getVertices()) {
		    IModelLeaf<IDescriptor> node = new OrientDBNode<IDescriptor>( super.getGraph(), v );
		    if( leaf.getDescriptor().equals( node.getDescriptor() ))
		    	return true;
		}		
		return false;
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> get(IDescriptor descriptor) {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		for (Vertex v : super.getGraph().getVertices()) {
		    IModelLeaf<IDescriptor> leaf = new OrientDBNode<IDescriptor>( super.getGraph(), v );
		    if( leaf.getDescriptor().equals( descriptor ))
		    	results.add( leaf );
		}		
		return results;
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> search(IModelFilter<IDescriptor> filter) {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		Iterable<Vertex> iter = super.getGraph().getVertices();
		if( iter.iterator() == null )
			return results;
		Iterator<Vertex> iterator = iter.iterator();
		while( iterator.hasNext() ){
			Vertex child = iterator.next();
			IModelLeaf<IDescriptor> leaf = new OrientDBNode<IDescriptor>( super.getGraph(), child );
			if( filter.accept( leaf ))
				results.add( leaf );
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	protected boolean accept( IModelFilter<IDescriptor> filter, IModelLeaf<IDescriptor> leaf ){
		if( !filter.accept( leaf.getDescriptor() ))
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
	
	@Override
	public boolean add(IModelLeaf<IDescriptor> root) {
		try{
			Iterator<Vertex> iterator = super.getRoot().getVertices( com.tinkerpop.blueprints.Direction.BOTH, new String[0]).iterator();
			while( iterator.hasNext() ){
				Vertex vtx = iterator.next();
				IDescriptor descriptor = new VertexDescriptor( vtx );
				if( descriptor.equals( root.getDescriptor() ))
					return false;
			}
			Vertex base = super.getGraph().addVertex(null);
			super.createDescriptor( super.getGraph(), base);
			return add( root, base );
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
	protected boolean add(IModelLeaf<IDescriptor> leaf, Vertex parent ) {
		Vertex vertex = null;
		Iterator<Vertex> iterator = super.getGraph().getVertices().iterator();
		while( iterator.hasNext() ){
		    Vertex vtx = iterator.next();
			IDescriptor descriptor = new VertexDescriptor( vtx );
			if( leaf.implies(descriptor) != 0 )
				continue;
			vertex = vtx;
			break;
		}
		if( vertex == null ){
			vertex = convert( super.getGraph(), leaf.getDescriptor(), leaf.getIdentifier() );
		}
	    String identifier = ( Utils.assertNull( leaf.getIdentifier()) ?  S_IS_CHILD: leaf.getIdentifier() );
		super.getGraph().addEdge( null, parent, vertex, identifier );
		boolean retval = true;
	    if( !( leaf instanceof IModelNode) || leaf.isLeaf())
	    	return true;
		IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) leaf;
	    for (IModelLeaf<?> child: node.getChildren()) {
			retval &= add( (IModelLeaf<IDescriptor>) child, vertex );
		}
	    return retval;
	}

	@Override
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

	@Override
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
