package org.aieonf.sketch.swt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.aieonf.sketch.factory.SelectedFactory;
import org.aieonf.sketch.factory.SketchModelFactory;
import org.condast.js.commons.controller.AbstractJavascriptController;
import org.eclipse.swt.browser.Browser;

public class SketchController extends AbstractJavascriptController {

	public static final String S_INITIALISTED_ID = "SketchInitialisedId";
	public static final String S_IS_INITIALISTED = "isInitialised";

	public enum Pages{
		BAR,
		BODY;

		@Override
		public String toString() {
			String str = name().toLowerCase(); 
			str = str.replaceAll("_", "-");
			str += ".html";
			return str;
		}

		public static String[] getItems(){
			String[] items = new String[ values().length ];
			for( int i=0; i< items.length; i++ ){
				items[i] = Pages.values()[i].name();
			}
			return items;
		}
	}

	private Logger logger = Logger.getLogger( this.getClass().getName() );

	private SelectedFactory selected = SelectedFactory.getInstance();

	public SketchController(Browser browser ) {
		super(browser, S_INITIALISTED_ID);
	}

	public SketchController(Browser browser, String id, String url) {
		super(browser, id, url);
	}

	public SketchController(Browser browser, String id, LoadTypes type, String url) {
		super(browser, id, type, url);
	}

	public SketchController(Browser browser, String id, InputStream in) {
		super(browser, id, in);
	}

	public void setBrowser( Pages page ) throws FileNotFoundException {
		SketchModelFactory smf = selected.getFactory();
		if( smf == null )
			return;
		try {
			String location = smf.getFilePath( page.toString() );
			super.setBrowser( new FileInputStream( location ));
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onLoadCompleted() {
		logger.info("COMPLETED");
	}
}
