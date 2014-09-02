package org.aieonf.util.logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.*;

public class Logger
{
	private java.util.logging.Logger logger;
	
	private String name;
	
	private Class<?> clss;
	
	//Maintain a list of loggers
	private static List<ILogger> loggers;

  /**
   * Create a log 4 j facade for the default java logger
   *
   * @param logger Logger
  */
  protected Logger( Class<?> clss, java.util.logging.Logger logger  )
	{
		if( clss == null )
			throw new NullPointerException( "No class added for logger" );
		
  	if( loggers == null )
			loggers = Collections.synchronizedList( new ArrayList<ILogger>() );
		this.clss = clss;
		this.logger = logger;
		this.name = logger.getName();
		this.logger.setLevel( Level.FINEST );
		this.trace( "Logger started" );
	}

  /**
   * Print a trace
   *
   * @param message String
  */
  public void trace( String message )
	{
		this.logger.log( Level.FINER, this.name + ":\n\t" + message );
		this.notifyListeners( ILogger.Level.Trace, message );
	}

  /**
   * Print a debug message
   *
   * @param message String
  */
  public void debug( String message )
	{
		this.logger.log( Level.FINE, this.name + ":\n\t" + message );
		this.notifyListeners( ILogger.Level.Debug, message );
	}

  /**
   * Print a debug message
   *
   * @param message String
  */
  public void info( String message )
	{
		this.logger.info( this.name + ":\n\t" + message );
		this.notifyListeners( ILogger.Level.Info, message );
	}

  /**
   * Print a warning
   *
   * @param message String
  */
  public void warning( String message )
	{
		String warning = this.name + ":\n\t" + message;
		this.logger.log( Level.WARNING, warning );
		this.notifyListeners( ILogger.Level.Error, warning );
	}

  /**
   * Print a warning and a printstack trace
   *
   * @param meesage String
   * @param thrw Throwable
  */
  public void warning( String message, Throwable thrw )
	{
		this.logger.warning( message );
		this.notifyListeners( ILogger.Level.Warning, message, thrw );
		thrw.printStackTrace();
	}

  /**
   * Print an error
   *
   * @param message String
  */
  public void error( String message )
	{
		String error = this.name + ":\n\t" + message;
  	this.logger.log( Level.SEVERE, error );
		this.notifyListeners( ILogger.Level.Error, error );
	}

  /**
   * Print an error and a printstack trace
   *
   * @param message String
   * @param thrw Throwable
  */
  public void error( String message, Throwable thrw )
	{
		this.logger.log( Level.SEVERE, message );
		this.notifyListeners( ILogger.Level.Error, message, thrw );
		thrw.printStackTrace();
	}

  /**
   * Print a fatal error
   *
   * @param message String
  */
  public void fatal( String message )
	{
		String error = this.name + ":\n\t" + message;
  	this.logger.log( Level.SEVERE, error );
		this.notifyListeners( ILogger.Level.Fatal, error );
	}

  /**
   * Print a fatal error and a print stack trace
   *
   * @param message String
   * @param thrw Throwable
  */
  public void fatal( String message, Throwable thrw )
	{
		this.logger.log( Level.SEVERE, message );
		this.notifyListeners( ILogger.Level.Fatal, message, thrw );
		thrw.printStackTrace();
	}

  /**
   * Notify listeners of an occurring message
   * @param level
   * @param message
   */
  protected void notifyListeners( ILogger.Level level, String message )
  {
  	for( ILogger logger: loggers )
  		logger.handleMessage( level, clss, message );
  }

  /**
   * Notify listeners of an occurring message
   * @param level
   * @param message
   */
  protected void notifyListeners( ILogger.Level level, String message, Throwable thrw )
  {
  	for( ILogger logger: loggers )
  		logger.handleError( level, clss, message, thrw );
  }

  /**
   * Add a logger
   * @param logger
  */
  public static void addLogger( ILogger logger )
  {
  	if( loggers == null )
  		return;
  	loggers.add( logger );
  }
  
  /**
   * Remove a logger
   * @param logger
  */
  public static void removeLogger( ILogger logger )
  {
  	if( loggers == null )
  		return;
  	loggers.remove( logger );
  }

  /**
   * Get the logger
   *
   * @param clss Class
   * @return Logger
  */
  public static Logger getLogger( Class<?> clss )
	{
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger( clss.getName() );
		logger.finest("Getting logger");
		return new Logger( clss, logger );
	}

  /**
   * Get the resource bundle name
   *
   * @param name String
   * @param resourceBundleName String
   * @return Logger
  */
  public static Logger getLogger( Class<?> clss, String resourceBundleName )
	{
		return new Logger( clss, java.util.logging.Logger.getLogger( clss.getCanonicalName(), resourceBundleName ));
	}
  
  /**
   * Log the specific type
   * @param type
   * @param request
   * @param moment
   */
  public void time( String type, String request, String moment )
  {
  	if( type.equals(request) == false )
  		return;
  	String str = "TIME (" + moment + ")";
  	logger.info( str + request );
  }
  
}
