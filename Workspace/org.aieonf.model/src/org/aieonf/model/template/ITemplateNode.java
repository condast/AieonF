package org.aieonf.model.template;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelNode;

public interface ITemplateNode<T extends IDescriptor> extends ITemplateLeaf<T>, IModelNode<T>{}
