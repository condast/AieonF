package org.aieonf.commons.ui.table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.ui.celleditors.AbstractCheckBoxCellEditor;
import org.aieonf.commons.ui.edit.CheckBoxEditingSupport;
import org.aieonf.commons.ui.images.IInformationImages.Information;
import org.aieonf.commons.ui.table.ITableEventListener.TableEvents;
import org.aieonf.commons.ui.images.InformationImages;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.viewers.TableViewerColumn;

public abstract class AbstractSimpleTableComposite<C extends Object> extends AbstractTableComposite<IDescriptor, C>
{
	private static final long serialVersionUID = 976428552549736382L;

	public static final String S_EDIT_LOCATION = "/saight/edit";

	public static final String S_ERROR_ICON = "/icons/error_button_16.png";
	public static final String S_MODIFIED_ICON = "/icons/orangeball.jpg";
	public static final String S_BLANK_ICON = "/icons/blank.png";

	public enum Columns{
		DESCRIPTION,
		EDIT,
		DELETE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.name());
		}
	}
		
	private TableViewerColumn deleteColumn;
	
	private Map<IModelLeaf<IDescriptor>, Boolean> selected;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractSimpleTableComposite( Composite parent, int style)
	{
		super( parent, style);
		selected = new HashMap<>();
	}

	protected TableViewerColumn getDeleteColumn() {
		return deleteColumn;
	}

	protected void setDeleteColumn(TableViewerColumn deleteColumn, boolean skipReadOnly ) {
		this.deleteColumn = deleteColumn;
		this.deleteColumn.setEditingSupport( new CheckBoxEditingSupport<IModelLeaf<IDescriptor>>( super.getTableViewer(), new DeleteCheckBoxEditor( skipReadOnly) ));
		this.deleteColumn.getColumn().addListener(SWT.Selection, e->{				
			onNotifyDeleteButton( new TableEvent<IModelLeaf<IDescriptor>, C>( e.widget, TableEvents.DELETE, getInput() ));
		});
	}

	protected boolean isInList( IModelLeaf<IDescriptor> element ) {
		boolean result = false;
		try {
			if(( selected == null ) || ( element == null ))
				return result;
			return selected.get(element);
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.condast.eclipse.swt.composite.AbstractTableComposite#prepareInput(org.condast.concept.model.IModelLeaf)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onSetInput(IModelLeaf<IDescriptor> leaf)
	{
		try {
			setDeleteImage();	
			if(leaf.isLeaf())
				return;
			IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) leaf;
			selected.clear();
			for( IModelLeaf<? extends IDescriptor> child: model.getChildren().keySet())
				selected.put((IModelLeaf<IDescriptor>) child,  false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean setDeleteImage() {
		IModelLeaf<IDescriptor> leaf=  getInput();
		boolean enable = false;
		InformationImages images = InformationImages.getInstance();		
		if( !leaf.isReadOnly()) {
			enable = ( leaf != null) && (leaf.isLeaf() || !Utils.assertNull( getRemoveChildren()));
		}
		Image image = images.getImage( Information.DELETE, enable );
		this.deleteColumn.getColumn().setImage(image);
		return enable;
	}
	
	@SuppressWarnings("unchecked")
	protected long[] getRemoveChildren() {
	    Collection<?> collect = selected.entrySet().stream()
	        .filter(x -> x.getValue())
	        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue())).keySet();
	    long[] results = new long[ collect.size()];
	    int index =0;
	    for( Object obj: collect) {
	    	IModelLeaf<IDescriptor> leaf = (IModelLeaf<IDescriptor>) obj;
	    	results[index++] = leaf.getID();
	    }
	    return results;
	}
		
	protected void onNotifyDeleteButton( TableEvent<IModelLeaf<IDescriptor>, C> event ) {
		if( Utils.assertNull(this.selected)) {
			notifyTableEvent(new TableEvent<IModelLeaf<IDescriptor>, C>( this, TableEvents.DELETE, getInput()));
			return;
		}
		Iterator<Map.Entry<IModelLeaf<IDescriptor>, Boolean>> iterator = this.selected.entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<IModelLeaf<IDescriptor>, Boolean> entry = iterator.next();
			if( entry.getValue())
				notifyTableEvent(new TableEvent<IModelLeaf<IDescriptor>, C>( this, TableEvents.DELETE, entry.getKey()));
		}
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	protected class DeleteCheckBoxEditor extends AbstractCheckBoxCellEditor<IModelLeaf<IDescriptor>>{
		private static final long serialVersionUID = 1L;

		private boolean skipReadonly;
		
		public DeleteCheckBoxEditor(boolean skipReadonly) {
			super();
			this.skipReadonly = skipReadonly;
		}

		@Override
		protected void onToggle() {
			if( skipReadonly)
				return;
			IModelLeaf<IDescriptor> leaf = super.getData();
			if( leaf == null )
				return;
			Boolean value = selected.get( super.getData());
			selected.put(leaf, (value==null)?false:!value );
		}

		@Override
		protected Object doGetValue() {
			IModelLeaf<IDescriptor> leaf = super.getData();
			return ((leaf == null ) || skipReadonly )?false: selected.get( leaf );
		}

		@Override
		protected void doSetValue( Object value ) {
			if( skipReadonly)
				return;
			IModelLeaf<IDescriptor> leaf = super.getData();
			if( leaf == null )
				return;
			selected.put(leaf, (Boolean)value);
			setDeleteImage();	
		}
	}
}
