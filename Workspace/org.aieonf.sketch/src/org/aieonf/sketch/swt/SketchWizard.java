package org.aieonf.sketch.swt;


import org.aieonf.commons.db.IDatabaseConnection;
import org.aieonf.commons.ui.wizard.ButtonEvent;
import org.aieonf.commons.ui.wizard.IButtonWizardContainer;
import org.aieonf.commons.ui.wizard.xml.AbstractXmlFlowWizard;
import org.aieonf.concept.request.IKeyEventListener;
import org.aieonf.concept.request.KeyEvent;
import org.aieonf.sketch.controller.BarController;
import org.aieonf.sketch.controller.SketchController.Pages;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;

public class SketchWizard extends AbstractXmlFlowWizard<KeyEvent<IDatabaseConnection.Requests>> {
	
	private static final String S_WIZARD_DESC = "/AIEONF-INF/wizard.xml";

	//Response to the 'find' button, which activates a search through the designate controller
	
	private SketchWizard wizard; 
	
	private BarController service = BarController.getInstance();	
	private IKeyEventListener<IDatabaseConnection.Requests> listener = new IKeyEventListener<IDatabaseConnection.Requests>(){

		@Override
		public void notifyKeyEventReceived(final KeyEvent<IDatabaseConnection.Requests> event)
		{
			//setData(event.getData());
			switch( event.getRequest() ){
			case PREPARE:
				int index = getFlowControl().getIndex();
				Pages page = Pages.values()[ index ];
				switch( page ){
				case HOME:
					injectButtonEvent( new ButtonEvent<KeyEvent<IDatabaseConnection.Requests> >( wizard,  IButtonWizardContainer.Buttons.NEXT ));
					break;
				case EDIT:
					injectButtonEvent( new ButtonEvent<KeyEvent<IDatabaseConnection.Requests> >( wizard,  IButtonWizardContainer.Buttons.CANCEL ));
					break;
				default:
					break;
				}
				setData(event);
				break;
			default:
				break;
			}
		}	
	};

	public SketchWizard( Composite parent, int style ) {
		super( SketchWizard.class.getResourceAsStream( S_WIZARD_DESC ), SketchWizard.class, null );
		//controller = new SketchController( new Browser( parent, style ));
		wizard = this;
		service.addListener(listener);
	}

	
	@Override
	protected IWizardPage onAddPage( String pageName, String description, String message) {
		IWizardPage wizard = 
		new FlowWizardPage( pageName, description, message ){
			private static final long serialVersionUID = 1L;

			
			@Override
			protected void onControlCreated(int index, Composite composite) {
				Pages page = Pages.values()[ index ];
				/**
				try {
					controller.setBrowser( page );
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				*/
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
			}	
		};
		return wizard;
	}
}
