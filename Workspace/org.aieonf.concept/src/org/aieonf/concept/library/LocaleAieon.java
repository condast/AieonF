package org.aieonf.concept.library;

import java.util.Locale;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.implicit.ImplicitAieon;

public class LocaleAieon extends ImplicitAieon 
{
	/**
	 * Serialisation id
	 */
	private static final long serialVersionUID = 7914305409901281367L;
	
	//Error messages
	public static final String S_ERR_NO_COUNTRY = 
		"No country in the provided concept: ";
	
	public static final String S_LOCALE = "Locale";
	
	public enum Attributes
	{
		COUNTRY,
		LOCALE,
		LANGUAGE,
		VARIANT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValid( String str ){
			for( Attributes attr: values() ){
				if( attr.name().equals( str ))
					return true;
			}
			return false;
		}

	}
	
	/**
	 * Create a locale aieon
	*/
	public LocaleAieon() 
	{
		this( Locale.getDefault() );
	}

	/**
	 * Create a locale aieon
	*/
	public LocaleAieon( String name ) 
	{
		super( name );
		Locale locale = Locale.getDefault();
		super.set( S_LOCALE, locale.getLanguage() );
		super.set( Attributes.COUNTRY, locale.getCountry() );
		super.set( Attributes.VARIANT, locale.getVariant() );
	}

	/**
	 * Create an aieon for the given locale
	 * @param locale
	 */
	public LocaleAieon( Locale locale )
	{
		super( S_LOCALE, locale.getLanguage() );
		super.set( S_LOCALE, locale.getLanguage() );
		super.set( Attributes.COUNTRY, locale.getCountry() );
		super.set( Attributes.VARIANT, locale.getVariant() );
	}

	/**
	 * Create an aieon for the given locale
	 * @param locale
	 */
	protected LocaleAieon( String name, Locale locale )
	{
		super( name );
		super.set( S_LOCALE, locale.getLanguage() );
		super.set( S_LOCALE, locale.getLanguage() );
		super.set( Attributes.COUNTRY, locale.getCountry() );
		super.set( Attributes.VARIANT, locale.getVariant() );
	}

	/**
	 * Create a locale from the given concept
	 * @param concept
	 */
	public LocaleAieon( IDescriptor descriptor )
	{
		super( descriptor, S_LOCALE );
	}


	@Override
	public int compareTo( IDescriptor descriptor )
	{
		int compare = super.compareTo(descriptor);
		if( compare != 0 )
			return compare;
		boolean local = false;
		
		try{
			Locale locale = this.getLocale();
			local = true;
			Locale descLocale = this.getLocale( descriptor );
			compare = locale.getCountry().compareTo( descLocale.getCountry());
			if( compare != 0 )
				return compare;
			compare = locale.getLanguage().compareTo( descLocale.getLanguage());
			if( compare != 0 )
				return compare;
			return locale.getVariant().compareTo( descLocale.getVariant() );
		}
		catch( NullPointerException ex ){
			return local?-1:1; 
		}
	}

	@Override
	public boolean test( IDescriptor descriptor)
	{
		if( !( descriptor instanceof IDescriptor ))
			return false;
		return ( this.compareTo(( IDescriptor )descriptor ) == 0);
	}

	/**
	 * Get the language
	 * @return
	 */
	public String getLanguage()
	{
		return super.getName();
	}

	/**
	 * Get the country
	 * @return
	 */
	public String getCountry()
	{
		return super.get( Attributes.COUNTRY );
	}

	/**
	 * Get the variant
	 * @return
	 */
	public String getVariant()
	{
		return super.get( Attributes.VARIANT );
	}

	/**
	 * Get the locale
	 * @return
	 */
	public Locale getLocale()
	{
		return getLocale( this );
	}
	
	/**
	 * Returns the locale of the given concept
	 * @param descriptor
	 * @return
	 */
	@SuppressWarnings("unused")
	public Locale getLocale( IDescriptor descriptor ) 
	{
		String attribute = super.getKeyName( Attributes.COUNTRY );
		String country = descriptor.get( attribute );
		if( country == null )
			throw new NullPointerException( S_ERR_NO_COUNTRY + descriptor.toString() );
		attribute = super.getKeyName( Attributes.LANGUAGE );
		String language = descriptor.get( attribute );
		if( language == null )
			return new Locale( country );
		attribute = super.getKeyName( Attributes.VARIANT );
		String variant = descriptor.get( attribute );
		if( language == null )
			return new Locale( country, language );
		return new Locale( country, language, variant );
	}
}
