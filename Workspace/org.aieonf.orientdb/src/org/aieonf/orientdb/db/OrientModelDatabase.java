package org.aieonf.orientdb.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.parser.AbstractModelParser;
import org.aieonf.model.parser.IModelParser;
import org.aieonf.model.parser.ParseEvent;
import org.aieonf.model.provider.IModelDatabase;
import org.aieonf.orientdb.cache.CacheService;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OrientModelDatabase implements IModelDatabase<IDescriptor, IModelLeaf<IDescriptor>> {

	private static DatabaseService service = DatabaseService.getInstance();

	private Collection<IModelListener<IModelLeaf<IDescriptor>>> listeners;
	
	private String identifier;
	private IDomainAieon domain;
	
	private Collection<IDescriptor> descriptors;
	private Map<IModelLeaf<IDescriptor>, Vertex> vertices;
	
	public OrientModelDatabase( String identifier, IDomainAieon domain ) {
		this.identifier = identifier;
		this.domain = domain;
		listeners = new ArrayList<>();
		descriptors = new ArrayList<>();
		vertices = new HashMap<>();
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public void open(IDescriptor key) {
		service.open();
	}

	@Override
	public boolean isOpen() {
		return service.isOpen();
	}

	@Override
	public void close() {
		service.close();
	}

	@Override
	public void addListener(IModelListener<IModelLeaf<IDescriptor>> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(IModelListener<IModelLeaf<IDescriptor>> listener) {
		this.listeners.remove(listener);
	}
	
	protected void notifyListeners( ModelEvent<IModelLeaf<IDescriptor>> event ) {
		for( IModelListener<IModelLeaf<IDescriptor>> listener: listeners )
			listener.notifyChange(event);
	}

	@Override
	public boolean add(IModelLeaf<IDescriptor> leaf) {
		boolean result = false;
		IModelParser<IDescriptor, IModelLeaf<IDescriptor>> parser = new ModelParser();
		CacheService cache = service.getCache();
		try{
			descriptors.clear();
			parser.addListener( e->onAddDescriptors(e));
			parser.parseModel(leaf);
			cache.open();
			cache.add(descriptors.toArray( new IDescriptor[ descriptors.size()]));
			result = true;
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		finally {
			cache.close();
		}
		if( !result )
			return false;
		try{
			vertices.clear();
			parser.addListener( e->onAddModels(e));
			parser.parseModel(leaf);
			service.open();
			cache.add(descriptors.toArray( new IDescriptor[ descriptors.size()]));
			result = true;
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		finally {
			service.close();
		}		
		return result;
	}

	private void onAddDescriptors( ParseEvent<IDescriptor, IModelLeaf<IDescriptor>> event ) {
		descriptors.add(event.getModel().getData());
	}

	private void onAddModels( ParseEvent<IDescriptor, IModelLeaf<IDescriptor>> event ) {
		IModelLeaf<IDescriptor> leaf = event.getModel();
		IModelNode<?> parent = leaf.getParent();
		leaf.set(IDescriptor.DESCRIPTOR, leaf.getData().getID());
		try {
			OrientGraph graph = service.getGraph();
			Vertex pvertex = vertices.get( parent );
			Vertex vertex = vertices.get( leaf );
			if( vertex == null )
				vertex = graph.addVertex(null);
			if( parent != null ) {
				String identifier = parent.getChildIdentifier(leaf);
				Edge edge = graph.addEdge(null, pvertex, vertex, identifier);
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	@Override
	public void sync() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(IModelLeaf<IDescriptor> leaf) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> get(IDescriptor descriptor) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> search(IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deactivate() {
		service.close();
	}

	@Override
	public boolean hasFunction(String function) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(IModelLeaf<IDescriptor> leaf) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(IModelLeaf<IDescriptor> leaf) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class ModelParser extends AbstractModelParser<IDescriptor, IModelLeaf<IDescriptor>>{

		@Override
		protected boolean onPrepare(ParseTypes type, ParseEvent<IDescriptor, IModelLeaf<IDescriptor>> event) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean onBusy(ParseTypes type, ParseEvent<IDescriptor, IModelLeaf<IDescriptor>> event, String key,
				String value) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean onComplete(ParseTypes type, ParseEvent<IDescriptor, IModelLeaf<IDescriptor>> event) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
