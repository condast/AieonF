package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.orientdb.core.ModelNode;
import org.aieonf.orientdb.db.DatabaseService;

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
	
	public ModelFactory( DatabaseService service ) {
		this.service = service;
	}
	

	public Collection<IModelLeaf<IDescriptor>> get( Collection<Vertex> vertices ) throws FilterException {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<>();
		try {
			OrientGraph graph = this.service.getGraph();
			IModelLeaf<IDescriptor> node = null;
			for( Vertex vertex: vertices ) {
				try {
					node = new ModelNode( graph, vertex );
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
		return results;
	}
}
