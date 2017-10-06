package org.aieonf.model.xml;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.xml.sax.Attributes;

public class InterpreterCollection<T extends IDescriptor, M extends IModelLeaf<T>> implements IXMLModelInterpreter<T, M> {

	private Collection<IXMLModelInterpreter<T, ? extends M>> interpreters;
	private IXMLModelInterpreter<T, ? extends M> main;
	
	
	public InterpreterCollection( IXMLModelInterpreter<T, ? extends M> interpreter ) {
		interpreters = new ArrayList<IXMLModelInterpreter<T, ? extends M>>();
		this.interpreters.add( interpreter );
		this.main = interpreter;
	}

	@Override
	public void clear() {
		for( IXMLModelInterpreter<T, ? extends M> interpreter: this.interpreters )
			interpreter.clear();
	}

	public void addInterpreter( IXMLModelInterpreter<T, M> interpreter ) {
		this.interpreters.add( interpreter );
	}

	public void removeInterpreter( IXMLModelInterpreter<T, M> interpreter ) {
		this.interpreters.remove( interpreter );
	}

	@Override
	public URL getURL() {
		return main.getURL();
	}

	@Override
	public boolean isActive() {
		return this.main.isActive();
	}

	@Override
	public boolean isValid(String name) {
		for( IXMLModelInterpreter<T, ? extends M> interpreter: this.interpreters ) {
			if( interpreter.isValid(name))
				return true;
		}
		return false;
	}

	@Override
	public boolean canCreate(String name, Attributes attributes) {
		for( IXMLModelInterpreter<T, ? extends M> interpreter: this.interpreters ) {
			if( interpreter.canCreate( name, attributes ))
				return true;
		}
		return false;
	}

	@Override
	public M create(String name, Attributes attributes) {
		for( IXMLModelInterpreter<T, ? extends M> interpreter: this.interpreters ) {
			if( interpreter.canCreate( name, attributes ))
				return interpreter.create(name, attributes);
		}
		return null;
	}

	@Override
	public M getModel() {
		return main.getModel();
	}

	@Override
	public boolean setProperty(String key, Attributes attr) {
		for( IXMLModelInterpreter<T, ? extends M> interpreter: this.interpreters ) {
			if( interpreter.isValid(key))
				return interpreter.setProperty(key, attr);
		}
		return false;
	}

	@Override
	public boolean setValue(String value) {
		for( IXMLModelInterpreter<T, ? extends M> interpreter: this.interpreters ) {
			//if( interpreter.isValid(key))
			//	return interpreter.setProperty(key, attr);
		}
		return false;
	}

	@Override
	public void endProperty() {
		main.endProperty();
	}

	@Override
	public void notifyModelCreated(ModelCreatorEvent event) {
		main.notifyModelCreated(event);
	}

	@Override
	public String getKey() {
		return main.getKey();
	}
}
