package org.aieonf.commons.ui.richtext;

import org.eclipse.nebula.widgets.richtext.RichTextEditor;
import org.eclipse.nebula.widgets.richtext.RichTextEditorConfiguration;
import org.eclipse.swt.widgets.Composite;

public class AieonFRichText extends RichTextEditor {
	private static final long serialVersionUID = 1L;

	public AieonFRichText(Composite parent) {
		super(parent);
	}

	public AieonFRichText(Composite parent, int style) {
		super(parent, style);
	}

	public AieonFRichText(Composite parent, RichTextEditorConfiguration editorConfig) {
		super(parent, editorConfig);
	}

	public AieonFRichText(Composite parent, RichTextEditorConfiguration editorConfig, int style) {
		super(parent, editorConfig, style);
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		super.setText(text);
	}

	
}
