package org.aieonf.commons.ui.widgets;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public abstract class AbstractButtonBarComposite extends Composite {
	private static final long serialVersionUID = 1L;

	private static final String S_SAIGHT_TITLE = "SaightTitle";
	
	public enum Buttons{
		ADD,
		DELETE,
		REMOVE,
		BACK,
		NEXT,
		UPDATE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	private Composite buttonBar;
	private Label lbl_title;
	
	private boolean barOnTop;

	private Collection<SelectionListener> listeners;
	
	protected AbstractButtonBarComposite(Composite parent, int style ) {
		this( parent, style, true );
	}
	
	protected AbstractButtonBarComposite(Composite parent, int style, boolean barOnTop ) {
		super(parent,style);
		this.barOnTop = barOnTop;
		listeners = new ArrayList<SelectionListener>();
		createContentComposite( parent, style );
		this.init();
	}

	protected abstract void onCreateComposite( Composite parent,int style );

	protected void createContentComposite( Composite parent,int style ){
		setLayout(new GridLayout(1, false));

		if( this.barOnTop ){
			buttonBar = new Composite(this, SWT.RIGHT_TO_LEFT );
		}

		onCreateComposite(this, style);

		if( !this.barOnTop ){
			buttonBar = new Composite(this, SWT.NONE);
		}
		buttonBar.setLayout(new GridLayout(5, true));
		GridData gd_buttons = new GridData(SWT.FILL, SWT.FILL, true, false);
		buttonBar.setLayoutData(gd_buttons);
		this.setupButtonbar(buttonBar);
		lbl_title = new Label( buttonBar, SWT.NONE );
		lbl_title.setLayoutData( new GridData( SWT.CENTER, SWT.FILL, false, true ));
		lbl_title.setData( RWT.CUSTOM_VARIANT, S_SAIGHT_TITLE);
		requestLayout();
	}

	//Set the title of the 
	protected void setTitle( String title ){
		lbl_title.setText( title );
	}

	protected void init(){
	}

	protected boolean isBarOnTop() {
		return barOnTop;
	}

	protected void setBarOnTop(boolean barOnTop) {
		this.barOnTop = barOnTop;
	}
		
	public Composite getButtonBar() {
		return buttonBar;
	}

	protected abstract void setupButtonbar( Composite buttonBar );

	protected Button includeButton( Buttons id, String text, Image image ){
		Button btn = new Button(buttonBar, SWT.RIGHT);
		btn.setLayoutData( new GridData(SWT.RIGHT, SWT.FILL));
		btn.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				onButtonSelected( e );
				super.widgetSelected(e);
			}
		});
		btn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btn.setEnabled(true);
		btn.setAlignment(SWT.CENTER);
		if(!StringUtils.isEmpty(text))
			btn.setText( text );
		if( image != null )
			btn.setImage( image );
		btn.setData( id );
		if( id instanceof Buttons )
			this.onButtonCreated( id, btn);
		return btn;
		
	}
	//Allow modification of the button
	protected abstract void onButtonCreated( Buttons type, Button button );
	
	/**
	 * respond to an add button click.
	 * returns true if the add was completed successfully.
	 * @return
	 */
	protected boolean onButtonSelected( SelectionEvent e){ 
		return true;
	}

	public void addSelectionListener(SelectionListener listener) {
		listeners.add( listener );
	}

	public void removeSelectionListener(SelectionListener listener) {
		listeners.add( listener );
	}

	protected void notifyWidgetSelected( SelectionEvent event ){
		for( SelectionListener listener: this.listeners )
			listener.widgetSelected(event);
	}

	protected abstract void onRefresh();
	
	protected void refresh(){
		Display.getCurrent().asyncExec( new Runnable(){

			@Override
			public void run() {
				onRefresh();
				requestLayout();		
			}
		});
	}
}