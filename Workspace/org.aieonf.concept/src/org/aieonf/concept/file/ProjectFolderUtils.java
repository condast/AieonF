/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.concept.file;

import java.io.File;
import java.net.URI;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.loader.ILoaderAieon;

public class ProjectFolderUtils {

	public static final String S_SAIGHT = ".saight";
	public static final String S_TEMPLATES = "Templates";
	public static final String S_APPLICATION = "Application";
	public static final String S_CONFIG = "config";
	public static final String S_ADMIN = "admin";
	public static final String S_USER_HOME_PROPERTY = "user.home";
	public static final String S_JXTA_CACHE = "cache";
	
	public static final String S_USER_HOME = "${user.home}";
	public static final String S_BUNDLE_ID = "${bundle-id}";
	
	public static final String S_DEFAULT_EXTENSION = "db";
	public static final String S_SQLITE = "sqlite";

	/**
	 * Return the default user directory. This is '%system-user%\<organisation>\'
	 * @param aieon
	 * @return
	 */
	public static URI getPublicDatabaseDir( String folder )
	{
		File file = new File( S_SAIGHT + File.separator + 
				folder + File.separator );
		return file.toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\'
	 * @param aieon
	 * @return 
	 */
	public static URI getDefaultPublicDatabase( String folder, String name )
	{
		File file = new File( S_SAIGHT + File.separator + folder + 
				File.separator + name + ".sqlite" );
		return file.toURI();
	}

	/**
	 * Return the default template directory. This is '%system-user%\.saight\Templates\'
	 * @param aieon
	 * @return 
	 */
	public static File getDefaultTemplateFolder( String bundle_id, String folder )
	{
		File file = new File( System.getProperty( S_USER_HOME_PROPERTY ) + File.separator +
				S_SAIGHT + File.separator + bundle_id + File.separator + folder );
		return file;
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\'
	 * @param aieon
	 * @return
	 */
	public static URI getParsedUserDir( String str, String bundle_id )
	{
		String parsed = str.replace( S_USER_HOME, "");
		parsed = parsed.replace( S_BUNDLE_ID, "");
		if( parsed.equals(str))
			return URI.create( str );
		
		String[] split = str.split("[/]");
		StringBuffer buffer = new StringBuffer();
		for( String line: split ){
			if( line.equals( S_USER_HOME ))
				buffer.append( System.getProperty( S_USER_HOME_PROPERTY ));
			else if( line.equals( S_BUNDLE_ID ))
				buffer.append( bundle_id );
			else
				buffer.append( line );
			buffer.append( File.separator );
		}
		return new File( buffer.toString() ).toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\'
	 * @param aieon
	 * @return
	 */
	public static URI getDefaultUserDir( ILoaderAieon loader, boolean create )
	{
		String folder = loader.getSource();
		String name = loader.getIdentifier();
		File file = new File( System.getProperty( S_USER_HOME_PROPERTY ) + File.separator + 
				S_SAIGHT + File.separator +
				folder + File.separator + StringStyler.prettyString( name ));
		if( create & !file.exists())
			file.mkdirs();
		return file.toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\<name>.sqlite'
	 * @param aieon
	 * @return 
	 */
	public static URI getDefaultUserDatabase( ILoaderAieon loader, String extension)
	{
		String folder = loader.getSource();
		String name = loader.getIdentifier();
		String ext = ( Utils.isNull( extension ))? S_DEFAULT_EXTENSION: extension;
		File file = new File( System.getProperty( S_USER_HOME_PROPERTY ) + File.separator + 
				S_SAIGHT + File.separator +
				folder + File.separator +  StringStyler.prettyString( name ) + "." + ext );
		return file.toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\<name>.sqlite'
	 * @param aieon
	 * @return 
	 */
	public static URI getDefaultUserDatabase( ILoaderAieon loader){
		return getDefaultUserDatabase(loader, S_DEFAULT_EXTENSION );
	}
	
	/**
	 * Return the default user directory. This is '%system-user%\<folder>\<name>\config'
	 * @param aieon
	 * @return 
	 */
	public static URI getDefaultJxseDir( String folder, String name )
	{
		File file = new File( System.getProperty( S_USER_HOME_PROPERTY ) + File.separator + 
				folder + File.separator + StringStyler.prettyString( name ) + File.separator + S_JXTA_CACHE);
		return file.toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\'
	 * @param aieon
	 * @return
	 */
	public static URI appendToUserDir( String path, boolean create )
	{
		File file = new File( System.getProperty( S_USER_HOME_PROPERTY ) + File.separator + 
				path);
		if( create & !file.exists())
			file.mkdirs();
		return file.toURI();
	}

}
