package test.aieonf.model.interpreter;

import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.xml.AbstractModelInterpreter;
import org.xml.sax.Attributes;


public class AuthenticationInterpreter extends AbstractModelInterpreter<IDescriptor, IDescriptor> {

	public static final String S_IDENTIFIER = "Authentication Model";

	public static enum Fields{
		LOGIN,
		PERSON,
		ADDRESS,
		BIRTH_DATE,
		TOWN,
		COUNTRY;
		
		public boolean isLeaf(){
			return this.equals( LOGIN ) || this.equals( BIRTH_DATE) ||
					this.equals( COUNTRY);
		}
	}
	
	private IDomainAieon domain;
	private IDescriptor model;

	public AuthenticationInterpreter( IDomainAieon domain ) {
		this( S_IDENTIFIER, domain, true );
	}

	public AuthenticationInterpreter( String id, IDomainAieon domain, boolean active) {
		super( id, AuthenticationInterpreter.class, IModelBuilder.S_DEFAULT_MODEL_LOCATION );
		this.domain = domain;
	}

	@Override
	public Class<?> getProcessedClass() {
		return this.getClass();
	}

	@Override
	public IDescriptor getModel() {
		return this.model;
	}

	@Override
	public boolean isValid(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected IDescriptor onCreate(String name, Attributes attributes) {
		String id = this.domain.getDomain() + "." + name;
		this.model = new Descriptor( id );
		return this.model;
	}

	protected boolean isValidProperty(String name) {
		if( StringUtils.isEmpty( name ))
			return false;
		for( IDescriptor.Attributes attr: IDescriptor.Attributes.values()){
			if( attr.name().equals( name.toUpperCase()))
				return true;
		}
		if( StringUtils.isEmpty( name ))
			return false;
		for( IConcept.Attributes attr: IConcept.Attributes.values()){
			if( attr.name().equals( name.toUpperCase()))
				return true;
		}
		return false;
	}

	@Override
	public boolean setProperty(String key, Attributes attr) {
		IDescriptor descriptor = this.model.getDescriptor();
		if( descriptor == null )
			return false;
		if( this.isValidProperty(key))
			super.setKey(key);
		else
			super.setKey( this.domain.getDomain() + "." + key);
		return true;
	}

	@Override
	public boolean setValue(String value) {
		IDescriptor descriptor = this.model.getDescriptor();
		descriptor.set( super.getKey(), value);
		return true;
	}

	@Override
	public void endProperty() {
	}
}
