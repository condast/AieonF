package org.aieonf.model.provider;

public interface IWriteModelProvider<U extends Object> extends IModelProvider<U>{

	public U create();

	public boolean add( U root );

	public boolean delete( U root );

	public boolean update( U root );
}