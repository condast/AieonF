package org.aieonf.commons.ui.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.commons.ui.celleditors.AbstractCheckBoxCellEditor;
import org.aieonf.commons.ui.edit.CheckBoxEditingSupport;
import org.aieonf.commons.ui.images.AbstractImages.ImageSize;
import org.aieonf.commons.ui.images.DashboardImages;
import org.aieonf.commons.ui.images.LabelProviderImages;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public abstract class AbstractTableViewerWithDelete<T extends Object> extends Composite {
	private static final long serialVersionUID = 1L;

	public enum Buttons{
		ADD,
		DELETE,
		DONTSHOW,
		REMOVE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	private TableViewer viewer;	
	private DeleteViewerComparator comparator; 

	private int deleteColumnIndex;
	
	private Composite buttonBar;
	private Button btnAddButton;
	private Button btnDeleteButton;
	private Button btnDontShowButton;
	
	private boolean barOnTop;
	private boolean includeAddButton;

	private TableColumnLayout tableLayout;
	private Collection<SelectionListener> listeners;
	
	private int selectedColumn;
	
	private boolean disposed;
	
	protected AbstractTableViewerWithDelete( Composite parent, int style ) {
		this( parent, style, false );
	}

	protected AbstractTableViewerWithDelete( Composite parent, int style, boolean includeAddButton ) {
		super( parent,style );
		this.includeAddButton = includeAddButton;
		this.deleteColumnIndex = 0;
		listeners = new ArrayList<SelectionListener>();
		createContentComposite( parent, style );
		this.selectedColumn = 0;
		this.disposed = false;
		this.init();
	}

	protected void createContentComposite( Composite parent, int style ){
		setLayout( new GridLayout( 1, false ) );

		if( this.barOnTop ){
			buttonBar = new Composite( this, SWT.NONE );
		}

		//Required for table viewer
		Composite comp = new Composite( this, SWT.NONE );
		comp.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
		tableLayout = new TableColumnLayout();
		comp.setLayout( tableLayout );

		viewer = new TableViewer( comp, SWT.BORDER|SWT.MULTI|SWT.FULL_SELECTION );
		Table table = viewer.getTable();
		table.setHeaderVisible( true );
		table.setLinesVisible( true );
		table.addMouseListener( new MouseAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseDown( MouseEvent event ) {
		        try {
					Point p = new Point( event.x, event.y );
					ViewerCell cell = viewer.getCell( p );
					if( cell == null )
						return;
					selectedColumn = cell.getColumnIndex();
				} catch( Exception e ) {
					e.printStackTrace();
				}
		    }
		});
		viewer.setUseHashlookup( true );
		comparator = new DeleteViewerComparator();
		viewer.setComparator( comparator );
		viewer.setContentProvider( ArrayContentProvider.getInstance() );
		viewer.setLabelProvider( new DeleteLabelProvider() );
	    viewer.addDoubleClickListener( new IDoubleClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void doubleClick( DoubleClickEvent event ) {				
				try {
					IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
					Iterator<?> iterator = selection.iterator();
					while( iterator.hasNext() ){
						StoreWithDelete swd = (AbstractTableViewerWithDelete<T>.StoreWithDelete)iterator.next() ;
						onRowDoubleClick( swd.data );
					}
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
		});

		if( !this.barOnTop ){
			buttonBar = new Composite( this, SWT.NONE );
		}
		
		GridData gd_buttons = new GridData( SWT.FILL, SWT.FILL, true, false );
		buttonBar.setLayoutData( gd_buttons );
		this.setupButtonbar( buttonBar );
		requestLayout();
	}
	
	//Allow modification of the button
	protected abstract void onRowDoubleClick( T selection );

	protected void init(){ /* Default NOTHING */ }

	protected boolean isBarOnTop() {
		return barOnTop;
	}

	protected void setBarOnTop( boolean barOnTop ) {
		this.barOnTop = barOnTop;
	}
		
	public Composite getButtonBar() {
		return buttonBar;
	}

	protected int getSelectedColumn() {
		return selectedColumn;
	}

	protected void setupButtonbar( Composite buttonBar ){		
		buttonBar.setLayout( new GridLayout( 3, false ) );
		Label label = new Label( buttonBar, SWT.NONE );
		label.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
		
		btnDeleteButton = createButton( Buttons.DELETE );
		btnDeleteButton.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected( SelectionEvent e ) {
				try {
					boolean result = deleteButtonPressed( e );
					btnDeleteButton.setEnabled( result );
				}
				catch( Exception ex ){
					ex.printStackTrace();
				}
			}
		});
		btnDeleteButton.setImage( DashboardImages.getImage( DashboardImages.Images.DELETE ) );
		if( !this.includeAddButton )
			return;
		
		btnAddButton = createButton( Buttons.ADD );
		btnAddButton.setEnabled( true );
		btnAddButton.addSelectionListener( new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected( SelectionEvent e ) {
				try {
					onAddButtonSelected( e );
					super.widgetSelected( e );
				} catch( Exception e1 ) {
					e1.printStackTrace();
				}
			}
		});
		btnAddButton.setImage( DashboardImages.getImage( DashboardImages.Images.ADD ) );
	}
	

	/**
	 * Create a button with default behaviour
	 * @param button
	 * @return
	 */
	protected Button createButton( Buttons buttonType ){
		Button btn = new Button( buttonBar, SWT.RIGHT );
		btn.setLayoutData( new GridData( SWT.RIGHT, SWT.FILL ) );//old
		btn.setEnabled( false );
		btn.setAlignment( SWT.CENTER );
		btn.setText( buttonType.toString() );
		this.onButtonCreated( buttonType, btn );
		return btn;
		
	}

	/**
	 * Create a button of the wished size and type
	 * If the corresponding button image is not present or has not the same name as the buttonType, show its text.
	 * Sometimes The position of the button must be corrected, because it is not displayed right.
	 * @param buttonType, the corresponding type described in Buttons, like Buttons.ADD, DELETE, REMOVE
	 * @param buttonSize, the corresponding size described in ImageSize, like AbstractImages.ImageSize.TINY, SMALL, and so on
	 * @return btn, the desired button
	 */
	protected Button createButton( Buttons buttonType, ImageSize buttonSize ){
		int correctedPosition;
		switch( buttonSize ) {
			case TINY://16 The tiny image won't otherwise fit correctly onto the button.
					  //Only necessary for btn.setImage, not for btn.setBackgroundImage.
				correctedPosition = SWT.RIGHT;
			break;
			case SMALL://24
			case MEDIUM://32
			case LARGE://48
			case BIG://64
			case HUGE://128
				correctedPosition = SWT.CENTER;
			break;
			default:
				correctedPosition = SWT.CENTER;
		}
		Button btn = new Button( buttonBar, correctedPosition | SWT.PUSH );
		//if there exists a picture with the same enumname, put the picture onto the button:
		if( ( (buttonType.name() ).equals( (DashboardImages.Images.valueOf( buttonType.name() ) ).name() ) ) ){
			DashboardImages.Images dashboardImagesEnum = DashboardImages.Images.valueOf( buttonType.name() );
			Image image = DashboardImages.getImage( dashboardImagesEnum, buttonSize );
			btn.setImage( image );
			int bSize = buttonSize.getSize();
			btn.setSize( bSize, bSize );// <-- btn.setSize does not solve the wrong position for TINY
			GridData gridData = new GridData( bSize, bSize );
			btn.setLayoutData( gridData );
		}
		else {//else show the text:
			btn.setText( buttonType.toString() );
			btn.setLayoutData( new GridData( SWT.RIGHT, SWT.TOP, true, true ) );
		}
		btn.setEnabled( false );
		//this.onButtonCreated( buttonType, btn );// <-- not necessary to change the button in a subclass any more.
		return btn;
	}

	//Allow modification of the button
	protected abstract void onButtonCreated( Buttons type, Button button );
	
	/**
	 * respond to an add button click.
	 * returns true if the add was completed successfully.
	 * @return
	 */
	protected boolean onAddButtonSelected( SelectionEvent e ){ 
		return true;
	}

	/**
	 * respond to a delete button click.
	 * returns true if the deletion was completed successfully.
	 * @return
	 */
	protected abstract boolean onDeleteButton( Collection<T> deleted );
	
	/**
	 * @param columnName the column heading
	 * @param index
	 * @param weight
	 * @param type for instance expressed in SWT.NONE, SWT.LEFT, SWT.CENTER or SWT.RIGHT
	 * @return TableViewerColumn the column you want
	 */
	protected TableViewerColumn createColumn( String columnName, int index, int weight, int type ) {
		TableViewerColumn result = new TableViewerColumn( viewer, type );
		TableColumn tcolumn = result.getColumn();
		if( !StringUtils.isEmpty( columnName ) )
			tcolumn.setText( columnName );
		tcolumn.setMoveable( true );
		tcolumn.addSelectionListener( getSelectionAdapter( tcolumn, index ) );
		tableLayout.setColumnData( tcolumn, new ColumnWeightData( weight ) );
		return result;
	}

	protected TableViewerColumn createColumn( String columnName, int index, int weight ) {
		return createColumn( columnName, index, weight, SWT.NONE );
	}

	private SelectionAdapter getSelectionAdapter( final TableColumn column,
			final int index ) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected( SelectionEvent e ) {
				try {
					comparator.setColumn( index );
					int dir = comparator.getDirection();
					viewer.getTable().setSortDirection( dir );
					viewer.getTable().setSortColumn( column );
					viewer.refresh();
				} catch( Exception e1 ) {
					e1.printStackTrace();
				}
			}
		};
		return selectionAdapter;
	}

	public void addSelectionListener( SelectionListener listener ) {
		listeners.add( listener );
	}

	public void removeSelectionListener( SelectionListener listener ) {
		listeners.add( listener );
	}

	protected void notifyWidgetSelected( SelectionEvent event ){
		for( SelectionListener listener: this.listeners )
			listener.widgetSelected( event );
	}

	@SuppressWarnings("unchecked")
	protected boolean deleteButtonPressed( SelectionEvent event ){
		Collection<StoreWithDelete> input = (Collection<StoreWithDelete>)this.viewer.getInput();
		Map<StoreWithDelete, T> deleted = new HashMap<StoreWithDelete, T>();
		for( StoreWithDelete swd: input ){
			if( swd.isDelete() )
				deleted.put( swd, swd.getStore() );
		}
		boolean result = onDeleteButton( deleted.values() );
		if( result ){
			input.removeAll( deleted.keySet() );
			this.viewer.setInput( input );
		}
		btnDeleteButton.setEnabled( !result );
		this.notifyWidgetSelected( event );
		this.refresh();
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

	/**
	 * @param deleteColumnindex
	 * @param name
	 * @param weight
	 * @param style for instance expressed in SWT.NONE, SWT.LEFT, SWT.CENTER or SWT.RIGHT
	 * @return TableViewerColumn the column you want
	 */
	protected TableViewerColumn createDeleteColumn( int deleteColumnindex, String name, int weight, int style ) {
		TableViewerColumn tcol = this.createColumn( name, deleteColumnindex, weight, style );
		tcol.setEditingSupport( new CheckBoxEditingSupport<StoreWithDelete>( viewer, new DeleteCheckBoxEditor() ) );
		this.deleteColumnIndex = deleteColumnindex;
		return tcol;
	}

	/**
	 * Returns true if the table is empty
	 * @return
	 */
	public boolean isEmpty(){
		return ( getStoreInput() == null ) ? true: getStoreInput().isEmpty();
	}

	@SuppressWarnings("unchecked")
	protected Collection<IStoreWithDelete<T>> getStoreInput(){
		return (Collection<IStoreWithDelete<T>>)viewer.getInput();
	}
	
	@SuppressWarnings("unchecked")
	protected T[] getInput(){
		Collection<StoreWithDelete> input = (Collection<AbstractTableViewerWithDelete<T>.StoreWithDelete>)viewer.getInput();
		if( Utils.assertNull( input ) )
			return null;
		Collection<T> results = new ArrayList<T>();
		for( StoreWithDelete str: input )
			results.add( str.getStore() );
		return (T[])results.toArray();
	}
	
	protected void setInput( Collection<T> elements ){
		Collection<StoreWithDelete> input = new ArrayList<StoreWithDelete>();
		int index = 0;
		for( T element: elements ){
			input.add( new StoreWithDelete( element, index++, elements.size() ) );
		}
		viewer.setInput( input );		
	}

	protected abstract void onRefresh();
	
	protected void refresh(){
		if( isDisposed() || disposed )
			return;
		getDisplay().asyncExec( new Runnable(){

			@Override
			public void run() {
				if( isDisposed() || disposed )
					return;
				try {
					onRefresh();
				}
				catch( Exception ex ) {
					ex.printStackTrace();
				}
				requestLayout();		
			}
		});
	}

	/**
	 * sometimes the index of the comparator should be set
	 * externally.
	 * @param index
	 */
	public void setComparatorIndex( int index, int direction ){
		comparator.setColumnIndex( index, direction );
	}
	
	/**
	 * Create a comparator for the given column. return null if a
	 *  default is good enough.
	 *  
	 * @param index
	 * @return
	 */
	protected Comparator<T> getColumnComparator( int index ){
		return null;
	}

	@Override
	public void dispose() {
		disposed = true;
		super.dispose();
	}

	private class DeleteViewerComparator extends ViewerComparator{
		private static final long serialVersionUID = 1L;
		private int selectedColumn;
		private int direction = SWT.DOWN;
		
		int getDirection(){
			return direction;
		}

		/**
		 * Set the column index, without toggling
		 * @param index
		 */
		private void setColumnIndex( int index, int direction ){
			this.selectedColumn = index;
			this.direction = direction;
		}
		
		void setColumn( int index ) {
			if( index == this.selectedColumn ) {
				// Same column as last sort; toggle the direction
				direction = ( direction == SWT.UP ) ? SWT.DOWN: SWT.UP;
			} else {
				// New column; do an ascending sort
				direction = SWT.DOWN;
				this.selectedColumn = index;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public int compare( Viewer vwer, Object e1, Object e2 ) {
			if( selectedColumn == deleteColumnIndex ){
				Collection<StoreWithDelete> items = (Collection<StoreWithDelete>)viewer.getInput(); 
				for( StoreWithDelete item: items )
					item.setDelete( direction == SWT.DOWN );
				return 0;
			}
			Comparator<T> comp = getColumnComparator( selectedColumn );
			StoreWithDelete swd1 = (StoreWithDelete)e1;
			StoreWithDelete swd2 = (StoreWithDelete)e2;
			int rc = ( comp == null ) ? super.compare( vwer, swd1.getText( selectedColumn ), swd2.getText( selectedColumn ) ) : 
				comp.compare( swd1.getStore(), swd2.getStore() );
			return (direction == SWT.DOWN) ? rc: -rc ;
		}	
	}
	
	protected class DeleteLabelProvider extends LabelProvider implements ITableLabelProvider{
		private static final long serialVersionUID = 1L;

		@Override
		public String getColumnText( Object element, int columnIndex ) {
			return ( columnIndex < deleteColumnIndex ) ? null : "";
		}

		@SuppressWarnings("unchecked")
		@Override
		public Image getColumnImage( Object arg0, int columnIndex ) {
			IStoreWithDelete<T> swd = (IStoreWithDelete<T>)arg0;
			Image image = null;
			if( deleteColumnIndex == columnIndex ){
				image = setCheckedButton( true, swd.isDelete() );
				if( btnDeleteButton != null )
					btnDeleteButton.setEnabled( deleteItems() );
				if( btnDontShowButton != null )
					btnDontShowButton.setEnabled( deleteItems() );
			}
			return image;
		}

		/**
		 * Set the correct checkbox image for the given value. If enabled is false,
		 * the checkbox is grayed
		 * @param button
		 * @param enabled
		 * @param value
		 * @return
		 */
		protected Image setCheckedButton( boolean enabled, boolean value ){
			LabelProviderImages images = new LabelProviderImages();
			Image image = value ? images.getImage( LabelProviderImages.Images.CHECKED, enabled ) : 
				images.getImage( LabelProviderImages.Images.UNCHECKED, enabled );
			return image;
		}
		
		@SuppressWarnings("unchecked")
		private boolean deleteItems(){
			Collection<StoreWithDelete> items = (Collection<AbstractTableViewerWithDelete<T>.StoreWithDelete>)viewer.getInput(); 
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
		public void addText( String text ) {
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
			super.getData().setDelete( !value );
		}

		@Override
		protected Object doGetValue() {
			return super.getData().isDelete();
		}

		@Override
		protected void doSetValue( Object value ) {
			super.getData().setDelete( (boolean)value );
		}
	}
}