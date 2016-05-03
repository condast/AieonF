package org.aieonf.template.builder;

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
import org.aieonf.concept.security.PasswordAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.xml.AbstractModelCreator;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.TemplateNode;
import org.aieonf.template.property.ITemplateProperty;
import org.xml.sax.Attributes;

public class DefaultModelCreator extends AbstractModelCreator<IDescriptor, ITemplateLeaf<IDescriptor>> {

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
			if( Utils.isNull( name ))
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

	private Logger logger  = Logger.getLogger( DefaultModelCreator.class.getName() );
	
	private IContextAieon context;
	
	public DefaultModelCreator( Class<?> clss ) {
		this( clss,  IModelBuilder.S_DEFAULT_LOCATION );
	}

	public DefaultModelCreator( Class<?> clss, String location ) {
		super( clss, location );
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
	public synchronized ITemplateLeaf<IDescriptor> onCreate( String name, Attributes attributes) {
		String str = StringStyler.styleToEnum( name );
		String desc = StringStyler.prettyString( str );
		Concepts dc = Concepts.valueOf(str);
		IDescriptor descriptor = null;
		ITemplateLeaf<IDescriptor> leaf = null;
		
		String id_name = StringStyler.xmlStyleString( IModelLeaf.Attributes.NAME.toString() );
		String name_attr = attributes.getValue( id_name );
		switch( dc ){
		case CONTEXT:
			this.context = new ContextAieon();
			if(!Utils.isNull( name_attr))
				this.context.set( IContextAieon.Attributes.CONTEXT, name_attr);
			descriptor = this.context;
			break;
		case DOMAIN:
			descriptor = new DomainAieon( this.context );
			if(!Utils.isNull( name_attr))
				this.context.set( IDomainAieon.Attributes.DOMAIN, name_attr);
			break;
		case LOADER:
			descriptor = new LoaderAieon( this.context );
			if(!Utils.isNull( name_attr))
				this.context.set( ILoaderAieon.Attributes.LOADER, name_attr);
			break;
		case CONCEPT:
			descriptor = createConcept( ConceptBase.class, attributes );
			break;			
		case CATEGORY:
			descriptor = new CategoryAieon();
			if(!Utils.isNull( name_attr))
				this.context.set( CategoryAieon.Attributes.CATEGORY, name_attr);
			break;
		case URL:
			descriptor = new URLAieon();
			break;			
		case LOG_ENTRY:
			descriptor = new LogEntryAieon();
			break;			
		case LOGIN:
			descriptor = new PasswordAieon();
			break;
		case BIRTH_DATE:
			descriptor = new DateAieon();
			break;
		case LOCALE:
			descriptor = new LocaleAieon();
			if(!Utils.isNull( name_attr))
				this.context.set( LocaleAieon.Attributes.LOCALE, name_attr);
			break;
		default:
			String clss_str = attributes.getValue( ITemplateProperty.Attributes.CLASS.toString());
			if(!Utils.isNull(clss_str)){
				Class<?> cd;
				try {
					cd = super.getClazz().getClassLoader().loadClass( clss_str );
					descriptor = (IDescriptor) cd.newInstance();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}else	
				logger.warning( S_WRN_CONCEPT_NOT_FOUND + name + S_WRN_DEFAULTING_TO );
			descriptor = new Concept();
			break;
		}
		descriptor.set( IDescriptor.Attributes.NAME, desc );
		descriptor.setVersion(1);
		leaf = new TemplateNode<IDescriptor>( descriptor );
		return leaf;
	}


	@Override
	public boolean setProperty(String id, Attributes attrs ) {
		ITemplateLeaf<IDescriptor> leaf = super.getModel();
		IDescriptor descriptor = super.getModel().getDescriptor();
		String str = StringStyler.styleToEnum( descriptor.getName() );
		Concepts dc = Concepts.valueOf( str);
		String key = StringStyler.styleToEnum(id );
		if( IDescriptor.Attributes.isValid(key )){
			super.setKey( IDescriptor.Attributes.valueOf( key ));
			return true;
		}
		if( IConcept.Attributes.isValid(key )){
			super.setKey( IConcept.Attributes.valueOf( key ));
			return true;
		}
		if( LocaleAieon.S_LOCALE.toUpperCase().equals( key )){
			LocaleAieon locale = new LocaleAieon( descriptor );
			setLocale(locale, attrs);
			super.setKey( LocaleAieon.Attributes.valueOf( key ));
			return true;
		}
		boolean retval = true;
		Map<ITemplateProperty.Attributes, String> ca = convertAttributes(attrs);
		Enum<?> enm = null;
		switch( dc ){
		case CONTEXT:
			enm = IContextAieon.Attributes.valueOf( key );
			break;
		case DOMAIN:
			enm = IDomainAieon.Attributes.valueOf( key );
			super.setKey( enm );			
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
			super.setKey( enm );
			leaf.addAttributes( enm , ca );			
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
			if( !Utils.isNull( attributes.getLocalName(i))){
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
			if( !Utils.isNull( attributes.getLocalName(i))){
				String str = StringStyler.styleToEnum( attributes.getLocalName( i ));
				LocaleAieon.Attributes key = LocaleAieon.Attributes.valueOf( str );
				locale.set( key, attributes.getValue(i));
			}
		}
	}
}
