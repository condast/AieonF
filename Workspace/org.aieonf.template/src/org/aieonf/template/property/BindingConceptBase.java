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

/**
 * Create a concept, using a properties file
 */
public class BindingConceptBase extends ConceptBase implements PropertyChangeListener
{
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

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
		Iterator<String> iterator = super.iterator();
		while( iterator.hasNext()){
			String prop = iterator.next();
			PropertyChangeListener[] listeners = propertyChangeSupport.getPropertyChangeListeners(prop);
			for( PropertyChangeListener listener: listeners){
				propertyChangeSupport.firePropertyChange(prop, super.get( prop ), null );
				propertyChangeSupport.removePropertyChangeListener( listener);
			}
		}
		super.clear();
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#set(java.lang.String, java.lang.String)
	 */
	@Override
	public final void set( String key, String value )
	{
		String old = super.get( key );
		super.set( key, value );
		if( super.hasChanged())
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
			propertyChangeSupport.firePropertyChange(key, super.get( key ), null );
			propertyChangeSupport.removePropertyChangeListener( listener);
		}
		super.remove(key);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		propertyChangeSupport.firePropertyChange( evt );
	}
}
