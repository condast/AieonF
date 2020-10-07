package org.aieonf.concept.library;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.IExtendedConcept;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.wrapper.ConceptWrapper;

public class ExtendedConcept extends ConceptWrapper implements IExtendedConcept
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2388032407721517418L;

	public ExtendedConcept(IDescriptor descriptor)
	{
		super(descriptor);
	}

	public ExtendedConcept(String id, IDescriptor descriptor)
			throws ConceptException
	{
		super(id, descriptor);
	}

	/**
	 * Get the author of the concept
	 *
	 * @return String
	 */
	public final String getAuthor()
	{
		return this.get( IConcept.Attributes.AUTHOR.name() );
	}

	/**
	 * Set the author of the concept
	 *
	 * @param author String
	 */
	public final void setAuthor( String author )
	{
		this.set( IConcept.Attributes.AUTHOR.name(), author );
	}

	/**
	 * A concept by definition has a 'weight' that can be used for popularity, etc.
	 * The user determines the meaning.
	 *
	 * @return int
	 */
	@Override
	public final int getWeight()
	{
		return this.getInteger( IExtendedConcept.Attribute.WEIGHT.toString() );
	}

	/**
	 * A concept by definition has a 'weight' that can be used for popularity, etc.
	 * The user determines the  meaning.
	 *
	 * @param weight int
	 */
	@Override
	public final void setWeight( int weight )
	{
		super.set( IExtendedConcept.Attribute.WEIGHT, String.valueOf( weight ));
	}

	/**
	 * Get the propator of the concept.
	 * This is the original source that created the concept,
	 * and should not be altered
	 *
	 * @return String
	 */
	@Override
	public String getPropator()
	{
		return this.get( IExtendedConcept.Attribute.PROPATOR );
	}

	/**
	 * Set the propator of the concept
	 * This is the original source that created the concept
	 *
	 * @param propator String
	 */
	@Override
	public void setPropator( String propator )
	{
		this.set( IExtendedConcept.Attribute.PROPATOR, propator );
	}

	/**
	 * Returns true if the concept is hotlisted
	 *
	 * @return boolean
	 */
	@Override
	public boolean isHotlisted()
	{
		String key = this.get( IExtendedConcept.Attribute.HOTLISTED );
		return this.getBoolean( key );
	}

	/**
	 * Get or set the hotlist option
	 *
	 * @param hotList boolean
	 */
	@Override
	public void setHotlist( boolean hotList )
	{
		this.set( IExtendedConcept.Attribute.HOTLISTED, String.valueOf( hotList ));
	}
}
