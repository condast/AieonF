package org.aieonf.sketch.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.aieonf.commons.Utils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.sketch.Activator;
import org.aieonf.template.builder.TemplateInterpreter;
import org.aieonf.template.context.AbstractProviderContextFactory;

public class SketchFactory extends AbstractProviderContextFactory<IContextAieon, IModelLeaf<IDescriptor>> {

	private List<SketchModelFactory> modelFactories;
	
	private int selection;

	private static SketchFactory factory = new SketchFactory();

	private SketchFactory() {
		super( Activator.BUNDLE_ID, new TemplateInterpreter( SketchFactory.class ));
		modelFactories = new ArrayList<SketchModelFactory>();
		this.selection = 0;
	}
	
	public static SketchFactory getInstance() {
		return factory;
	}

	public void addSketchModelFactory( SketchModelFactory selected ) {
		this.modelFactories.add(selected);
	}

	public void removeSketchModelFactory( SketchModelFactory selected ) {
		this.modelFactories.remove(selected);
	}

	public boolean isEmpty() {
		return Utils.assertNull(modelFactories);
	}
	
	public SketchModelFactory getSelected() {
		return this.modelFactories.get(selection);
	}

	public SketchModelFactory setSelection( IDomainAieon domain ) {
		for( SketchModelFactory factory: this.modelFactories ) {
			if( factory.getDomain().getDomain().equals(domain.getDomain())) {
				this.selection = this.modelFactories.indexOf(factory);
				return factory;
			}
		}
		return null;
	}

	public void setSelection(int selection) {
		this.selection = selection;
	}
	
	public File getSelectedRoot() {
		return this.modelFactories.get(this.selection).getRoot();
	}

	public SketchModelFactory[] getSketchFactories() {
		return this.modelFactories.toArray( new SketchModelFactory[ this.modelFactories.size()]);
	}
}
