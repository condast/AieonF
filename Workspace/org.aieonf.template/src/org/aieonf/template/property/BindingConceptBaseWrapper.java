/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.template.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.IConceptBase;

/**
 * Create a concept, using a properties file
 */
public class BindingConceptBaseWrapper implements IConceptBase, PropertyChangeListener
{
	
	private IConceptBase base;
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public BindingConceptBaseWrapper( IConceptBase base ) {
		this.base = base;
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#clear()
	 */
	@Override
	public synchronized void clear()
	{
		Iterator<String> iterator = base.iterator();
		while( iterator.hasNext()){
			String prop = iterator.next();
			PropertyChangeListener[] listeners = propertyChangeSupport.getPropertyChangeListeners(prop);
			for( PropertyChangeListener listener: listeners){
				propertyChangeSupport.firePropertyChange(prop, base.get( prop ), null );
				propertyChangeSupport.removePropertyChangeListener( listener);
			}
		}
		base.clear();
	}

	
	@Override
	public boolean hasChanged() {
		return base.hasChanged();
	}

	@Override
	public void setChanged(boolean choice) {
		base.setChanged(choice);
	}

	@Override
	public String get(String key) {
		return base.get(key);
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#set(java.lang.String, java.lang.String)
	 */
	@Override
	public final void set( String key, String value )
	{
		String old = base.get( key );
		base.set( key, value );
		if( base.hasChanged())
			propertyChangeSupport.firePropertyChange(key, old, key );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#remove(java.lang.String)
	 */
	@Override
	public final void remove( String key )
	{
		PropertyChangeListener[] listeners = propertyChangeSupport.getPropertyChangeListeners(key);
		for( PropertyChangeListener listener: listeners){
			propertyChangeSupport.firePropertyChange(key, base.get( key ), null );
			propertyChangeSupport.removePropertyChangeListener( listener);
		}
		base.remove(key);
	}

	@Override
	public String get(Enum<?> enm) {
		return base.get(enm);
	}

	@Override
	public void set(Enum<?> enm, String value) {
		this.set( ConceptBase.getAttributeKey(enm), value);
		
	}

	@Override
	public void remove(Enum<?> enm) {
		this.remove( ConceptBase.getAttributeKey(enm));
	}

	@Override
	public boolean getBoolean(String attribute) {
		return base.getBoolean(attribute);
	}

	@Override
	public int getInteger(String attribute) {
		return base.getInteger(attribute);
	}

	@Override
	public int size() {
		return base.size();
	}

	@Override
	public Iterator<String> iterator() {
		return base.iterator();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		propertyChangeSupport.firePropertyChange( evt );
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		IConceptBase newBase = new ConceptBase();
		Iterator<String> iterator = base.iterator();
		while( iterator.hasNext() ){
			String key = iterator.next();
			newBase.set(key, base.get( key));
		}
		return new BindingConceptBaseWrapper( newBase );
	}
}