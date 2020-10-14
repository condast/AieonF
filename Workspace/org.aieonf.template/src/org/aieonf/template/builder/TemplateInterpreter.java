package org.aieonf.template.builder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.ContextAieon;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.domain.DomainAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.concept.library.DateAieon;
import org.aieonf.concept.library.LocaleAieon;
import org.aieonf.concept.library.LogEntryAieon;
import org.aieonf.concept.library.URLAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.concept.security.IPasswordAieon;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.xml.AbstractModelInterpreter;
import org.aieonf.template.def.ITemplateLeaf;
import org.aieonf.template.property.ITemplateProperty;
import org.xml.sax.Attributes;

public class TemplateInterpreter extends AbstractModelInterpreter<IDescriptor, IDescriptor> {

	public static final String S_WRN_CONCEPT_NOT_FOUND = "No concept has name: ";
	public static final String S_WRN_DEFAULTING_TO = ". Defaulting to concept instance. ";
	public static final String S_WRN_PROPERTY_NOT_FOUND = "The property is not found for this concept: ";

	public static final String S_ERR_NULL_KEY = "No key found for concept. Trying to add value: ";

	public enum Concepts{
		CONTEXT,
		DOMAIN,
		LOADER,
		CONCEPT,
		CATEGORY,
		URL,
		LOG_ENTRY,
		LOGIN,
		BIRTH_DATE,
		LOCALE;

