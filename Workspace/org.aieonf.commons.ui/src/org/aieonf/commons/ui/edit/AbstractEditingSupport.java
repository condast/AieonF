package org.aieonf.commons.ui.edit;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

public abstract class AbstractEditingSupport<T,U extends Object> extends EditingSupport {
	private static final long serialVersionUID = 7406850455892040238L;

	private final ColumnViewer viewer;
	private final CellEditor editor;

	protected AbstractEditingSupport( ColumnViewer viewer, CellEditor editor) {
		super(viewer);
		this.viewer = viewer;
		this.editor = editor;	
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	/**
	 * The objects may vary, depending on the column
	 * @param element
	 * @return
	 */
	protected abstract U onGetValue( T element );

	@SuppressWarnings("unchecked")
	@Override
	protected Object getValue(Object element) {
		try{
			return this.onGetValue(( T )element);
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * The objects may vary, depending on the column
	 * @param element
	 * @return
	 */
	protected abstract void onSetValue( T element, U userInputValue );

	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(Object element, Object userInputValue) {
		try{
			onSetValue( (T)element, (U)userInputValue);
			viewer.update(element, null);
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
}
