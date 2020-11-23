package org.aieonf.commons.ui.table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.ui.celleditors.AbstractCheckBoxCellEditor;
import org.aieonf.commons.ui.edit.CheckBoxEditingSupport;
import org.aieonf.commons.ui.images.IInformationImages.Information;
import org.aieonf.commons.ui.images.InformationImages;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewerColumn;

public abstract class AbstractSimpleTableComposite<D extends IDescriptor, C extends Object> extends AbstractTableComposite<D, C>
{
	private static final long serialVersionUID = 976428552549736382L;

	public static final String S_EDIT_LOCATION = "/Saight/edit";

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
	
	private Map<IModelLeaf<? extends IDescriptor>, Boolean> list;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractSimpleTableComposite( Composite parent, int style)
	{
		super( parent, style);
		list = new HashMap<>();
		//setDeleteImage();	
	}

	@Override
	protected void initTableColumnLayout(TableColumnLayout tclayout)
	{
		Table table = super.getTableViewer().getTable();
		tclayout.setColumnData( table.getColumn(0), new ColumnWeightData( 88 ) );
		tclayout.setColumnData( table.getColumn(1), new ColumnWeightData( 7 ) );
		tclayout.setColumnData( table.getColumn(2), new ColumnWeightData( 5 ) );
	}

	protected TableViewerColumn getDeleteColumn() {
		return deleteColumn;
	}

	protected void setDeleteColumn(TableViewerColumn deleteColumn) {
		this.deleteColumn = deleteColumn;
		this.deleteColumn.setEditingSupport( new CheckBoxEditingSupport<IModelLeaf<IDescriptor>>( super.getTableViewer(), new DeleteCheckBoxEditor() ));
	}

	protected boolean isInList( IModelLeaf<IDescriptor> element ) {
		return list.get(element);
	}

	protected abstract void onPrepareInput( IModelLeaf<D> leaf );
	
	/* (non-Javadoc)
	 * @see org.condast.eclipse.swt.composite.AbstractTableComposite#prepareInput(org.condast.concept.model.IModelLeaf)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void prepareInput(IModelLeaf<D> leaf)
	{
		try {
			onPrepareInput(leaf);
			setDeleteImage();	
			if(leaf.isLeaf())
				return;
			IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) leaf;
			list.clear();
			for( IModelLeaf<? extends IDescriptor> child: model.getChildren().keySet())
				list.put(child,  false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean setDeleteImage() {
		IModelLeaf<D> leaf=  getInput();
		InformationImages images = InformationImages.getInstance();		
		boolean enable = ( leaf != null) && (leaf.isLeaf() || !Utils.assertNull( getRemoveChildren()));
		Image image = images.getImage( Information.DELETE, enable );
		this.deleteColumn.getColumn().setImage(image);
		return enable;
	}
	
	@SuppressWarnings("unchecked")
	protected long[] getRemoveChildren() {
	    Collection<?> collect = list.entrySet().stream()
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
		
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	protected class DeleteCheckBoxEditor extends AbstractCheckBoxCellEditor<IModelLeaf<IDescriptor>>{
		private static final long serialVersionUID = 1L;

		@Override
		protected void onToggle() {
			IModelLeaf<IDescriptor> leaf = super.getData();
			if( leaf == null )
				return;
			Boolean value = list.get( super.getData());
			list.put(leaf, (value==null)?false:!value );
		}

		@Override
		protected Object doGetValue() {
			IModelLeaf<IDescriptor> leaf = super.getData();
			return (leaf == null )?false: list.get( leaf );
		}

		@Override
		protected void doSetValue( Object value ) {
			IModelLeaf<IDescriptor> leaf = super.getData();
			if( leaf == null )
				return;
			list.put(leaf, (Boolean)value);
			setDeleteImage();	
		}
	}
}
