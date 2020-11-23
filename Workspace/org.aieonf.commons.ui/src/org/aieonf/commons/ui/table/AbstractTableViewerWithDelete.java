package org.aieonf.commons.ui.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.ui.celleditors.AbstractCheckBoxCellEditor;
import org.aieonf.commons.ui.edit.CheckBoxEditingSupport;
import org.aieonf.commons.ui.images.IInformationImages.Information;
import org.aieonf.commons.ui.images.InformationImages;
import org.aieonf.commons.ui.widgets.IStoreWithDelete;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public abstract class AbstractTableViewerWithDelete<T extends Object> extends Composite {
	private static final long serialVersionUID = 1L;

	private static final String S_SAIGHT_TITLE = "SaightTitle";
	
	public enum Buttons{
		ADD,
		DELETE,
		REMOVE,
		BACK,
		NEXT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	private TableViewer viewer;	
	private DeleteViewerComparator comparator; 

	private int deleteColumnIndex;
	
	private Composite buttonBar;
	private Label lbl_title;
	private Button btnDeleteButton;
	private Button btnAddButton;
	
	private boolean barOnTop;
	private boolean includeAddButton;

	private TableColumnLayout tableLayout;
	private Collection<SelectionListener> listeners;
	
	protected AbstractTableViewerWithDelete(Composite parent, int style ) {
		this( parent, style, false );
	}

	protected AbstractTableViewerWithDelete(Composite parent, int style, boolean includeAddButton ) {
		this( parent, style, includeAddButton, false );
	}
	
	protected AbstractTableViewerWithDelete(Composite parent, int style, boolean includeAddButton, boolean barOnTop ) {
		super(parent,style);
		this.includeAddButton = includeAddButton;
		this.barOnTop = barOnTop;
		this.deleteColumnIndex = 0;
		listeners = new ArrayList<SelectionListener>();
		createContentComposite( parent, style );
		this.init();
	}

	protected void createContentComposite( Composite parent,int style ){
		setLayout(new GridLayout(1, false));

		if( this.barOnTop ){
			buttonBar = new Composite(this, SWT.RIGHT_TO_LEFT );
		}

		Composite comp = new Composite( this, SWT.NONE );
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableLayout = new TableColumnLayout();
		comp.setLayout(tableLayout);

		viewer = new TableViewer( comp, style );
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible( true );

		viewer.setUseHashlookup( true );
		comparator = new DeleteViewerComparator();
		viewer.setComparator( comparator );
		viewer.setContentProvider( ArrayContentProvider.getInstance());
		viewer.setLabelProvider( new DeleteLabelProvider() );
	    viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				Iterator<?> iterator = selection.iterator();
				while( iterator.hasNext() ){
					StoreWithDelete swd = (AbstractTableViewerWithDelete<T>.StoreWithDelete) iterator.next() ;
					onRowSelected( swd.data, false );
				}
			}
		});
	    viewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void doubleClick(DoubleClickEvent event) {				
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				Iterator<?> iterator = selection.iterator();
				while( iterator.hasNext() ){
					StoreWithDelete swd = (AbstractTableViewerWithDelete<T>.StoreWithDelete) iterator.next() ;
					onRowSelected( swd.data, true );
				}
			}
		});

		if( !this.barOnTop ){
			buttonBar = new Composite(this, SWT.NONE);
		}
		
		GridData gd_buttons = new GridData(SWT.FILL, SWT.FILL, true, false);
		buttonBar.setLayoutData(gd_buttons);
		this.setupButtonbar(buttonBar);
		lbl_title = new Label( buttonBar, SWT.NONE );
		lbl_title.setLayoutData( new GridData( SWT.CENTER, SWT.FILL, true, true ));
		lbl_title.setData( RWT.CUSTOM_VARIANT, S_SAIGHT_TITLE);
	}

	//Set the title of the 
	protected void setTitle( String title ){
		lbl_title.setText( title );
	}

	//Allow modification of the button
	protected abstract void onRowSelected( T selection, boolean doubleClick );

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

	protected void setupButtonbar( Composite buttonBar ){		
		buttonBar.setLayout( new GridLayout(3, false));
		
		btnDeleteButton = includeButton( Buttons.DELETE, Buttons.DELETE.toString(),
				InformationImages.getInstance().getImage( Information.DELETE, true ));
		btnDeleteButton.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = deleteButtonPressed( e );
				btnDeleteButton.setEnabled( result );
			}
		});
		if( !this.includeAddButton )
			return;
		
		btnAddButton = includeButton( Buttons.ADD, Buttons.ADD.toString() , 
				InformationImages.getInstance().getImage( Information.ADD, true ));
	}

	protected Button includeButton( Object id, String text, Image image ){
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
		btn.setText( text );
		btn.setImage( image );
		btn.setData( id );
		if( id instanceof Buttons )
			this.onButtonCreated( (Buttons) id, btn);
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

	/**
	 * respond to a delete button click.
	 * returns true if the deletion was completed successfully.
	 * @return
	 */
	protected abstract boolean onDeleteButton( Collection<T> deleted);

	protected TableViewerColumn createColumn( String columnName, int index, int weight  ) {
		TableViewerColumn result = new TableViewerColumn( viewer, SWT.NONE );
		TableColumn tcolumn = result.getColumn();
		tcolumn.setText( columnName );
		tcolumn.setMoveable( true );
		tcolumn.pack();
		tcolumn.addSelectionListener( getSelectionAdapter( tcolumn, index ));
		tableLayout.setColumnData(tcolumn, new ColumnWeightData(weight, true ));
		return result;
	}
	
	private SelectionAdapter getSelectionAdapter(final TableColumn column,
			final int index ) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn( index );
				int dir = comparator.getDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		return selectionAdapter;
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

	@SuppressWarnings("unchecked")
	private boolean deleteButtonPressed( SelectionEvent event ){
		Collection<StoreWithDelete> input = (Collection<StoreWithDelete>) this.viewer.getInput();
		Collection<T> deleted = new ArrayList<T>();
		for( StoreWithDelete swd: input ){
			if( swd.isDelete())
				deleted.add( swd.getStore());
		}
		boolean result = onDeleteButton( deleted );
		btnDeleteButton.setEnabled( !result );
		this.notifyWidgetSelected(event);
		return result;
	}

	protected Button getDeleteButton(){
		return this.btnDeleteButton;
	}

	protected Button getAddButton(){
		return this.btnAddButton;
	}

	protected TableViewer getViewer() {
		return viewer;
	}

	protected int getDeleteColumnindex() {
		return deleteColumnIndex;
	}

	protected TableViewerColumn createDeleteColumn(int deleteColumnindex, String name, int weight ) {
		TableViewerColumn tcol = this.createColumn( name, deleteColumnindex, weight);
		tcol.setEditingSupport( new CheckBoxEditingSupport<StoreWithDelete>(viewer, new DeleteCheckBoxEditor()));
		this.deleteColumnIndex = deleteColumnindex;
		return tcol;
	}

	/**
	 * Returns true if the table is empty
	 * @return
	 */
	public boolean  isEmpty(){
		return ( getStoreInput() == null )? true: getStoreInput().isEmpty();
	}

	@SuppressWarnings("unchecked")
	protected Collection<StoreWithDelete> getStoreInput(){
		return (Collection<AbstractTableViewerWithDelete<T>.StoreWithDelete>) viewer.getInput();
	}
	
	@SuppressWarnings("unchecked")
	protected T[] getInput(){
		Collection<StoreWithDelete> input = (Collection<AbstractTableViewerWithDelete<T>.StoreWithDelete>) viewer.getInput();
		if( Utils.assertNull( input ))
			return null;
		Collection<T> results = new ArrayList<T>();
		for( StoreWithDelete str: input)
			results.add( str.getStore());
		return (T[]) results.toArray();
	}
	
	protected void setInput( Collection<T> elements ){
		Collection<StoreWithDelete> input = new ArrayList<StoreWithDelete>();
		int index = 0;
		for( T element: elements ){
			input.add( new StoreWithDelete(element, index++, elements.size()));
		}
		viewer.setInput( input );		
	}

	protected abstract void onRefresh();
	
	protected void refresh(){
		Display.getCurrent().asyncExec( new Runnable(){

			@Override
			public void run() {
				onRefresh();
				layout( false );		
			}
		});
	}

	/**
	 * Create a comaparotr for the given column. return null if a
	 *  default is good enough.
	 *  
	 * @param index
	 * @return
	 */
	protected Comparator<T> getColumnComparator( int index ){
		return null;
	}

	private class DeleteViewerComparator extends ViewerComparator{
		private static final long serialVersionUID = 1L;
		private int selectedColumn;
		private int direction = SWT.DOWN;
		
		int getDirection(){
			return direction;
		}

		void setColumn( int index) {
			if (index == this.selectedColumn ) {
				// Same column as last sort; toggle the direction
				direction = ( direction == SWT.UP )? SWT.DOWN: SWT.UP;
			} else {
				// New column; do an ascending sort
				direction = SWT.DOWN;
				this.selectedColumn = index;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public int compare(Viewer vwer, Object e1, Object e2) {
			if( selectedColumn == deleteColumnIndex ){
				Collection<StoreWithDelete> items = (Collection<StoreWithDelete>) viewer.getInput(); 
				for( StoreWithDelete item: items )
					item.setDelete(direction == SWT.DOWN);
				return 0;
			}
			Comparator<T> comp = getColumnComparator( selectedColumn);
			StoreWithDelete swd1 = (StoreWithDelete) e1;
			StoreWithDelete swd2 = (StoreWithDelete) e2;
			int rc = ( comp == null )? super.compare(vwer, swd1.getText( selectedColumn ), swd2.getText( selectedColumn )): 
				comp.compare( swd1.getStore(),  swd2.getStore() );
			return (direction == SWT.DOWN)? rc: -rc ;
		}	
	}
	
	protected class DeleteLabelProvider extends LabelProvider implements ITableLabelProvider{
		private static final long serialVersionUID = 1L;


		@Override
		public String getColumnText(Object element, int columnIndex) {
			return ( columnIndex < deleteColumnIndex )? null : "";
		}

		@SuppressWarnings("unchecked")
		@Override
		public Image getColumnImage(Object arg0, int columnIndex) {
			IStoreWithDelete<T> swd = (IStoreWithDelete<T>) arg0;
			Image image = null;
			if( deleteColumnIndex == columnIndex ){
				image = setCheckedButton( true, swd.isDelete());
				btnDeleteButton.setEnabled( deleteItems());	
			}
			return image;
		}

		/**
		 * Set the correct checkbox image for the given value. If enabled is false,
		 * the checkbox is grayed
		 * @param button
		 * @param enabled
		 * @param isDelete
		 * @return
		 */
		protected Image setCheckedButton( boolean enabled, boolean isDelete ){
			Image image = isDelete ? InformationImages.getInstance().getImage( Information.CHECK, enabled) : InformationImages.getInstance().getImage( Information.UNCHECKED, enabled );
			return image;
		}
		
		@SuppressWarnings("unchecked")
		private boolean deleteItems(){
			Collection<StoreWithDelete> items = (Collection<AbstractTableViewerWithDelete<T>.StoreWithDelete>) viewer.getInput(); 
			for( IStoreWithDelete<T> item: items ){
				if( item.isDelete() )
					return true;
			}
			return false;
		}
	}

	protected class StoreWithDelete implements IStoreWithDelete<T>{

		private List<String> texts;
		private T data;
		private boolean delete;
		private int index, count;

		public StoreWithDelete( T data, int index, int count ) {
			super();
			this.data = data;
			this.delete = false;
			this.index = index;
			this.count = count;
			this.texts = new ArrayList<String>();
		}

		/* (non-Javadoc)
		 * @see org.condast.commons.ui.widgets.IStoreWithDelete#getText(int)
		 */
		@Override
		public String getText( int index ) {
			if( this.texts.isEmpty() )
				return null;
			return texts.get( index );
		}

		/* (non-Javadoc)
		 * @see org.condast.commons.ui.widgets.IStoreWithDelete#addText(java.lang.String)
		 */
		@Override
		public void addText(String text) {
			this.texts.add( text );
		}


		/* (non-Javadoc)
		 * @see org.condast.commons.ui.widgets.IStoreWithDelete#getIndex()
		 */
		@Override
		public int getIndex() {
			return index;
		}

		/* (non-Javadoc)
		 * @see org.condast.commons.ui.widgets.IStoreWithDelete#getCount()
		 */
		@Override
		public int getCount() {
			return count;
		}

		/* (non-Javadoc)
		 * @see org.condast.commons.ui.widgets.IStoreWithDelete#isDelete()
		 */
		@Override
		public boolean isDelete() {
			return delete;
		}

		public void setDelete( boolean choice ) {
			this.delete = choice;
		}

		/* (non-Javadoc)
		 * @see org.condast.commons.ui.widgets.IStoreWithDelete#getData()
		 */
		@Override
		public T getStore() {
			return data;
		}
	}
	
	private class DeleteCheckBoxEditor extends AbstractCheckBoxCellEditor<StoreWithDelete>{
		private static final long serialVersionUID = 1L;

		@Override
		protected void onToggle() {
			boolean value = super.getData().isDelete();
			super.getData().setDelete(!value);
		}

		@Override
		protected Object doGetValue() {
			return super.getData().isDelete();
		}

		@Override
		protected void doSetValue(Object value) {
			super.getData().setDelete( (Boolean) value );
		}
	}
}