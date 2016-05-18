package org.aieonf.template;

import org.aieonf.model.IModelProvider;

public interface IWriteModelProvider<U extends Object> extends IModelProvider<U>{

	public U create();

	public boolean add( U root );

	public boolean delete( U root );

	public boolean update( U root );
}