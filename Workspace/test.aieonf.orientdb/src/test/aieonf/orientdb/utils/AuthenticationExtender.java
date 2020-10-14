package test.aieonf.orientdb.utils;

import java.io.InputStream;

import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.xml.sax.Attributes;


public class AuthenticationExtender implements IXMLModelInterpreter<IModelLeaf<IDescriptor>> {

	public static final String S_IDENTIFIER = "Authentication Model";

	private IDomainAieon domain;
	private boolean active;
	private IModelLeaf<IDescriptor> model;
	private String key;

	public AuthenticationExtender( IDomainAieon domain ) {
		this( domain, true );
	}

	public AuthenticationExtender( IDomainAieon domain, boolean active) {
		this.active = active;
		this.domain = domain;
	}
	
	@Override
	public String getIdentifier() {
		return S_IDENTIFIER;
	}

	@Override
	public Class<?> getProcessedClass() {
		return this.getClass();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean isValid(String name) {
		return true;
	}

	@Override
	public boolean canCreate(String name, Attributes attributes) {
		return true;
	}

	@Override
	public IModelLeaf<IDescriptor> create(String name, Attributes attributes) {
		String id = this.domain.getDomain() + "." + name;
		IDescriptor descriptor = new Descriptor( id );
		this.model = new ModelLeaf<IDescriptor>( descriptor );
		return this.model;
	}

	@Override
	public IModelLeaf<IDescriptor> getModel() {
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
			this.key = key;
		else
			this.key = this.domain.getDomain() + "." + key;
		return true;
	}

	public boolean setValue(String value) {
		IDescriptor descriptor = this.model.getDescriptor();
		descriptor.set( key, value);
		return true;
	}

	@Override
	public void endProperty() {
		this.model = null;
	}

	@Override
	public String getKey() {
		return this.key;
	}

}
