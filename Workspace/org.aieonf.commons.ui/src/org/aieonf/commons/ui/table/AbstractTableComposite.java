package org.aieonf.commons.ui.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.aieonf.commons.strings.StringUtils;
import org.aieonf.commons.ui.IBindableWidget;
import org.aieonf.commons.ui.table.ITableEventListener.TableEvents;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.core.IModelLeaf;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;

public abstract class AbstractTableComposite<D extends IDescribable, C extends Object> extends Composite implements IBindableWidget<IModelLeaf<D>>
{
	private static final long serialVersionUID = 976428552549736382L;
	
	protected static final String S_INDEX = "INDEX";
	private Table table;
	
	private TableViewer tableViewer;
	private TableColumnLayout tclayout;
	private List<ColumnLabelProvider> columnLabelProviders;
	
	private IModelLeaf<D> input;
	
	private String title;
	
	private Collection<ITableEventListener<IModelLeaf<D>,C>> listeners;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	protected AbstractTableComposite( Composite parent, int style)
	{
		super(parent, style);
		this.listeners = new ArrayList<ITableEventListener<IModelLeaf<D>,C>>();
		this.columnLabelProviders = new ArrayList<>();
		this.prepare();
		this.createComposite(parent);
	}

	public void addTableEventListener( ITableEventListener<IModelLeaf<D>,C> listener ){
		this.listeners.add( listener );
	}

	public void removeTableEventListener( ITableEventListener<IModelLeaf<D>,C> listener ){
		this.listeners.remove( listener );
	}

	protected void notifyTableEvent( TableEvent<IModelLeaf<D>, C> event ){
		for( ITableEventListener<IModelLeaf<D>,C> listener: this.listeners ){
			listener.notifyTableEvent(event);
		}
	}
		
	/**
	 * The leaf that is currently set, or null if nothing has been added yet
	 * @return
	 */
	protected IModelLeaf<D> getInput() {
		return input;
	}

	/**
	 * Prepare the composite
	 */
	protected abstract void prepare();

