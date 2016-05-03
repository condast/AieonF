package org.aieonf.commons.hex;

import java.io.*;
import java.util.*;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>
 * Creates a fixed size long hash number from a byte array or a String that is
 * unique for every byte array up to the given size
 * </p>
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class Hasher
{

  //The hash that is created
  private long hash;

  /**
   * Create a hsh number with the given bit size for the given byte array
   * @param bytes byte[]
   * @throws Exception
  */
  public Hasher( byte[] bytes) throws Exception
  {
    super();
    this.hash = this.calculate( bytes );
  }

  /**
   * Create a hsh number with the given bit size for the given text
   * @param bytes byte[]
   * @throws Exception
  */
  public Hasher( String text ) throws Exception
  {
    this( text.getBytes() );
  }

  /**
   * Create a hash number for the given input stream
   * @param stream InputStream
   * @throws Exception
   */
  public Hasher( InputStream stream ) throws Exception
  {
    super();

    byte[] buf = new byte[ 1024 ];
    List<Byte> bytes = new ArrayList<Byte>();
    int len;
    while(( len = stream.read( buf )) != -1 ){
      for( int i = 0; i < len; i++ ){
        bytes.add(buf[ i ]);
      }
    }
    this.hash = this.calculate( bytes );
  }

  /**
   * Get the hash
   * @return long
   */
  public long getHash()
  {
    return hash;
  }

  /**
   * Calculate the hash number from a given byte array
   * @param bytes byte[]
   * @return long
   * @throws Exception
   */
  protected long calculate( List<Byte> bytes ) throws Exception
  {
    long counter = 0;
    for( int i = 0; i < bytes.size(); i++ ){
      if( counter > Long.MAX_VALUE - bytes.get( i )){
        counter = bytes.get( i );
        continue;
      }
      counter += bytes.get( i );
      //System.out.print( "byte " + bytes.get( i ) + ", counter " + counter + " | " );
    }
    return counter;
  }

  /**
   * Calculate String from a given byte array
   * @param bytes byte[]
   * @return String
   * @throws Exception
   */
  protected long calculate( byte[] bytes ) throws Exception
  {
    long counter = 0;
    for( int i = 0; i < bytes.length; i++ ){
      if( counter > Long.MAX_VALUE - bytes[ i ]){
        counter = bytes[ i ];
        continue;
      }
      counter += bytes[ i ];
    }
    return counter;
  }
}
