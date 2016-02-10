package org.aieonf.template;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IPersistModelFactory;

public interface IPersistTemplate<T extends IModelLeaf< ? extends IDescriptor>> extends IPersistModelFactory<T>
{

}
