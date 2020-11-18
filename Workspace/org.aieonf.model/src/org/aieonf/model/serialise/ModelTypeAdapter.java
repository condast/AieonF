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
	protected String getID(IModelLeaf<IDescriptor> node) {
		return String.valueOf( node.getID() );
	}

	@Override
	protected IModelLeaf<IDescriptor> onTransform(IModelLeaf<IDescriptor> node) {
		return node;
	}

	@Override
	protected IModelLeaf<IDescriptor> onCreateNode( long id, IConceptBase base, boolean leaf, IDescriptor descriptor) {
		base.set(IDescriptor.Attributes.ID.name(), String.valueOf(id));
		IModelLeaf<IDescriptor> result = leaf? new ModelLeaf<IDescriptor>( base ): new Model<IDescriptor>( base );
		result.setData(descriptor);
		return result;
	}

	@Override
	protected boolean onAddChild(IModelLeaf<IDescriptor> model, IModelLeaf<IDescriptor> child,
			boolean reverse, String label) {
		IModelNode<IDescriptor> node = null;
		boolean result = false;
		if( reverse ) {
			node = (IModelNode<IDescriptor>) child;
			node.addChild( model, label);
		}else {
			node = (IModelNode<IDescriptor>) model;
			node.addChild( child, label);
		}
		return result;
	}

	@Override
	protected IDescriptor onCreateDescriptor(IConceptBase base ) {
		IDescriptor descriptor = new Descriptor( base );
		return descriptor;
	}
}
