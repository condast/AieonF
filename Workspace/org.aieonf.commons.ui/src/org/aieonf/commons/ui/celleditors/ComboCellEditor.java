/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons.ui.celleditors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ComboCellEditor extends AbstractControlCellEditor {
	private static final long serialVersionUID = 3458589863657408886L;

	/**
	 * The values.
	 */
	private Combo combo;
	
	/**
	 * Default CheckboxCellEditor style
	 */
	protected static final int defaultStyle = SWT.NONE;

	/**
	 * Creates a new checkbox cell editor with no control
	 */
	public ComboCellEditor()
	{
		setStyle(defaultStyle);
	}

	/**
	 * Creates a new checkbox cell editor parented under the given control. The cell editor value is a boolean value, which is initially <code>false</code>.
	 * Initially, the cell editor has no cell validator.
	 * 
	 * @param parent the parent control
	 */
	public ComboCellEditor(Composite parent)
	{
		this(parent, defaultStyle);
	}

	/**
	 * Creates a new checkbox cell editor parented under the given control. The cell editor value is a boolean value, which is initially <code>false</code>.
	 * Initially, the cell editor has no cell validator.
	 * 
	 * @param parent the parent control
	 * @param style the style bits
	 */
	public ComboCellEditor(Composite parent, int style)
	{
		super(parent, style);
	}
	
	public void setItems( String[] items ){
		this.combo.setItems(items);
	}

	protected Combo getCombo() {
		return combo;
	}

	@Override
	public Control createControl(final Composite parent)
	{
		this.combo = new Combo( parent, SWT.NONE );
		this.combo.setEnabled( super.isEnabled());
		combo.addSelectionListener( new SelectionAdapter(){
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo sp = ( Combo )e.getSource();
				doSetValue( sp.getSelectionIndex() );
				super.widgetSelected(e);
			}
		});
		return combo;
	}

	/**
	 * The object is being passed in, return the index to be used in the editor.
	 * 
	 * It should return sNoSelection if the value can't be converted to a index. The errormsg will have already been set in this case.
	 */

	@Override
	public void activate()
	{
	}

	/**
	 * The <code>CheckboxCellEditor</code> implementation of this <code>CellEditor</code> framework method returns the checkbox setting wrapped as a
	 * <code>Boolean</code>.
	 * 
	 * @return the Boolean checkbox value
	 */
	@Override
	protected Object doGetValue()
	{
		if( this.combo == null )
			return -1;
		return this.combo.getSelectionIndex();
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	@Override
	protected void doSetFocus()
	{
		this.combo.setFocus();
	}

	/**
	 * The <code>CheckboxCellEditor</code> implementation of this <code>CellEditor</code> framework method accepts a value wrapped as a
	 * <code>Boolean</code> .
	 * 
	 * @param val a Boolean value
	 */
	@Override
	protected void doSetValue(Object val)
	{
		if(( this.combo == null ) || ( val == null))
			return;
		if( val instanceof Integer )
			this.combo.select((int)val );
		if( val instanceof Enum )
			this.combo.select( ((Enum<?>) val).ordinal() );
	}
}
