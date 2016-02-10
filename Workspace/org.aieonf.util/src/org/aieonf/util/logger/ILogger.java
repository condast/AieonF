package org.aieonf.util.logger;

/**
 * Impliment a logger interface
 * @author Kees Pieters
 *
 */
public interface ILogger 
{
	/**
	 * Supported log levels
	 * @author Kees Pieters
	 *
	*/
	public enum Level
	{
		Trace,
		Debug,
		Info,
		Warning,
		Error,
		Fatal
	}
	
	/**
	 * Handle a regular message 
	 * @param level
	 * @param message
	*/
	public void handleMessage( Level level, Class<?> clss, String message );
	
	/**
	 * Handle an error
	 * @param level
	 * @param message
	*/
	public void handleError( Level level, Class<?> clss, String message, Throwable thrw );
	
}
