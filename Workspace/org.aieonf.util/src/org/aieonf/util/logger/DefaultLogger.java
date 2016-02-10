/**
 * 
 */
package org.aieonf.util.logger;


/**
 * @author Kees Pieters
 *
 */
public class DefaultLogger implements ILogger 
{
	private java.util.logging.Logger logger;
	
	/**
	 * Create a default logger
	 */
	public DefaultLogger( String name ) 
	{
		Logger.addLogger( this );
		this.logger = java.util.logging.Logger.getLogger( name );
		this.logger.setLevel( java.util.logging.Level.FINEST );
		this.logger.info( "Starting message" );
	}
	
	/**
	 * Get the logger
	 * @return
	 */
	public java.util.logging.Logger getLogger()
	{
		return this.logger;
	}
	

	/**
	 * Handle errors
	 * @see org.aieonf.util.logger.ILogger#handleError(org.aieonf.util.logger.ILogger.Level, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void handleError(Level level, Class<?> clss, String message, Throwable thrw) 
	{
		this.handleMessage( level, clss, message );
		thrw.printStackTrace();
	}

	/**
	 * Convert the logger levels to java logging levels
	 * @param level
	 * @return
	 */
	protected java.util.logging.Level convert( Level level )
	{
		switch( level ){
			case Trace:
				return java.util.logging.Level.FINEST;
			case Debug:
				return java.util.logging.Level.FINE;
			case Info:
				return java.util.logging.Level.INFO;
			case Warning:
				return java.util.logging.Level.WARNING;
			default:
				break;
		}
		return java.util.logging.Level.SEVERE;
	}
	
	/**
	 * Handle messages
	 * @see org.aieonf.util.logger.ILogger#handleMessage(org.aieonf.util.logger.ILogger.Level, java.lang.String)
	 */
	@Override
	public void handleMessage(Level level, Class<?> clss, String message) 
	{
		java.util.logging.Level javLev = this.convert( level );
		if( javLev.intValue() < this.logger.getLevel().intValue() )
			return;
		String msg = clss.getCanonicalName() + "\n" + message;
		this.logger.log( javLev, msg );
	}

}