		public static boolean isValid( String name ){
			if( Utils.assertNull( name ))
				return false;
			String str = StringStyler.styleToEnum( name );
			for( Concepts concept: Concepts.values()){
				if( concept.name().equals( str ))
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	private Logger logger  = Logger.getLogger( TemplateInterpreter.class.getName() );
	
	private IContextAieon context;
	private Class<?> clss;
	
	public TemplateInterpreter( Class<?> clss ) {
		this( clss, IModelBuilder.S_DEFAULT_LOCATION );
		this.clss = clss;
	}

	public TemplateInterpreter( Class<?> clss, String location ) {
		super( ITemplateLeaf.S_TEMPLATE, clss, location );
	}

	public TemplateInterpreter( Class<?> clss, InputStream in ) {
		super( ITemplateLeaf.S_TEMPLATE, in );
		this.clss = clss;
	}

	/**
	 * Get the Class of the template
	 * @return
	 */
	public Class<?> getProcessedClass(){
		return clss;
	}

	protected IContextAieon getContext(){
		return this.context;
	}
	
	@Override
	public boolean isValid(String name) {
		return Concepts.isValid(name);
	}

	@Override
	public boolean canCreate(String name, Attributes attributes) {
		return this.isValid(name);
	}

	@Override
	public synchronized IDescriptor onCreate( String name, Attributes attributes) {
		String str = StringStyler.styleToEnum( name );
		if( !Concepts.isValid( str ))
			return new Descriptor( -1, str );

		String desc = StringStyler.prettyString( str );
		IDescriptor descriptor = null;
		
		String id_name = StringStyler.xmlStyleString( IDescriptor.Attributes.NAME.name() );
		String name_attr = attributes.getValue( id_name );
		Concepts dc = Concepts.valueOf(str);
		switch( dc ){
		case CONTEXT:
			this.context = new ContextAieon();
			if(!Utils.assertNull( name_attr))
				this.context.set( IContextAieon.Attributes.CONTEXT, name_attr);
			descriptor = this.context;
			break;
		case DOMAIN:
			descriptor = new DomainAieon();
			break;
		case LOADER:
			descriptor = new LoaderAieon( this.context );
			if(!Utils.assertNull( name_attr))
				this.context.set( ILoaderAieon.Attributes.LOADER, name_attr);
			break;
		case CONCEPT:
			descriptor = createConcept( ConceptBase.class, attributes );
			break;			
		case CATEGORY:
			descriptor = new CategoryAieon();
			if(!Utils.assertNull( name_attr))
				this.context.set( CategoryAieon.Attributes.CATEGORY, name_attr);
			break;
		case URL:
			descriptor = new URLAieon();
			break;			
		case LOG_ENTRY:
			descriptor = new LogEntryAieon();
			break;			
		case BIRTH_DATE:
			descriptor = new DateAieon();
			break;
		case LOCALE:
			descriptor = new LocaleAieon();
			if(!Utils.assertNull( name_attr))
				this.context.set( LocaleAieon.Attributes.LOCALE, name_attr);
			break;
		default:
			String clss_str = attributes.getValue( ITemplateProperty.Attributes.CLASS.toString());
			if(!Utils.assertNull(clss_str)){
				Class<?> cd;
				try {
					cd = clss.getClassLoader().loadClass( clss_str );
					descriptor = (IDescriptor) cd.newInstance();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}else{	
				logger.warning( S_WRN_CONCEPT_NOT_FOUND + name + S_WRN_DEFAULTING_TO );
				descriptor = new Concept();
			}
			break;
		}
		descriptor.set( IDescriptor.Attributes.NAME.name(), desc );
		descriptor.setVersion(1);
		return descriptor;
	}

	@Override
	public boolean setProperty(String id, Attributes attrs ) {
		IDescriptor descriptor = super.getModel();
		String str = StringStyler.styleToEnum( descriptor.getName() );

		if( !Concepts.isValid( str ))
			return false;

		Concepts dc = Concepts.valueOf( str);
		String key = StringStyler.styleToEnum(id );
		if( IDescriptor.Attributes.isValid(key )){
			super.setKey( key );
			return true;
		}
		if( IConcept.Attributes.isValid(key )){
			super.setKey(  key );
			return true;
		}
		if( LocaleAieon.S_LOCALE.toUpperCase().equals( key )){
			LocaleAieon locale = new LocaleAieon( descriptor );
			setLocale(locale, attrs);
			super.setKey(  key );
			return true;
		}
		boolean retval = true;
		Enum<?> enm = null;
		switch( dc ){
		case CONTEXT:
			enm = IContextAieon.Attributes.valueOf( key );
			break;
		case DOMAIN:
			enm = IDomainAieon.Attributes.valueOf( key );
			super.setKey( enm.toString() );			
			break;
		case LOADER:
			if( ILoaderAieon.Attributes.isValid(key ))
				enm = ILoaderAieon.Attributes.valueOf(key);
			else
				enm = IPasswordAieon.Attributes.valueOf( key );
			break;
		case CONCEPT:
			enm = IConcept.Attributes.valueOf( key );
			break;			
		case CATEGORY:
			enm = CategoryAieon.Attributes.valueOf( key );
			break;
		case URL:
			enm = URLAieon.Attributes.valueOf( key );
			break;
		case LOG_ENTRY:
			enm = LogEntryAieon.Attributes.valueOf( key );
			break;
		case LOGIN:
			enm = IPasswordAieon.Attributes.valueOf( key );
			break;
		case BIRTH_DATE:
			enm = DateAieon.Attributes.valueOf( key );
			break;
		default:
			retval = false;
			break;
		}
		if( enm != null ){
			super.setKey( enm.toString() );
			//descriptor.set( enm , ca );			
		}
		if( !retval )
			logger.warning( S_WRN_PROPERTY_NOT_FOUND + id );
		return retval;
	}

	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static Map<ITemplateProperty.Attributes, String> convertAttributes( Attributes attributes ){
		Map<ITemplateProperty.Attributes,String> attrs = new HashMap<ITemplateProperty.Attributes, String>();
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !Utils.assertNull( attributes.getLocalName(i))){
				String str = StringStyler.styleToEnum( attributes.getLocalName( i ));
				ITemplateProperty.Attributes attr = ITemplateProperty.Attributes.valueOf( str );
				attrs.put( attr, attributes.getValue(i));
			}
		}
		return attrs;
	}

	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static void setLocale( LocaleAieon locale, Attributes attributes ){
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !Utils.assertNull( attributes.getLocalName(i))){
				String str = StringStyler.styleToEnum( attributes.getLocalName( i ));
				LocaleAieon.Attributes key = LocaleAieon.Attributes.valueOf( str );
				locale.set( key, attributes.getValue(i));
			}
		}
	}
}
