package org.aieonf.model.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelException;
import org.aieonf.model.core.ModelLeaf;

public abstract class AbstractModelParserEngine<T extends IDescriptor, U extends Object> implements IModelParserEngine<T, U>
{
	protected List<U> listeners;
	
	protected IModelNode<T> root;
	
	private Search search;
	
	public AbstractModelParserEngine( IModelNode<T> root ){
		this( root, Search.DFS );
	}

	public AbstractModelParserEngine( IModelNode<T> root, Search search ){
		this.root = root;
		this.search = search;
		this.listeners = new ArrayList<U>();
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.model.parser.IModelParserEngine#getSearch()
	 */
	@Override
	public Search getSearch()
	{
		return search;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(Search search)
	{
		this.search = search;
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.model.parser.IModelParserEngine#addListener(U)
	 */
	@Override
	public void addListener( U listener ){
		this.listeners.add( listener );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.model.parser.IModelParserEngine#removeListener(U)
	 */
	@Override
	public void removeListener( U listener ){
		this.listeners.remove( listener );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.model.parser.IModelParserEngine#parse()
	 */
	@Override
	public void parse() throws ModelException{
		switch( this.search ){
			case BFS:
				this.parseBFS();
				break;
			default:
				this.parseDFS();
				break;
		}
	}
	
	/**
	 * Parse the listeners for the given model node
	 * @param node
	 * @throws ModelException
	 */
	protected abstract void parseListeners( IModelLeaf<?> node ) throws ModelException;
	
	/**
	 * Parse the model according to a Breadth First Search
	 * @throws ModelException
	 */
	protected void parseBFS() throws ModelException
	{
		parseListeners( root );
		this.parseBFS( root.getChildren() );
	}

	/**
	 * Parse the collection according to a Breadt First Search
	 * @param nodes
	 * @throws ModelException
	 */
	protected void parseBFS( Collection<IModelLeaf<? extends IDescriptor>> nodes ) throws ModelException
	{
		List<IModelLeaf<? extends IDescriptor>> children = new ArrayList<IModelLeaf<? extends IDescriptor>>();
		for( IModelLeaf<?> node: nodes ){
			parseListeners( node );
			if( ModelLeaf.hasChildren( node ))
				children.addAll( ModelLeaf.getChildren( node ));
		}
		this.parseBFS( children );
	}

	/**
	 * Parse the model according to a depth first search
	 * @throws ModelException
	 */
	protected void parseDFS() throws ModelException
	{
		this.parseDFS(root);
	}

	/**
	 * Parse the given node, according to a depth first search
	 * @param node
	 * @throws ModelException
	 */
	protected void parseDFS( IModelLeaf<?> node ) throws ModelException
	{
		parseListeners( node );
		for( IModelLeaf<?> child: ModelLeaf.getChildren( node )){
			this.parseDFS(child);
		}
	}
}