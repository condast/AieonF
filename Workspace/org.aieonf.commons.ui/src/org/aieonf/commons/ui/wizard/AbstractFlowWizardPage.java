package org.aieonf.commons.ui.wizard;

import org.aieonf.commons.ui.controller.IManagedProvider;
import org.aieonf.commons.ui.flow.IFlowControl;
import org.aieonf.commons.ui.images.DashboardImages;
import org.aieonf.commons.ui.images.DashboardImages.Images;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class AbstractFlowWizardPage extends WizardPage implements IFlowWizardPage {
	private static final long serialVersionUID = 1L;
	
	protected static String S_HELP_ID = "HelpID";
	
	private boolean disposed;
	private IFlowControl flow;
	private String description;
	private String message;
	private boolean helpAvailable;
	private boolean blockUpdateTitleBar;

	public AbstractFlowWizardPage( IWizard wizard, IFlowControl flow, String pageName, String description, String message ){
		super( pageName, description, null );
		super.setWizard( wizard );
		this.description = description;
		this.message = message;
		this.flow = flow;
		this.disposed = false;
		this.helpAvailable = false;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	protected IFlowControl getFlowControl() {
		return flow;
	}

	protected boolean isHelpAvailable() {
		return helpAvailable;
	}

	protected void setHelpAvailable( boolean helpAvailable ) {
		this.helpAvailable = helpAvailable;
	}

	/**
	 * Create a new page in the 
	 * @param parent
	 * @param style
	 * @return
	 */
	public abstract Control onCreatePage( Composite parent );

	@Override
	public void createControl(Composite parent) {
		if( flow.isCompleted() )
			return;
		Control control = super.getControl();
		if( control != null )
			this.cleanup( control );
			
		control = onCreatePage( parent );
		if( control == null )
			return;
		this.blockUpdateTitleBar = true;
		super.setDescription(description);
		super.setMessage( message );
		onUpdateControl( control );
		super.setControl( control );
		this.blockUpdateTitleBar = false;
	}

	public abstract void onCreateIcon( Composite iconbar );
	
	@Override
	public void createIcon(Composite iconbar) {
		if(this.blockUpdateTitleBar )
			return;
		for( Control control: iconbar.getChildren())
			control.dispose();
		iconbar.setLayout( new FillLayout());
		this.onCreateIcon(iconbar);
		iconbar.layout( true );
	}

	protected void addToolBarButton( Composite toolbar ){/* NOTHING */};

	protected void onHelpButtonSelected( SelectionEvent e ){ /* NOTHING */}
	
	@Override
	public void createToolBar( Composite toolbar ){ 
		if(this.blockUpdateTitleBar )
			return;
		for( Control control: toolbar.getChildren())
			control.dispose();
		toolbar.setLayout( new GridLayout(4, true));
		if( this.helpAvailable ){
			Button button = createToolBarButton( S_HELP_ID, toolbar, SWT.PUSH);
			DashboardImages.getInstance();
			button.setImage( DashboardImages.getImage( Images.HELP));
			button.addSelectionListener( new SelectionAdapter(){
				private static final long serialVersionUID = 1L;

				@Override
				public void widgetSelected(SelectionEvent e) {
					onHelpButtonSelected(e);
					super.widgetSelected(e);
				}
			});
		}
		addToolBarButton(toolbar);
	}
	
	protected abstract void onUpdateControl( Control control );
	
	@Override
	public void synchronizeData() {
		if( getControl() instanceof IManagedProvider ){
			IManagedProvider<?> sp = (IManagedProvider<?>)getControl();
			sp.getInput();
		}
	}

	@Override
	public boolean isPageComplete() {
		if( flow.isCompleted() )
			return true;
		return super.isPageComplete();
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}
	
	public void setDisposed(boolean disposed) {
		this.disposed = disposed;
	}

	/**
	 * Clean up the current control prior to selecting a new page
	 * @param control
	 */
	protected abstract void cleanup( Control control );
	
	/**
	 * Refresh the current layout
	 */
	protected void refresh(){
		if(!( getContainer() instanceof IHeadlessWizardContainer))
			return;
		IHeadlessWizardContainer container = (IHeadlessWizardContainer) getContainer();
		container.refresh();
	}
	
	protected void onToolBarButtonSelected( SelectionEvent event ){/* NOTHING  */}
	
	/**
	 * The toolbar buttons have specific sizes
	 * @param toolbar
	 * @return
	 */
	protected Button createToolBarButton( String id, Composite toolbar, int style ){
		Button button = new Button( toolbar, style );
		button.setData(id);
		button.setLayoutData( new GridData( 32, 32 ));
		return button;
	}
}