package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.graph.IGraphModel;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.orientdb.core.OrientDBNode;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.ITemplateNode;
import org.aieonf.template.builder.TemplateModelBuilderEvent;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

public class GraphModel<T extends IDescriptor> extends AbstractOrientGraphModel<T,IVertex<T>> implements IGraphModel<IVertex<T>> {
	
	private OrientGraphFactory factory;
	private OrientGraph graph;
	private ITemplateLeaf<IContextAieon> template;
	
	public GraphModel( ILoaderAieon loader, ITemplateLeaf<IContextAieon> template ) {
		super( loader );
		this.template = template;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IVertex<T> create() {
		Vertex root = graph.getVerticesOfClass( S_ROOT).iterator().next();
		root.setProperty( ConceptBase.getAttributeKey( IDescriptor.Attributes.NAME ), S_ROOT );
		root.setProperty( ConceptBase.getAttributeKey( IDescriptor.Attributes.VERSION ), 1 );
		IVertex<T> parent = new VertexImpl<T>( root ); 
		IModelLeaf<? extends IDescriptor>[] models = new IModelLeaf[1];
		models[0] = new OrientDBNode<IDescriptor>( graph, root );
		this.notifyListeners( new TemplateModelBuilderEvent(this, template, models));
		create( root, template );
		return parent;
	}

	@SuppressWarnings({ "unchecked" })
	protected void create( Vertex vertex, ITemplateLeaf<? extends IDescriptor> leaf ) {
		String date = String.valueOf( Calendar.getInstance().getTimeInMillis());
		Vertex child = graph.addVertex( null );
		IVertex<T> vtx = new VertexImpl<T>( child );
		IDescriptor descriptor = vtx.get();
		descriptor.set( IDescriptor.Attributes.CREATE_DATE, date );
		descriptor.set( IModelLeaf.Attributes.IDENTIFIER, leaf.getIdentifier() );
		graph.addEdge(null, vertex, child, leaf.getIdentifier());
		this.notifyListeners( new TemplateModelBuilderEvent(this, leaf, new OrientDBNode<IDescriptor>( graph, child )));
		if( !leaf.isLeaf()){
			ITemplateNode<IDescriptor> node = (ITemplateNode<IDescriptor>) leaf;
			for( IModelLeaf<? extends IDescriptor> next: node.getChildren() )
				create( child, (ITemplateLeaf<? extends IDescriptor>) next );
		}
	}

	@Override
	public boolean contains(IModelLeaf<?> leaf) {
		for (Vertex v : graph.getVertices()) {
		    IVertex<T> vtx = new VertexImpl<T>( v );
		    if( vtx.get().equals( leaf.getDescriptor() ))
		    	return true;
		}		
		return false;
	}

	@Override
	public Collection<IVertex<T>> get(IDescriptor descriptor) {
		Collection<IVertex<T>> results = new ArrayList<IVertex<T>>();
		for (Vertex v : graph.getVertices()) {
		    IVertex<T> vtx = new VertexImpl<T>( v, new VertexImpl.VertexConcept( v ));
		    if( vtx.get().equals( descriptor ))
		    	results.add( vtx );
		}		
		return results;
	}

	@Override
	public Collection<IVertex<T>> search(IModelFilter<IDescriptor> filter) {
		Collection<IVertex<T>> results = new ArrayList<IVertex<T>>();
		Iterable<Vertex> iter = graph.getVertices();
		if( iter.iterator() == null )
			return results;
		Iterator<Vertex> iterator = iter.iterator();
		while( iterator.hasNext() ){
			Vertex child = iterator.next();
			IVertex<T> vtx = new VertexImpl<T>( child, new VertexImpl.VertexConcept( child ));
			if( filter.accept( vtx.get() ))
				results.add( vtx );
		}
		return results;
	}

	@Override
	public boolean add(IVertex<T> root) {
		try{
			IModelNode<T> model = new OrientDBNode<T>( this.graph, (Vertex) root );
			return ( model != null );
		}
		catch( Exception e ){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(IVertex<T> root) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(IVertex<T> root) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deactivate() {
		if( factory != null )
			factory.close();
	}
}
