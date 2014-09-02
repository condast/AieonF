package org.aieonf.util.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.aieonf.util.logger.Logger;

public class ObjectPersistence<T extends Object> implements IPersistence<T>
{
	//open or close the persistence
	private boolean open;
	
	//The supported streams
	private ObjectInputStream oin;
	private ObjectOutputStream oOut;
	
	private Logger logger;
	
	/**
	 * Create the object persistence
	*/
	public ObjectPersistence()
	{
		this.open = false;
		this.oin = null;
		this.oOut = null;	
		this.logger = Logger.getLogger( this.getClass() );
	}
	
	/**
	 * Close the persistence
	*/
	@Override
	public void close()
	{
		this.open = false;
		try{
			if( this.oin != null )
				this.oin.close();
			if( this.oOut != null ){
				this.oOut.flush();
				this.oOut.close();
			}
		}
		catch( IOException ex ){
			logger.error( ex.getMessage(), ex );
		}
		finally{
			this.oin = null;
			this.oOut = null;
		}
	}

	@Override
	public boolean isOpen()
	{
		return this.open;
	}

	@Override
	public void open()
	{
		this.open = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T read(InputStream in) throws IOException
	{
		this.oin = new ObjectInputStream( in );
		try{
			Object obj = this.oin.readObject();
			return ( T )obj;
		}
		catch( ClassNotFoundException ex ){
			throw new IOException( ex.getMessage(), ex );
		}
		finally{
			if( this.oin != null )
				this.oin.close();
		}
	}

	/**
	 * Write the wrapped object
	*/
	@Override
	public void write( T descriptor, OutputStream out) throws IOException
	{
		this.oOut = new ObjectOutputStream( out );
		this.oOut.writeObject( descriptor );
		this.oOut.flush();
	}
}
