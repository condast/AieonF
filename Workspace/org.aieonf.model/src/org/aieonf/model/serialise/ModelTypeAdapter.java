package org.aieonf.model.serialise;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelLeaf;

public class ModelTypeAdapter extends AbstractModelTypeAdapter<IModelLeaf<IDescriptor>, IDescriptor> {
	
	public ModelTypeAdapter() {
		super();
	}

	@Override
	protected IModelLeaf<IDescriptor> onTransform(IModelLeaf<IDescriptor> node) {
		return node;
	}

	@Override
	protected IModelLeaf<IDescriptor> onCreateNode(long id, boolean leaf) {
		return leaf? new ModelLeaf<IDescriptor>( id ): new Model<IDescriptor>( id );
	}

	@Override
	protected boolean onAddChild(IModelLeaf<IDescriptor> model, IModelLeaf<IDescriptor> child,
			String label) {
		IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) model;
		return node.addChild( child, label);
	}

	@Override
	protected IDescriptor onSetDescriptor(IModelLeaf<IDescriptor> node, IConceptBase base ) {
		IDescriptor descriptor = new Descriptor();
		node.setData(descriptor);
		return descriptor;
	}

	@Override
	protected boolean onAddProperty(IModelLeaf<IDescriptor> node, String key, String value) {
		node.set(key, value);
		return true;
	}
}
