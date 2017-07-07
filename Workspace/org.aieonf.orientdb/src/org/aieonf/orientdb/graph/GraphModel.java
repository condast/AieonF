package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.graph.IGraphModelProvider;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.ModelLeaf;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.orientdb.core.OrientDBNode;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.ITemplateNode;
import org.aieonf.template.builder.TemplateModelBuilderEvent;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

public class GraphModel extends AbstractOrientGraphModel<IDomainAieon, IModelLeaf<IDescriptor>> implements IGraphModelProvider<IDomainAieon, IModelLeaf<IDescriptor>> {
	
	private OrientGraphFactory factory;
	private OrientGraph graph;
	private ITemplateLeaf<IContextAieon> template;
	
	public GraphModel( ILoaderAieon loader, ITemplateLeaf<IContextAieon> template ) {
		super( loader );
		this.template = template;
	}

	@Override
	public boolean hasFunction(String function) {
		return DefaultModels.GRAPH.toString().equals( function );
	}

	public IDescriptor create() {
		Vertex root = graph.getVerticesOfClass( S_ROOT).iterator().next();
		root.setProperty( ConceptBase.getAttributeKey( IDescriptor.Attributes.NAME ), S_ROOT );
		root.setProperty( ConceptBase.getAttributeKey( IDescriptor.Attributes.VERSION ), 1 );
		IDescriptor parent = (IDescriptor) new VertexImpl( root ); 
		IModelLeaf<IDescriptor> model =  new OrientDBNode( graph, root );
		TemplateModelBuilderEvent<IContextAieon, IModelLeaf<IDescriptor>> event = new TemplateModelBuilderEvent<IContextAieon, IModelLeaf<IDescriptor>>( this, template, model );
		super.notifyListeners( event );
		create( root, template );
		return parent;
	}

	@SuppressWarnings({ "unchecked" })
	protected void create( Vertex vertex, ITemplateLeaf<? extends IDescriptor> leaf ) {
		String date = String.valueOf( Calendar.getInstance().getTimeInMillis());
		Vertex child = graph.addVertex( null );
		IVertex<IDescriptor> vtx = new VertexImpl( child );
		IDescriptor descriptor = vtx.get();
		descriptor.set( IDescriptor.Attributes.CREATE_DATE, date );
		descriptor.set( IModelLeaf.Attributes.IDENTIFIER, leaf.getIdentifier() );
		graph.addEdge(null, vertex, child, leaf.getIdentifier());
		this.notifyListeners( new TemplateModelBuilderEvent<IContextAieon, 
				IModelLeaf<IDescriptor>>(this, (ITemplateLeaf<IContextAieon>) leaf, new OrientDBNode( graph, child )));
		if( !leaf.isLeaf()){
			ITemplateNode<IDescriptor> node = (ITemplateNode<IDescriptor>) leaf;
			for( IModelLeaf<? extends IDescriptor> next: node.getChildren() )
				create( child, (ITemplateLeaf<? extends IDescriptor>) next );
		}
	}

	@Override
	public boolean contains(IModelLeaf<IDescriptor> leaf) {
		for (Vertex v : graph.getVertices()) {
		    IVertex<IDescriptor> vtx = new VertexImpl( v );
		    if( vtx.get().equals( leaf.getDescriptor() ))
		    	return true;
		}		
		return false;
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> get(IDescriptor descriptor) {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		for (Vertex v : graph.getVertices()) {
		    IVertex<IDescriptor> vtx = new VertexImpl( v, new VertexImpl.VertexConcept( v ));
		    if( vtx.get().equals( descriptor ))
		    	results.add( new ModelLeaf<IDescriptor>( vtx.get() ));
		}		
		return results;
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> search(IModelFilter<IDescriptor> filter) {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		Iterable<Vertex> iter = graph.getVertices();
		if( iter.iterator() == null )
			return results;
		Iterator<Vertex> iterator = iter.iterator();
		while( iterator.hasNext() ){
			Vertex child = iterator.next();
			IVertex<IDescriptor> vtx = new VertexImpl( child, new VertexImpl.VertexConcept( child ));
			if( filter.accept( vtx.get() ))
				results.add( new ModelLeaf<IDescriptor>( vtx.get() ));
		}
		return results;
	}

	

	@Override
	public void deactivate() {
		if( factory != null )
			factory.close();
	}
}
