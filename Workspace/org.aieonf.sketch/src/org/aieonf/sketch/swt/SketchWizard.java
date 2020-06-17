package org.aieonf.sketch.swt;

import java.io.FileNotFoundException;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.osgi.wizard.xml.AbstractXmlFlowWizard;
import org.aieonf.sketch.controller.SketchController;
import org.aieonf.sketch.controller.SketchController.Pages;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
public class SketchWizard extends AbstractXmlFlowWizard<IModelLeaf<IDescriptor>> {
	
	private static final String S_WIZARD_DESC = "/AIEONF-INF/wizard.xml";

	//Response to the 'find' button, which activates a search through the designate controller
	
	private IModelLeaf<?> model;
	private SketchWizard wizard; 
	
	private SketchController controller; 

	public SketchWizard( Composite parent, int style ) {
		super( SketchWizard.class.getResourceAsStream( S_WIZARD_DESC ), SketchWizard.class );
		controller = new SketchController( new Browser( parent, style ));
		wizard = this;
	}

	
	@Override
	protected IWizardPage onAddPage( String pageName, String description, String message) {
		IWizardPage wizard = 
		new FlowWizardPage( pageName, description, message ){
			private static final long serialVersionUID = 1L;

			
			@Override
			protected void onControlCreated(int index, Composite composite) {
				Pages page = Pages.values()[ index ];
				try {
					controller.setBrowser( page );
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				switch( page ){
				case SEARCH:
					break;
				case SHOW:
						//ecomp.addDataEventListener(dlistener);
					//ecomp.setInput( (IModelLeaf<IDescriptor>) model );
					break;
				case EDIT:
						//acomp.setInput( (IModelLeaf<IDescriptor>) model );
					break;

				default:
					break;
				}
			}

			@Override
			public void onCreateIcon(Composite iconbar) {
				// TODO Auto-generated method stub
				
			}	
		};
		return wizard;
	}
}
