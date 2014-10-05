package org.aieonf.template;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelProvider;

public interface IWriteModelProvider<T extends IDescriptor, U extends Object> extends IModelProvider<T, U>{

	public U create( ITemplateLeaf<T> template );

	public boolean add( U root );

	public boolean delete( U root );

	public boolean update( U root );
}