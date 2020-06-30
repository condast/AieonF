package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.serialise.SerialisableModel;
import org.aieonf.orientdb.core.ModelNode;
import org.aieonf.orientdb.db.DatabaseService;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelFactory< T extends IDescriptor > {

	public static final String S_CLASS = "class:";

	public enum Attributes{
		P,//Properties of the model
		D,//Descriptor
		C;//Children

		public static boolean isAttribute( String str ) {
			for( Attributes attr: values() ){
				if( attr.name().equals(str.toUpperCase()))
					return true;
			}
			return false;
		}
	}

	private DatabaseService service;
	
	private IDomainAieon domain;
		
	public ModelFactory( IDomainAieon domain, DatabaseService service ) {
		this.domain = domain;
		this.service = service;
	}
	

	public Collection<IModelLeaf<IDescriptor>> get( Collection<Vertex> vertices ) throws FilterException {
		this.service.open( domain );
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<>();
		try {
			OrientGraph graph = this.service.getGraph();
			for( Vertex vertex: vertices ) {
				try {
					IModelLeaf<IDescriptor> node = new ModelNode( graph, domain, vertex );
					results.add(node);
				}
				catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			service.close();
		}
		return results;
	}
	
	protected class SerialisableNode extends SerialisableModel{

		public SerialisableNode( Vertex descriptor ) {
			super();
			for( String key: descriptor.getPropertyKeys())
				super.putDescriptor( key, descriptor.getProperty(key));
			Iterator<Edge> edges = descriptor.getEdges(Direction.BOTH, IDescriptor.DESCRIPTOR).iterator();
			if(!edges.hasNext())
				return;
			Vertex root = edges.next().getVertex(Direction.OUT);
			for( String key: root.getPropertyKeys())
				super.put( key, root.getProperty(key));
			edges = root.getEdges(Direction.OUT).iterator();
			while( edges.hasNext() ) {
				Edge edge = edges.next();
				Vertex vertex = edge.getVertex(Direction.IN); 
				if( !vertex.equals(descriptor))
					super.addChild(new SerialisableNode( vertex ), edge.getLabel());
			}
		}		
	}
}
