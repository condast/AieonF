package org.aieonf.concept.wrapper;

import org.aieonf.commons.tree.IChildObject;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public class ChildDescriptor<T extends IDescribable> extends DescriptorWrapper implements IChildObject<T>, IDescriptor {

	private static final long serialVersionUID = -3712077207177738130L;

	private T parent;
	
	public ChildDescriptor( T parent, IDescriptor descriptor) {
		super( descriptor );
		this.parent = parent;
	}

	@Override
	public T getParent() {
		return parent;
	}
}