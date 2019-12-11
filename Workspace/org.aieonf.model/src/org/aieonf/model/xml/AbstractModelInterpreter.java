package org.aieonf.model.xml;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.xml.sax.Attributes;

public abstract class AbstractModelInterpreter<T extends IDescriptor, M extends IDescribable> implements IXMLModelInterpreter<M> {

	private boolean active;
	private String identifier;
	private M model;
	private String key;
	private URL url;
	
	private Collection<IModelBuilderListener<T>> listeners;

	protected AbstractModelInterpreter( String identifier, Class<?> clss, String resourceLocation) {
		this( identifier, clss.getResource( resourceLocation ));
	}

	protected AbstractModelInterpreter( String identifier, URL url ) {
		this.url = url;
		this.identifier = identifier;
		this.listeners = new ArrayList<IModelBuilderListener<T>>();
	}

	@Override
	public String getIdentifier() {
		return this.identifier;
	}

	public void addModelBuilderListener( IModelBuilderListener<T> listener ) {
		this.listeners.add( listener );
	}

	public void removedModelBuilderListener( IModelBuilderListener<T> listener ) {
		this.listeners.remove( listener );
	}

	/**
	 * Reset the creator
	 */
	@Override
	public void clear(){
		this.active = false;
		this.key = null;
	}
	
	@Override
	public URL getURL() {
		return url;
	}
	
	@Override
	public String getKey() {
		if( key == null )
			return key;
		return key.toString();
	}

	/**
	 * Get the currently selected model
	 * @return
	 */
	@Override
	public M getModel() {
		return model;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean canCreate(String name, Attributes attributes) {
		return this.isValid(name);
	}

	/**
	 * Create a descriptor from the given nae and attributes.
	 * @param name
	 * @param attributes
	 * @return
	 */
	protected abstract M onCreate( String name, Attributes attributes );

	@Override
	public synchronized M create( String name, Attributes attributes) {
		this.model = this.onCreate( name, attributes);
		this.active = ( this.model != null );
		if( model == null ) {
			return null;
		}
		return model;
	}

	/**
	 * Create a factory from a class definition
	 * @param componentName
	 * @param attributes
	 * @param parentSource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static IConcept createConcept( Class<?> clss, Attributes attributes ){
		if( attributes.getValue( Keys.CLASS.toString().toLowerCase()) == null )
			return null;
		String className = attributes.getValue( Keys.CLASS.toString().toLowerCase());
		Class<?> conceptClass = null;
		try{
			conceptClass = clss.getClassLoader().loadClass( className );
		}
		catch( ClassNotFoundException ex1 ){
			try{
				conceptClass = clss.getClassLoader().loadClass( className );
			}
			catch( ClassNotFoundException ex2 ){ /* do nothing */ }
		}
		if( conceptClass != null  ){
			try {
				Constructor<IConcept> constructor = (Constructor<IConcept>) conceptClass.getDeclaredConstructor();
				return constructor.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	protected boolean setKey( String key ) {
		this.key = key;
		return true;
	}

	@Override
	public boolean setValue(String value) {
		this.model.getDescriptor().set(key, value);
		return true;
	}

	@Override
	public void endProperty() {
		this.key = null;
	}
}
