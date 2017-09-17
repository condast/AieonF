package org.aieonf.osgi.wizard;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.osgi.flow.IFlowControl;
import org.aieonf.osgi.flow.IFlowControl.Direction;
import org.aieonf.osgi.wizard.IAddWizardPageListener.PageActions;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class AbstractFlowControlWizard<T extends Object> implements IWizard, IFlowControlWizard<T> {
	
	private IHeadlessWizardContainer container;
	private Composite parent;
	
	private Collection<IButtonSelectionListener<T>> listeners;
	private String title;
	private IFlowControl flow;
	
	private Collection<IAddWizardPageListener> wpls;
	
	private T data;

	protected AbstractFlowControlWizard() {
		listeners = new ArrayList<IButtonSelectionListener<T>>();
		wpls = new ArrayList<IAddWizardPageListener>();
	}
	
	protected AbstractFlowControlWizard( IFlowControl flow, String title ) {
		this();
		this.flow = flow;
		this.title = title;
	}

	@Override
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}

	protected IFlowControl getFlowControl() {
		return flow;
	}

	protected void setFlow(IFlowControl flow) {
		this.flow = flow;
	}

	/**
	 * Create the control for this wizard( usually a wizard container)
	 * @param parent
	 * @param style
	 * @return
	 */
	@Override
	public void createPageControls(Composite parent) {
		this.parent = parent;
		this.selectContainer( getFlowControl().getIndex() );
		updatePage( getFlowControl().getIndex() );
	}
	
	/**
	 * Create the container for this wizard
	 * @param parent
	 * @param style
	 * @return
	 */
	protected abstract IHeadlessWizardContainer onSelectContainer( int index );

	protected abstract void initialiseContainer( IHeadlessWizardContainer container );

	protected void selectContainer( int index ){
		boolean updateContainer = false;
		try{
			IWizardContainer test = this.onSelectContainer( index );
			updateContainer = this.changeContainer(test);

			IWizardPage page = this.container.getCurrentPage();
			if( page instanceof IFlowWizardPage ){
				IFlowWizardPage fpage=  (IFlowWizardPage) page;
				test = fpage.selectContainer(this.container ); 
				updateContainer |= changeContainer(test);
			}
			if( updateContainer){
				this.container.createComposite(parent, SWT.NONE );
				this.initialiseContainer(container);
				parent.layout(true);//Needed to resize the composite to the correct dimensions
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	/**
	 * Change the current container with the new one, if they are not equal
	 * @param newContainer
	 * @return
	 */
	protected boolean changeContainer( IWizardContainer newContainer ){
		if(( newContainer == null ) || ( newContainer.equals( this.container )))
			return false;
		if( this.container != null )
			this.container.dispose();
		IHeadlessWizardContainer hwc = (IHeadlessWizardContainer) newContainer;
		if( this.container == null ){
			this.container = hwc;
			addPages();
		}else{
			hwc.setPages( this.container.getPages());
			this.container = hwc;
		}
		return true;
	}
	
	/**
	 * Support method to create a wizard page from a composite
	 * @param pageName
	 * @return
	 */
	protected abstract IWizardPage onAddPage( String pageName, String description, String message );
	
	protected IWizardPage addPage( String pageName, String description, String message  ){
		IWizardPage page = onAddPage( pageName, description, message );
		IAddWizardPageListener.PageActions action = (page == null )? PageActions.NULL: PageActions.INTERNAL;
		switch( action ){
		case INTERNAL:
			break;
		case NULL:
			page = notifyListeners( new PageActionEvent(this, getFlowControl(), action, pageName, description, message));
			if( page == null )
				page = new NullWizardPage(this, flow, pageName, description, message);
		}
		this.container.addPage( page );
		return page;
	}

	protected Composite getParent() {
		return parent;
	}

	/**
	 * Clear the wizard. This does NOT reactivate the wizard!
	 */
	@Override
	public void clear(){
		this.data = null;
		updatePage(0);
		this.flow.init();
	}
	
	/* (non-Javadoc)
	 * @see org.condast.commons.ui.wizard.IButtonSelectionComposite#addListener(org.condast.commons.ui.wizard.IButtonSelectionListener)
	 */
	@Override
	public void addListener( IButtonSelectionListener<T> listener ){
		this.listeners.add( listener );
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.wizard.IButtonSelectionComposite#removeListener(org.condast.commons.ui.wizard.IButtonSelectionListener)
	 */
	@Override
	public void removeListener( IButtonSelectionListener<T> listener ){
		this.listeners.remove( listener );
	}

	protected void notifyListeners( ButtonEvent<T> event ){
		for( IButtonSelectionListener<T> listener: this.listeners ){
			listener.notifyButtonPressed(event);
		}
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.wizard.IButtonSelectionComposite#addListener(org.condast.commons.ui.wizard.IButtonSelectionListener)
	 */
	@Override
	public void addWizardPageListener( IAddWizardPageListener listener ){
		this.wpls.add( listener );
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.wizard.IButtonSelectionComposite#removeListener(org.condast.commons.ui.wizard.IButtonSelectionListener)
	 */
	@Override
	public void removeListener( IAddWizardPageListener listener ){
		this.wpls.remove( listener );
	}

	protected IWizardPage notifyListeners( PageActionEvent event ){
		IWizardPage page = null;
		for( IAddWizardPageListener listener: this.wpls ){
			page = listener.notifyWizardPageAction(event);
		}
		return page;
	}

	@Override
	public boolean canFinish() {
		return this.container.canFinish( this.flow.getIndex() );
	}

	@Override
	public IWizardContainer getContainer() {
		return container;
	}

	@Override
	public Image getDefaultPageImage() {
		return null;
	}

	@Override
	public IDialogSettings getDialogSettings() {
		return null;
	}

	@Override
	public IWizardPage getPage(String pagename) {
		for( IWizardPage page: this.container.getPages() ){
			if( page.getName().equals( pagename ))
				return page;
		}
		return null;
	}

	@Override
	public int getPageCount() {
		return this.container.size();
	}

	@Override
	public IWizardPage[] getPages() {
		return this.container.getPages();
	}
			
	protected IWizardPage updatePage( int index ){
		int indx = ( index < 0 )?0: index;
		IWizardPage  page = this.container.getPage( indx );
		container.refresh();
		if( flow.isCompleted() )
			return page;
		container.showPage(page);
		return page;
	}
	
	/**
	 * Get the current page
	 * @return
	 */
	protected IWizardPage getCurrentPage(){
		return container.getCurrentPage();
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage arg0) {
		this.flow.setDirection( Direction.PROCEED);
		this.synchronizeData( arg0 );
		boolean lastPage = isLastPage();
		int newIndex = this.flow.next();
		selectContainer( newIndex );
		IWizardPage page = null;
		if( !lastPage){
			page = updatePage( newIndex );
			notifyListeners( new ButtonEvent<T>( this, IButtonWizardContainer.Buttons.NEXT ));
		}
		else{
			int index = this.flow.getIndex();
			page = updatePage( index );
			flow.complete();
			notifyListeners( new ButtonEvent<T>( this, IButtonWizardContainer.Buttons.FINISH ));
		}
		return page;
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage arg0) {
		this.flow.setDirection( Direction.BACKTRACK);
		this.synchronizeData( arg0 );
		int index = this.flow.previous();
		selectContainer( index );
		IWizardPage  page = updatePage( index );
		notifyListeners( new ButtonEvent<T>( this, IButtonWizardContainer.Buttons.PREVIOUS ));
		return page;
	}
	
	@Override
	public IWizardPage getStartingPage() {
		return this.container.getPage(0);
	}

	/**
	 * Returns true if the index points to the last page
	 * @return
	 */
	protected boolean isLastPage(){
		int index = this.flow.getIndex();
		return this.container.isLastPage(index);
	}
	
	@Override
	public RGB getTitleBarColor() {
		return null;
	}

	@Override
	public String getWindowTitle() {
		return title;
	}

	protected void setWindowTitle(String title) {
		this.title = title;
	}

	@Override
	public boolean isHelpAvailable() {
		return false;
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		return ( container.size() > 1 );
	}

	@Override
	public void setContainer(IWizardContainer arg0) {
		this.container = (IHeadlessWizardContainer) arg0;
	}	

	@Override
	public boolean needsProgressMonitor() {
		return false;
	}

	/**
	 * The change of a wizard page often implies synchronization with
	 * one or more data objects. This is performed during a'next' , 'previous' and
	 * 'finish'
	 */
	protected void synchronizeData( IWizardPage arg0 ){
		if(!( arg0 instanceof IFlowWizardPage ))
			return;
		IFlowWizardPage page = (IFlowWizardPage) arg0;
		page.synchronizeData();
	}
	
	@Override
	public boolean performCancel() {
		flow.complete();
		this.clear();
		this.notifyListeners( new ButtonEvent<T>( this, IButtonWizardContainer.Buttons.CANCEL ));
		return false;
	}

	@Override
	public boolean performFinish() {
		this.synchronizeData( this.getCurrentPage());
		flow.complete();
		this.notifyListeners( new ButtonEvent<T>( this, IButtonWizardContainer.Buttons.FINISH ));
		return false;
	}

	@Override
	public void dispose() {
		flow.complete();
		this.listeners.clear();		
	}
	
	/**
	 * Is used to create a blank wizard page if a null pages was added
	 * @author Kees
	 *
	 */
	private static class NullWizardPage extends AbstractFlowWizardPage{
		private static final long serialVersionUID = 1L;

		public NullWizardPage(IWizard wizard, IFlowControl flow, String pageName, String description, String message) {
			super(wizard, flow, pageName, description, message);
		}

		@Override
		public IWizardContainer selectContainer(IWizardContainer currentContainer) {
			return null;
		}

		@Override
		public Control onCreatePage(Composite parent) {
			return new Composite( parent, SWT.BORDER);
		}

		@Override
		public void onCreateIcon(Composite iconbar) { /* NOTHING */}		
		
		@Override
		protected void cleanup(Control control) { /* NOTHING */ }

		@Override
		protected void onUpdateControl(Control control) {/* NOTHING */}
	}
}