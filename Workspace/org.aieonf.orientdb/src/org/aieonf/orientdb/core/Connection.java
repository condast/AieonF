package org.aieonf.orientdb.core;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

public class Connection {

	public static final String S_DATABASE_LOCATION = "";
	
	private ODatabaseDocumentTx db;
	
	public boolean connect(){
		db = new ODatabaseDocumentTx
				   ("plocal:R:/tmp/test");
		db.open("admin", "admin_passwd");
		return false;
	}

	public boolean disconnect(){
		db.close();
		return true;
	}
}
