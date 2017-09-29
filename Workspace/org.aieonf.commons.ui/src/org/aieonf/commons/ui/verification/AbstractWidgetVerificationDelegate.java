package org.aieonf.commons.ui.verification;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

public abstract class AbstractWidgetVerificationDelegate implements IWidgetVerificationDelegate{	
	
	private boolean disable;
	
	private Collection<VerifyListener> listeners;
	
	protected AbstractWidgetVerificationDelegate() {
		listeners = new ArrayList<VerifyListener>();
	}

	
	protected boolean isDisable() {
		return disable;
	}


	public void disable(boolean disable) {
		this.disable = disable;
	}


	@Override
	public void addVerifyListener( VerifyListener listener ){
		this.listeners.add( listener );
	}
	
	@Override
	public void removeVerifyListener( VerifyListener listener ){
		this.listeners.remove( listener );
	}

	@Override
	public VerifyListener getVerifyListener(  ){
		return new ExtendedVerifyListener();
	}

	protected void notifyVerificationListeners( VerifyEvent event ){
		for( VerifyListener listener: listeners )
			listener.verifyText(event);
	}
	
	public boolean verifyWidget(VerifyEvent event) {
		VerificationTypes type = getVerificationType( event.widget);;
		return this.verifyWidget( event, type );
	}

	
	/**
	 * Implement the verification. If the verification succeeeds, a notification is given
	 * so that a next step can be startesd
	 * @param event
	 * @param type
	 * @return
	 */
	protected abstract boolean onVerifyEvent( VerifyEvent event, VerificationTypes type );
	
	@Override
	public boolean verifyWidget(VerifyEvent event, VerificationTypes type) {
		boolean retval = onVerifyEvent(event, type );
		if( retval )
			notifyVerificationListeners(event);
		return retval;
	}

	private class ExtendedVerifyListener implements VerifyListener{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void verifyText(VerifyEvent event) {
			if( disable )
				return;
			VerificationTypes type = getVerificationType( event.widget );
			onVerifyEvent(event, type );
			notifyVerificationListeners(event);
		}
	};

	public static VerificationTypes getVerificationType( Widget control ){
		VerificationTypes type = (VerificationTypes) control.getData( VERIFICATION_TYPE );
		if( type == null )
			type = VerificationTypes.ALPHABET;
		return type;
	}

	public static void setVerificationType( Widget control, VerificationTypes type ){
		control.setData( VERIFICATION_TYPE, type);
	}


	/**
	 * Perform a default verification action
	 * @param event
	 * @param type
	 * @return
	 */
	public static boolean defaultVerificationAction( VerifyEvent event, VerificationTypes type, String tooltipText ){
		if( VerificationTypes.CUSTOM.equals( type ))
			return false;
		boolean result = defaultVerificationAction(event.text, (Control) event.widget, type, tooltipText);
		event.data = result;
		return result;
	}

	/**
	 * Perform a default verification action. Returns true if the string is accepted
	 * @param event
	 * @param type
	 * @return
	 */
	public static boolean defaultVerificationAction( String text, Control control, VerificationTypes type, String tooltipText ){
		String str = text.replace(" ", "");
		boolean retval =  type.verify( str );
		return defaultVerificationAction(retval, control, tooltipText);
	}

	/**
	 * Perform a default verification action. Returns true if the string is accepted
	 * @param event
	 * @param type
	 * @return
	 */
	public static boolean defaultVerificationAction( boolean correct, Control control, String tooltipText ){
		Display display = Display.getCurrent();
		Color colour = correct ? display.getSystemColor( SWT.COLOR_LIST_BACKGROUND ): 
			new Color (display, RGB_INVALID[0], RGB_INVALID[1], RGB_INVALID[2]);		
		control.setBackground( colour );
		String tooltip = correct? "": tooltipText;
		control.setToolTipText(tooltip);
		return correct;
	}
}