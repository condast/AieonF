package org.aieonf.commons.ui.swt;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescribable;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class DescribablePropertyViewer extends Composite{
	private static final long serialVersionUID = 1L;

	private enum Columns{
		KEY,
		VALUE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );//NaLanguage.getInstance().getString( this );
		}

		public static int getWeight( Columns column ){
			switch( column ){
			default:
				return 100;
			}
		}
	}

	private TableViewer viewer;
	private TableColumnLayout tableLayout;
	private IDescribable input;
	
	public DescribablePropertyViewer(Composite parent,int style ) {
		super(parent,style );
		this.createComposite(parent, style);
	}

	protected void createComposite( Composite parent,int style ){
		tableLayout = new TableColumnLayout();
		setLayout( tableLayout );

		viewer = new TableViewer( this, SWT.BORDER|SWT.MULTI|SWT.FULL_SELECTION );
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
					//selectedColumn = cell.getColumnIndex();
				} catch( Exception e ) {
					e.printStackTrace();
				}
		    }
		});
		viewer.setUseHashlookup( true );
		//comparator = new DeleteViewerComparator();
		//viewer.setComparator( comparator );
		viewer.setContentProvider( ArrayContentProvider.getInstance() );
	    viewer.addSelectionChangedListener( new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				try {
					IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
					Iterator<?> iterator = selection.iterator();
					while( iterator.hasNext() ){
						iterator.next();
						//onRowDoubleClick( swd.data );
					}
				} catch( Exception e ) {
					e.printStackTrace();
				}				
			}
	    });
	    
		viewer.addDoubleClickListener( new IDoubleClickListener() {
			
			@Override
			public void doubleClick( DoubleClickEvent event ) {				
				try {
					IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
					Iterator<?> iterator = selection.iterator();
					while( iterator.hasNext() ){
						iterator.next();
						//onRowDoubleClick( swd.data );
					}
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
		});
		for( Columns column: Columns.values() ){
			createColumn( column, SWT.NONE );
		}
		viewer.setLabelProvider( new DescribableLabelProvider() );
	}

	public IDescribable getInput() {
		return input;
	}

	public void setInput( IDescribable input ){
		this.input = input;
		viewer.setInput(input.entrySet());
	}
	
	protected void onRowDoubleClick(IDescribable selection) {
		try{
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
	
	private TableViewerColumn createColumn( final Columns column, int alignmentContent ) {
		TableViewerColumn result = new TableViewerColumn( viewer, alignmentContent );
		TableColumn tcolumn = result.getColumn();
		if( !StringUtils.isEmpty( column.toString() ) )
			tcolumn.setText( column.toString() );
		tcolumn.setMoveable( true );
		//tcolumn.addSelectionListener( getSelectionAdapter( tcolumn, column.ordinal() ) );
		tableLayout.setColumnData( tcolumn, new ColumnWeightData( Columns.getWeight(column)));
		return result;
	}
	
	private class DescribableLabelProvider extends LabelProvider implements ITableLabelProvider{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText( Object element, int columnIndex ) {
			String retval = null;
			Columns column = Columns.values()[ columnIndex ];
			Map.Entry<String, String> entry = (Entry<String, String>) element;
			switch( column){
			case KEY:
				retval = entry.getKey();
				break;
			case VALUE:
				retval = entry.getValue();
				break;
			default:
				break;				
			}
			return retval;
		}

		@Override
		public Image getColumnImage(Object arg0, int columnIndex) {
			Image image = null;
			Columns column = Columns.values()[ columnIndex ];
			switch( column){
			case KEY:
				break;
			case VALUE:
				break;
			default:
				break;				
			}
			return image;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}
	}
}