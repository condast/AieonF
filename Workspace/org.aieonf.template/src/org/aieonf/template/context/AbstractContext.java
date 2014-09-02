package org.aieonf.template.context;

import java.util.logging.Logger;

import org.aieonf.concept.context.IApplication;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.domain.DomainAieon;

/**
 * An application controls the flow of concepts
 * @author Kees Pieters
 *
 */
public abstract class AbstractContext<T extends IContextAieon> implements IContext<T>, IApplication
{
	public static final String S_CONTEXT ="Context";

	//Supported databases
	public static final String S_DB_PUBLIC = "public";
	public static final String S_DB_PRIVATE = "private";
//
	//Error messages
	public static final String S_ERR_NO_APPLICATION_CREATED  =
		"The application was not created";
		
	//The aieon that contains information about the application
	private IContextAieon aieon;
	
	//Get the domain
	private DomainAieon domain;

	//This flag is true while the context is starting up.
	//It needs to be manually reset
	private boolean starting;

	private Logger logger;
	
	/**
	 * Create a new context
	 * @param aieon
	 * @throws ConceptException
	 */
	public AbstractContext( IContextAieon aieon ) throws ConceptException
	{
		this.aieon = aieon;
		this.starting = false;
		this.domain = new DomainAieon( aieon );
		if( aieon.getVersion() < 0 )
			this.domain.setVersion( 0 );
		this.domain.setSort( 0 );
		this.domain.setActive( true );
		this.logger = Logger.getLogger( this.getClass().getName() );
	}

	/**
	 * Returns true if the context is starting up
	 * @return
	*/
	@Override
	public boolean isStarting()
	{
		return this.starting;
	}
	
	/**
	 * Resets the startup flag. This can be used to control a decision when the
	 * startup of a context is completed. By default, this only done at a shutdown
	*/
	@Override
	public void resetStartup()
	{
		this.starting = false;
	}

	/**
	 * Get the domain
	 * @return
	 */
	@Override
	public DomainAieon getDomain()
	{
	  return this.domain;
	}
		
	/**
	 * Initialise the application
	 * @throws Exception
	*/
	@Override
	public void initialise()throws Exception
	{
    this.starting = true;
    logger.info( "Application initialised" );
	}

	/**
	 * Clear the container of the application
	 * @throws Exception
	*/
	@Override
	public void clear() throws Exception
	{
		this.starting = false;
	}
	

	/**
	 * Get the application aieon
	 * @return
	*/
	@Override
	public IContextAieon getDescriptor()
	{
		return this.aieon;
	}

	
	/* (non-Javadoc)
	 * @see org.condast.concept.IDescribable#hasChanged()
	 */
	@Override
	public boolean hasChanged()
	{
		return false;
	}

	/**
	 * Finalise the application
	 * @throws Exception
	*/
	@Override
	public void shutdown() throws Exception
	{
		this.clear();
	}
}
