package org.aieonf.commons.ui.table;

import java.util.Comparator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

public abstract class AbstractViewerComparator<T extends Object> extends ViewerComparator{

	private static final long serialVersionUID = 1L;
	private int selectedColumn;
	private int direction = SWT.DOWN;

	protected AbstractViewerComparator() {
		this( SWT.DOWN );
	}

	protected AbstractViewerComparator( int direction) {
		this.direction = direction;
		this.selectedColumn = 0;
	}

	public int getDirection(){
		return direction;
	}

	public void setColumn( int index) {
		if (index == this.selectedColumn ) {
			// Same column as last sort; toggle the direction
			direction = ( direction == SWT.UP )? SWT.DOWN: SWT.UP;
		} else {
			// New column; do an ascending sort
			direction = SWT.DOWN;
			this.selectedColumn = index;
		}
	}

	/**
	 * Create a comparator for this viewer
	 * @return
	 */
	protected abstract int compareColumn( int columnIndex, T o1, T o2 );

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Viewer vwer, Object e1, Object e2) {
		Comparator<T> comp = new Comparator<T>(){

			@Override
			public int compare(T o1, T o2) {
				return compareColumn( selectedColumn, o1, o2 );
			}
			
		};
		int rc = ( comp == null )? super.compare(vwer, e1, e2 ): comp.compare(( T) e1, (T)e2 );
		return (direction == SWT.DOWN)? rc: -rc ;
	}
}
