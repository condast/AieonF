package org.aieonf.template;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelNode;

public interface ITemplateNode<T extends IDescriptor> extends ITemplateLeaf<T>, IModelNode<T>{}
