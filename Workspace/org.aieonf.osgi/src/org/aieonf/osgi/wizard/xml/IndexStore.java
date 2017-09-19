package org.aieonf.osgi.wizard.xml;

import java.util.HashMap;
import java.util.Map;

import org.aieonf.osgi.wizard.IButtonWizardContainer;
import org.aieonf.osgi.wizard.IHeadlessWizardContainer.ContainerTypes;

/**
 * Used for temporary storage
 * @author Kees
 *
 */
public class IndexStore{
	
	String pageName;
	String description;
	String message;
	ContainerTypes type;
	
	String composite_clss;
	int composite_style;
	String url;
	String source;
	
	boolean barOnTop;
	String titleStyle;
	
	String buttonStyle;
	Map<IButtonWizardContainer.Buttons, ButtonInfo> buttonInfo;
	boolean onCancel;
	boolean onFinish;
	
	int index;

	IndexStore( String pageName, String description, String message, ContainerTypes type, int index ) {
		super();
		this.pageName = pageName;
		this.index = index;
		this.description = description;
		this.message = message;
		this.type = type;
		this.buttonInfo = new HashMap<IButtonWizardContainer.Buttons, ButtonInfo>();
		this.onCancel = ( index == 0 );
		this.onFinish = ( index == 0 );
	}

	int getIndex() {
		return index;
	}
	
	public String getURL(){
		return this.url;
	}

	public String getSource(){
		return this.source;
	}

	void addButtonInfo( IButtonWizardContainer.Buttons button, boolean visible, boolean enabled ){
		ButtonInfo bi = new ButtonInfo();
		bi.enabled = enabled;
		bi.visible = visible;
		buttonInfo.put( button, bi );
	}
	
	boolean isButtonVisible( IButtonWizardContainer.Buttons button ){
		ButtonInfo bi = buttonInfo.get( button );
		return ( bi == null )?false: bi.visible;
	}

	boolean isButtonEnabled( IButtonWizardContainer.Buttons button ){
		ButtonInfo bi = buttonInfo.get( button );
		return ( bi == null )?false: bi.enabled;
	}

	String getButtonPushed( IButtonWizardContainer.Buttons button ){
		ButtonInfo bi = buttonInfo.get( button );
		return ( bi == null )?null: bi.push;
	}

	IButtonWizardContainer.Buttons[] getButtons(){
		return buttonInfo.keySet().toArray( new IButtonWizardContainer.Buttons[ this.buttonInfo.size() ]);
	}

	/**
	 * Add the id of the push id
	 * @param text
	 * @param enabled
	 */
	void addPushPage( IButtonWizardContainer.Buttons button, String push ){
		buttonInfo.get( button ).push = push;
	}

	private static class ButtonInfo{
		
		boolean enabled;
		boolean visible;
		
		//The id of the page that the wizard will jump to after
		//pushing this button
		String push;
	}
}
