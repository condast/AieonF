package org.aieonf.util.petition;

public class Petition<T,U extends Object> implements IPetition<T,U>
{
	private T petition;
	private U data;
	
	public Petition( T petition )
	{
		this.petition = petition;
	}
	
	public Petition( T petition, U data )
	{
		this.petition = petition;
		this.data = data;
	}

	@Override
	public T getPetition()
	{
		return this.petition;
	}

	@Override
	public U getData()
	{
		return this.data;
	}

}
