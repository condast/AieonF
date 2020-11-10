/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons.ui.celleditors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public abstract class AbstractCheckBoxCellEditor<T extends Object> extends AbstractControlCellEditor {
	private static final long serialVersionUID = 627098984741006686L;

	T value;

	/**
	 * Default CheckboxCellEditor style
	 */
	private static final int defaultStyle = SWT.NONE;

	/**
	 * Creates a new checkbox cell editor with no control
	 */
	public AbstractCheckBoxCellEditor()
	{
		setStyle(defaultStyle);
	}

	/**
	 * Creates a new checkbox cell editor parented under the given control. The cell editor value is a boolean value, which is initially <code>false</code>.
	 * Initially, the cell editor has no cell validator.
	 * 
	 * @param parent the parent control
	 */
	public AbstractCheckBoxCellEditor(Composite parent)
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
	protected AbstractCheckBoxCellEditor(Composite parent, int style)
	{
		super(parent, style);
	}
	
	protected T getData() {
		return value;
	}

	public void setData(T value) {
		this.value = value;
	}

	@Override
	public Control createControl(final Composite parent)
	{
		Canvas canvas = new Canvas(parent, SWT.NO_BACKGROUND); // transparent
		canvas.addMouseListener(new MouseAdapter(){
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseUp(MouseEvent e)
			{
				//toggle();
			}
		});
		return canvas;
	}

	/**
	 * The object is being passed in, return the index to be used in the editor.
	 * 
	 * It should return sNoSelection if the value can't be converted to a index. The errormsg will have already been set in this case.
	 */

	@Override
	public void activate()
	{
		toggle();
		fireApplyEditorValue();
		//super.activate();
	}

	protected abstract void onToggle();
	
	protected void toggle()
	{
		this.onToggle();
		markDirty();
	}


	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	@Override
	protected void doSetFocus()
	{
		// Ignore
	}
}
