package test.aieonf.orientdb.model;

import java.util.logging.Logger;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.library.DateAieon;
import org.aieonf.concept.library.LocaleAieon;
import org.aieonf.concept.locator.ILocatorAieon;
import org.aieonf.concept.security.IPasswordAieon;
import org.aieonf.concept.security.PasswordAieon;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.model.xml.AbstractModelInterpreter;
import org.xml.sax.Attributes;

public class ModelInterpreter extends AbstractModelInterpreter<IDescriptor, IModelLeaf<IDescriptor>> {

	public static final String S_IDENTIFIER = "Model";

	public static final String S_WRN_CONCEPT_NOT_FOUND = "No concept has name: ";
	public static final String S_WRN_PROPERTY_NOT_FOUND = "The property is not found for this concept: ";

	public static final String S_ERR_NULL_KEY = "No key found for concept. Trying to add value: ";

	public enum Models{
		AUTHENTICATION,
		LOGIN,
		PERSON,
		BIRTH_DATE,
		ADDRESS,
		TOWN,
		COUNTRY;

		public static boolean isValid( String name ){
			if( StringUtils.isEmpty( name ))
				return false;
			String str = StringStyler.styleToEnum( name );
			for( Models concept: Models.values()){
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

	private Logger logger  = Logger.getLogger( ModelInterpreter.class.getName() );
	
	public ModelInterpreter() {
		super( S_IDENTIFIER, ModelInterpreter.class, IModelBuilder.S_DEFAULT_LOCATION );
	}
	
	@Override
	public Class<?> getProcessedClass() {
		return this.getClass();
	}

	@Override
	public boolean isValid(String name) {
		if( Models.isValid(name))
			return true;
		logger.warning( S_WRN_PROPERTY_NOT_FOUND + name);
		return false;
	}

	@Override
	public synchronized IModelLeaf<IDescriptor> onCreate( String name, Attributes attributes) {
		IModelLeaf<IDescriptor> leaf = null;
		String str = StringStyler.styleToEnum( name );

		Models dc = Models.valueOf(str);
		String desc = StringStyler.prettyString( str );
		IDescriptor descriptor = null;
		switch( dc ){
		case LOGIN:
			descriptor = new PasswordAieon();
			descriptor.set( IDescriptor.Attributes.NAME, desc );
			descriptor.setVersion(1);
			leaf = new Model<IDescriptor>( descriptor );
			break;
		case PERSON:
			descriptor = new Concept();
			descriptor.set( IDescriptor.Attributes.NAME, desc );
			descriptor.setVersion(1);
			leaf = new Model<IDescriptor>( descriptor );
			break;
		case BIRTH_DATE:
			descriptor = new DateAieon();
			descriptor.set( IDescriptor.Attributes.NAME, desc );
			descriptor.setVersion(1);
			leaf = new ModelLeaf<IDescriptor>( descriptor );
			break;
		case ADDRESS:
			descriptor = new Concept();
			descriptor.set( IDescriptor.Attributes.NAME, desc );
			descriptor.setVersion(1);
			leaf = new Model<IDescriptor>( descriptor );
			break;
		case TOWN:
			descriptor = new Concept();
			descriptor.set( IDescriptor.Attributes.NAME, desc );
			descriptor.setVersion(1);
			leaf = new Model<IDescriptor>( descriptor );
			break;
		case COUNTRY:
			descriptor = new Concept();
			descriptor.set( IDescriptor.Attributes.NAME, desc );
			leaf = new ModelLeaf<IDescriptor>( descriptor );
			break;
		default:
			logger.warning( S_WRN_CONCEPT_NOT_FOUND + name );
			descriptor = new Concept();
			leaf = new ModelLeaf<IDescriptor>( descriptor );
			break;
		}
		return leaf;
	}


	@Override
	public boolean setProperty(String id, Attributes attrs ) {
		IDescriptor descriptor = super.getModel().getDescriptor();
		String str = StringStyler.styleToEnum( descriptor.getName() );
		Models dc = Models.valueOf( str);
		String key = StringStyler.styleToEnum(id );
		if( IDescriptor.Attributes.isValid(key )){
			super.setKey(  key );
			return true;
		}
		if( IConcept.Attributes.isValid(key )){
			super.setKey(  key );
			return true;
		}
		if( LocaleAieon.S_LOCALE.toUpperCase().equals( key )){
			super.setKey(  key );
			return true;
		}
		boolean retval = true;
		Enum<?> enm = null;
		switch( dc ){
		case AUTHENTICATION:
			if( IContextAieon.Attributes.isValid(key )){
				enm = IContextAieon.Attributes.valueOf( key );
			}else if( ILocatorAieon.Attributes.isValid(key )){
				enm = ILocatorAieon.Attributes.valueOf( key );
			}else if( LocaleAieon.Attributes.isValid(key )){
				enm = LocaleAieon.Attributes.valueOf( key );
			} else if( IDescriptor.Attributes.isValid(key )){
				enm = IDescriptor.Attributes.valueOf( key );
			}else
				retval = false;
			break;
		case LOGIN:
			enm = IPasswordAieon.Attributes.valueOf( key );
			break;
		case PERSON:
			enm = IConcept.Attributes.valueOf( key );
			break;
		case BIRTH_DATE:
			enm = DateAieon.Attributes.valueOf( key );
			break;
		case ADDRESS:
			enm = IConcept.Attributes.valueOf( key );
			break;
		case TOWN:
			enm = IConcept.Attributes.valueOf( key );
			break;
		case COUNTRY:
			enm = IConcept.Attributes.valueOf( key );
			break;
		default:
			retval = false;
			break;
		}
		if( retval ){
			super.setKey( enm.name() );
		}else{			
			logger.warning( S_WRN_PROPERTY_NOT_FOUND + id );
		}
		return retval;
	}

	@Override
	public boolean setValue(String value) {
		boolean result = super.setValue(value);
		if( result )
			return result;
		IDescriptor descriptor = super.getModel().getDescriptor();
		String name = descriptor.getName();
		if( super.getKey() == null )
			throw new NullPointerException( S_ERR_NULL_KEY + name + "{" + value + "}" );
		//String key = super.getKey().toString();
		Models concept = Models.valueOf( StringStyler.styleToEnum( name ));
		boolean retval = false;
		switch( concept ){
		case COUNTRY:
/*
			if( CountryAieon.Attributes.isValid( StringStyler.styleToEnum( key ))){
				CountryAieon.Attributes attr = CountryAieon.Attributes.valueOf( StringStyler.styleToEnum( key ));
				if( CountryAieon.Attributes.COUNTRY.equals( attr ))
					retval = super.setValue( Country.convert( value ).toString());	
				else
					retval = super.setValue(value);
			}else
				retval = super.setValue(value);
				*/				
			break;
		default:
			retval = super.setValue(value);
			break;
		}
		return retval;
	}
}
