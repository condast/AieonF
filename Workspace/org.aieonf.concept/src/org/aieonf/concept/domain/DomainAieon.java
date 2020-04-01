package org.aieonf.concept.domain;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Concept
;
import org.aieonf.concept.implicit.ImplicitAieon;

public class DomainAieon extends ImplicitAieon implements IConcept, IDomainAieon
{
	private static final long serialVersionUID = -7712089015989805720L;

	/**
	 * Create a default domain aieon
	*/
	public DomainAieon() 
	{
	  super( new Concept( IDomainAieon.Attributes.DOMAIN.toString() ), IDomainAieon.Attributes.DOMAIN.name() );
	  super.getDescriptor().set( IDescriptor.Attributes.CLASS, this.getClass().getName() );
	}

	/**
	 * Create a domain name with the given name
	 * @param domain
	*/
	public DomainAieon( String shortName )
	{
	  this( IDomainAieon.Attributes.DOMAIN.toString(), shortName );
	}

	/**
	 * Create a domain name with the given name
	 * @param domain
	*/
	public DomainAieon( String domain, String shortName )
	{
	  super( new Concept( domain ),IDomainAieon.Attributes.DOMAIN.toString());
	  super.set(IDomainAieon.Attributes.DOMAIN.toString(), shortName );
	  super.set(IDomainAieon.Attributes.SHORT_NAME.toString(), shortName );
	  super.getDescriptor().set( IDescriptor.Attributes.CLASS, this.getClass().getName() );
	}
	
	public DomainAieon(IDescriptor descriptor) {
		super(descriptor, IDomainAieon.Attributes.DOMAIN.toString());
	}

	/**
	 * Get the short name for this domain
	 */
	@Override
	public String getShortName(){
		return this.get( IDomainAieon.Attributes.SHORT_NAME.toString() );
	}
	
	
	@Override
	public String getUserName() {
		return this.get( IDomainAieon.Attributes.USER_NAME.toString() );
	}

	@Override
	public String getPassword() {
		return this.get( IDomainAieon.Attributes.PASSWORD.toString() );
	}

	/**
	 * Get the domain name
	 * @return
	*/
	@Override
	public String getDomain()
	{
		return this.get(IDomainAieon.Attributes.DOMAIN.toString() );
	}
	
	/**
	 * Get the perspective to observe the domain
	 * @return
	*/
	@Override
	public String getPerspective()
	{
		return this.get(IDomainAieon.Attributes.PERSPECTIVE.toString() );
	}
	
	/**
	 * Set the perspective
	 * @param domain String
	 */
	protected void setPerspective( String perspective )
	{
	  super.set(IDomainAieon.Attributes.PERSPECTIVE.toString(), perspective );
	}

	/**
	 * set or reset the 'active concept' flag
	 * @param choice
	 */
	public void setActive( boolean choice )
	{
		super.getDescriptor().set( IDomainAieon.Attributes.ACTIVE.toString(), String.valueOf( choice ));
	}
/**
	 * Get the sort order of this domain
	 * @return
	 */
	public int getSort()
	{
		return super.getInteger( IDomainAieon.Attributes.SORT.toString() );
	}

	/**
	 * Set the sort order of this domain
	 * @param sort
	 */
	public void setSort( int sort ){
		super.getDescriptor().set( IDomainAieon.Attributes.SORT.toString(), String.valueOf( sort ));
	}

	/**
	 * Get the parent domain, if this domain contains one
	 * @return
	*/
	public String getParent(){
		return this.get( IDomainAieon.Attributes.PARENT.toString() );
	}

	/**
	 * Set the parent domain name
	 * @param name
	 */
	public void setParent( String parent ){
		this.set(IDomainAieon.Attributes.PARENT.toString(), parent );
	}
	
	/**
	 * If true, the given domain is a root domain (parent = null)
	 * @return
	 */
	public boolean isRootDomain(){
		return ( this.getParent() == null );
	}	
	
	
	@Override
	public boolean implies(Object descriptor) {
		if( !(descriptor instanceof IDescriptor ))
			return false;
		IDomainAieon check = new DomainAieon((IDescriptor) descriptor);
		if( getDomain().equals(check.getDomain()))
			return true;
		return super.implies(descriptor);
	}

	@Override
	public int hashCode() {
		return getDomain().hashCode();
	}

	@Override
	public boolean equals( Object obj ) {
		if( !(obj instanceof IDomainAieon ))
			return false;
		if(!super.equals(obj))
			return false;
		IDomainAieon check = (IDomainAieon) obj;
		return getDomain().equals(check.getDomain());
	}
}