	/**
	 * Prepare the composite
	 */
	@SuppressWarnings("unchecked")
	protected void onRowSelected( SelectionChangedEvent e ) {
		try {
			IStructuredSelection selection = (IStructuredSelection)e.getSelection();
			IModelLeaf<D> entry = (IModelLeaf<D>) selection.getFirstElement();
			notifyTableEvent( new TableEvent<IModelLeaf<D>, C>( this, TableEvents.SELECT, entry));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	/**
	 * Create the composite
	 * @param parent
	 */
	protected void createComposite( Composite parent ){
		tableViewer = new TableViewer( this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		table.setData( RWT.CUSTOM_ITEM_HEIGHT, Integer.valueOf( 22 ));		
		tableViewer.addSelectionChangedListener( e-> onRowSelected( e ));
		tableViewer.setComparator( new SearchViewerComparator());
		tableViewer.setContentProvider( new ModelProvider());
		tclayout = new TableColumnLayout();
		this.setLayout( tclayout );
	}

	/**
	 * Initialise the component
	 */
	public void initComposite(){
		this.createColumns( this, tableViewer);		
		this.initTableColumnLayout(tclayout);
	}
	
	protected void addColumnLabelProvider( ColumnLabelProvider labelProvider ){
		this.columnLabelProviders.add( labelProvider );
	}

	protected void removeColumnLabelProvider( ColumnLabelProvider labelProvider ){
		this.columnLabelProviders.remove( labelProvider );
	}

	protected void setViewerComparator( ViewerComparator comparator ) {
		tableViewer.setComparator(comparator);		
	}
	
	/**
	 * Get the column label provider for the column at the given index
	 * @param index
	 * @return
	 */
	public ColumnLabelProvider getColumnProvider( int index ) {
		return this.columnLabelProviders.get(index );
	}
	
	@Override
	public void dispose() {
		this.columnLabelProviders.clear();
		super.dispose();
	}

	public void refresh(){
		if( this.tableViewer.getTable().isDisposed() )
			return;
		this.tableViewer.refresh();
	}

	public void refresh( Object item, boolean choice ){
		if( this.tableViewer.getTable().isDisposed() )
			return;
		this.tableViewer.refresh( item, choice );
	}

	/**
	 * @return the title
	 */
	@Override
	public final String getTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public final void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * initialise the table column layout
	 * @param tclayout
	 */
	protected abstract void initTableColumnLayout( TableColumnLayout tclayout );
	
	/**
	 * @return the tableViewer
	 */
	protected final TableViewer getTableViewer()
	{
		return tableViewer;
	}

	@SuppressWarnings("unchecked")
	protected IModelLeaf<D>[] getSelected(){
		IStructuredSelection selection = (IStructuredSelection) this.tableViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		Collection<IModelLeaf<D>> results=  new ArrayList<>();
		while( iterator.hasNext() ) {
			results.add( (IModelLeaf<D>) iterator.next());
		}
		return results.toArray( new IModelLeaf[ results.size()]);
	}

	public void setSelection( int index ) {
		this.table.setSelection(index);
	}
	
	public int getSelectionCount() {
		return this.table.getSelectionCount();
	}
	
	protected TableColumnLayout getTableColumnLayout(){
		return this.tclayout;
	}

	/**
	 * Prepare the input prior to setting it
	 * @param leaf
	 */
	protected abstract void onSetInput( IModelLeaf<D> leaf );
	
	/**
	 * Set the input
	 * @param leaf
	 */
	@Override
	public void setInput( IModelLeaf<D> leaf ){
		if(( leaf == null ) || super.isDisposed() )
			return;
		if( StringUtils.isEmpty( this.title )){
			IDescriptor descriptor = leaf.getData().getDescriptor();
			if( !Descriptor.assertNull( descriptor.getProviderName()))
				this.title = descriptor.getProviderName() + ":";
			if( !Descriptor.assertNull( descriptor.getDescription() )){
				this.title += descriptor.getDescription();
			}
			else
				this.title = "{Title}";
		}
		this.input = leaf;
		this.onSetInput( leaf );
		if(leaf.isLeaf())
			return;
		this.tableViewer.setInput(leaf);
		this.table.setSelection(0);
	}

	/**
	 * Register a column to the table
	 * @param columnName
	 * @param className
	 * @param bound
	 * @return
	 */
	protected TableViewerColumn registerColum( String className, int style, int weight, int index ){
		final TableViewerColumn viewerColumn = new TableViewerColumn( tableViewer, style);
		final TableColumn column = viewerColumn.getColumn();
		column.setWidth(weight);
		column.setResizable(true);
		column.setMoveable(true);
		column.setData( RWT.CUSTOM_VARIANT, className );
		column.setData( S_INDEX, index );
		column.addSelectionListener(new SelectionAdapter() {    	
			private static final long serialVersionUID = 1L;
			@Override
			public void widgetSelected(SelectionEvent e){
				onHeaderClicked( e );
			}
		});
		viewerColumn.setLabelProvider( this.columnLabelProviders.get(index));
		return viewerColumn;
	}

	
	/**
	 * Response to clicking a header
	 * @param e
	 */
	protected abstract void onHeaderClicked( SelectionEvent e );
	
	protected abstract int compareTables( int columnIndex, IModelLeaf<D> o1, IModelLeaf<D> o2);

	// This will create the columns for the table
	protected abstract void createColumns(final Composite parent, final TableViewer viewer);	
	
	protected class SearchViewerComparator extends AbstractViewerComparator<IModelLeaf<D>>{
		private static final long serialVersionUID = 1L;

		public SearchViewerComparator() {
			super();
		}

		@Override
		protected int compareColumn(int columnIndex, IModelLeaf<D> o1, IModelLeaf<D> o2) {
			return compareTables( columnIndex, o1, o2 );
		}
	}
}
