package org.aieonf.orientdb.cache;

import java.io.Closeable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.aieonf.concept.IDescriptor;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;

public class CacheController<T extends IDescriptor> implements Closeable{

	private boolean open;
	private String url;
	private String userName, password;
	private ODatabaseDocumentTx db;
	
	public CacheController() {
		this.open = false;
	}
	
	public void connect( String url, String username, String password ){
		this.url = url;
		this.userName = username;
		this.password = password;
	}
	
	public void disconnect(){
		this.url = null;
		this.userName = null;
		this.password = null;
	}
	
	public void open(){
		try{
			db = new ODatabaseDocumentTx( this.url );
			db.open( this.userName, this.password );
			this.open = true;
		}
		catch( Exception ex ){
			ex.printStackTrace();
			db.close();
		}		
	}
	
	public boolean isOpen(){
		return open;
	}
	
	/**
	 * Add a descriptor to the cache
	 * @param descriptor
	 */
	public void add( T descriptor ){
		ODocument doc = new ODocument( descriptor.toString());
		Iterator<String> iterator = descriptor.iterator();
		while( iterator.hasNext() ){
			String field = iterator.next();
			doc.field( field, descriptor.get( field ));
		}
		doc.save();
	}

	public Collection<T> query( String query ){
		//List<ODocument> results = db.query(
		//		new OSQLQuery<ODocument>( query ), null
		//		);	
		
		return null;///results;
	}

	/**
	 * Add a descriptor to the cache
	 * @param descriptor
	 */
	public void updateDescriptor( T descriptor ){
		//db.qu
		ODocument doc = new ODocument( descriptor.toString());
		Iterator<String> iterator = descriptor.iterator();
		while( iterator.hasNext() ){
			String field = iterator.next();
			doc.field( field, descriptor.get( field ));
		}
		doc.save();
	}

	@Override
	public void close(){
		db.close();
	}

}
