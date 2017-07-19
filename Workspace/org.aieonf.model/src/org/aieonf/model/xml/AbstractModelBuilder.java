package org.aieonf.model.xml;

import java.lang.reflect.Constructor;
import java.net.URL;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.xml.sax.Attributes;

public abstract class AbstractModelBuilder<T extends IDescriptor, U extends IModelLeaf<T>> implements IXMLModelBuilder<T,U> {

	private boolean active;
	private U model;
	private String key;
	private URL url;

	protected AbstractModelBuilder( Class<?> clss, String resourceLocation) {
		this( clss.getResource( resourceLocation ));
	}

	protected AbstractModelBuilder( URL url ) {
		this.url = url;
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
	public U getModel() {
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
	protected abstract U onCreate( String name, Attributes attributes );

	@Override
	public synchronized U create( String name, Attributes attributes) {
		this.model = this.onCreate( name, attributes);
		this.active = ( this.model != null );
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

	@Override
	public void notifyDescriptorCreated(ModelCreatorEvent event) {
		// DO NOTHING	
	}
}
