package org.aieonf.commons.ui.table;

import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ModelProvider implements ITreeContentProvider {
	private static final long serialVersionUID = 4294651251926296097L;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(!( parentElement instanceof IModelLeaf ))
			return null;
		IModelLeaf<?> leaf = (IModelLeaf<?>) parentElement;
		if( leaf.isLeaf() )
			return new Object[0];
		IModelNode<?> node = (IModelNode<?>) leaf;
		Collection<IModelLeaf<? extends IDescriptor>> children = node.getChildren().keySet(); 
		return children.toArray( new Object[ children.size()] );
	}

	@Override
	public Object getParent(Object element) {
		if(!( element instanceof IModelLeaf ))
			return null;
		IModelLeaf<?> leaf = (IModelLeaf<?>) element;
		if( leaf.isRoot() )
			return null;
		return leaf.getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		if(!( element instanceof IModelLeaf ))
			return false;
		IModelLeaf<?> leaf = (IModelLeaf<?>) element;
		if( leaf.isLeaf() )
			return false;
		IModelNode<?> node = (IModelNode<?>) leaf;
		return node.hasChildren();
	}
}
