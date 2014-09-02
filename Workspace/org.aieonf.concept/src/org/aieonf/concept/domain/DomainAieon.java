package org.aieonf.concept.domain;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.ConceptBody;
import org.aieonf.concept.core.ConceptInstance;
import org.aieonf.concept.implicit.ImplicitAieon;

public class DomainAieon extends ImplicitAieon implements IConcept, IDomainAieon
{
	private static final long serialVersionUID = -7712089015989805720L;

	public static final String S_DEFAULT_PREFIX = "org.aieonf.domain";
	/**
	 * Create a default domain aieon
	*/
	public DomainAieon() 
	{
	  super( new ConceptInstance( IDomainAieon.Attributes.DOMAIN.toString() ), IDomainAieon.Attributes.DOMAIN.name() );
	  super.getDescriptor().set( IDescriptor.Attributes.CLASS, this.getClass().getName() );
	}

	/**
	 * Create a domain name with the given name
	 * @param domain
	*/
	public DomainAieon( String prefix, String shortName )
	{
	  super( new ConceptInstance( IDomainAieon.Attributes.DOMAIN.name() ),IDomainAieon.Attributes.DOMAIN.name());
	  this.setDomain( prefix, shortName );
	  super.getDescriptor().set( IDescriptor.Attributes.CLASS, this.getClass().getName() );
	}

	/**
	 * Create a domain name with the given name
	 * @param domain
	*/
	public DomainAieon( String shortName )
	{
		this( S_DEFAULT_PREFIX, shortName );
	}
	
	/**
	 * Create a domain aieon for the given concept
	 * @param descriptor
	 */
	public DomainAieon( IDescriptor descriptor )
	{
	  super( descriptor,IDomainAieon.Attributes.DOMAIN.name());
	  super.getDescriptor().set( IDescriptor.Attributes.CLASS, this.getClass().getName() );		
	}

	/**
	 * Create a domain aieon for the given concept
	 * @param descriptor
	 */
	public DomainAieon( IDescriptor descriptor, String domain )
	{
	  this( descriptor);
	  super.getDescriptor().set( IDomainAieon.Attributes.DOMAIN.name(), domain );		
	}

	/**
	 * Get the short name for this domain
	 */
	@Override
	public String getShortName(){
		return this.get( IDomainAieon.Attributes.SHORT_NAME );
	}
	
	/**
	 * Get the domain name
	 * @return
	*/
	@Override
	public String getDomain()
	{
		return this.get(IDomainAieon.Attributes.DOMAIN );
	}
	
	/**
	 * Set the domain
	 * @param domain String
	 */
	protected void setDomain( String prefix, String shortName )
	{
	  super.set(IDomainAieon.Attributes.DOMAIN, prefix + "." + shortName );
	  super.set(IDomainAieon.Attributes.SHORT_NAME, shortName );
	}
	
	/**
	 * set or reset the 'active concept' flag
	 * @param choice
	 */
	public void setActive( boolean choice )
	{
		String attr = ConceptBody.getKeyName(IDomainAieon.Attributes.ACTIVE );
		super.getDescriptor().set( attr, String.valueOf( choice ));
	}
/**
	 * Get the sort order of this domain
	 * @return
	 */
	public int getSort()
	{
		String attr = ConceptBody.getKeyName(IDomainAieon.Attributes.SORT );
		return super.getInteger( attr );
	}

	/**
	 * Set the sort order of this domain
	 * @param sort
	 */
	public void setSort( int sort )
	{
		String attr = ConceptBody.getKeyName(IDomainAieon.Attributes.SORT );
		super.getDescriptor().set( attr, String.valueOf( sort ));
	}

	/**
	 * Get the parent domain, if this domain contains one
	 * @return
	*/
	public String getParent()
	{
		return this.get( IDomainAieon.Attributes.PARENT );
	}

	/**
	 * Set the parent domain name
	 * @param name
	 */
	public void setParent( String parent )
	{
		this.set(IDomainAieon.Attributes.PARENT, parent );
	}
	
	/**
	 * If true, the given domain is a root domain (parent = null)
	 * @return
	 */
	public boolean isRootDomain()
	{
		return ( this.getParent() == null );
	}

	/**
	 * Returns true if the given concept is a domain
	 * @param concept
	 * @return
	 */
	public static boolean isDomain( IConcept concept )
	{
		return (( concept.get(IDomainAieon.Attributes.DOMAIN.name() )) != null );
	}
}
