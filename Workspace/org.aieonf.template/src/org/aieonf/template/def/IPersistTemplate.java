package org.aieonf.template.def;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.persistence.IPersistModelFactory;

public interface IPersistTemplate<T extends IModelLeaf< ? extends IDescriptor>> extends IPersistModelFactory<T>
{

}
