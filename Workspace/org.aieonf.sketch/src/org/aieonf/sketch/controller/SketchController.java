package org.aieonf.sketch.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.aieonf.commons.db.IDatabaseConnection.Requests;
import org.aieonf.commons.filter.WildcardFilter;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.osgi.js.AbstractJavascriptController;
import org.aieonf.sketch.factory.SketchFactory;
import org.aieonf.sketch.factory.SketchModelFactory;
import org.aieonf.sketch.preferences.SketchPreferences;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

public class SketchController extends AbstractJavascriptController {

	public static final String S_INITIALISTED_ID = "SketchInitialisedId";
	public static final String S_IS_INITIALISTED = "isInitialised";
	
	public static final String S_BAR_CLICKED = "barClicked";

	public enum Pages{
		BAR,
		HOME,
		INTRO,
		SEARCH,
		SHOW,
		EDIT;

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

	private SketchPreferences prefs = SketchPreferences.getInstance();
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );

	private SketchFactory factory = SketchFactory.getInstance();

	private BarController controller = BarController.getInstance();

	public SketchController(Browser browser ) {
		super(browser, S_INITIALISTED_ID);
		new BrowserFunction(browser, S_BAR_CLICKED){

			/**
			 * TODO CP:We need to add the correct parameters  
			 */
			@Override
			public Object function(Object[] arguments) {
				controller.prepare();
				Map<String, String> parameters = new HashMap<>();
				try {
					//prefs.setSearchType( arguments[0].toString());
					//prefs.setWildcard( arguments[1].toString());
					prefs.setCleared(false);
					logger.info( arguments.toString() );
					controller.setEvent(Requests.SEARCH, parameters);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return super.function(arguments);
			}
		};	
	}

	private SketchController(Browser browser, String id, String url) {
		super(browser, id, url);
	}

	private SketchController(Browser browser, String id, LoadTypes type, String url) {
		super(browser, id, type, url);
	}

	private SketchController(Browser browser, String id, InputStream in) {
		super(browser, id, in);
	}

	public void setBrowser( Pages page ) throws FileNotFoundException, MalformedURLException {
		SketchModelFactory smf = factory.getFactory();
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
	
	/**
	 * Create a table from a wildcard search of names
	 * 
	 * @param response
	 * @throws Exception 
	 */
	public synchronized void search( String choice_str, String wildcard ) throws Exception
	{
		if( Descriptor.assertNull( wildcard ))
			wildcard = WildcardFilter.S_ALL;
		//AttributeFilter<IModelLeaf<IDescriptor>> categoryFilter = new AttributeFilter<IModelLeaf<IDescriptor>>( AttributeFilter.Rules.WILDCARD, 
		//		CategoryAieon.Attributes.CATEGORY, wildcard );
		//AttributeFilter<IModelLeaf<IDescriptor>> urlFilter = new AttributeFilter<IModelLeaf<IDescriptor>>( AttributeFilter.Rules.WILDCARD, 
		//		IDescriptor.Attributes.DESCRIPTION, wildcard );
		//HierarchicalModelAttributeFilter<IDescriptor> filter = new HierarchicalModelAttributeFilter<IDescriptor>( categoryFilter, urlFilter );
		/*
		switch( choice ){
		case NAME:
			filter.setHierarchyRule( HierarchyRules.ALLPARENTS );
			break;
		default:
			filter.setHierarchyRule( HierarchyRules.ALLCHILDREN );
			break;
		}
		*/
		//SketchModelFactory modelFactory = factory.getFactory();
		//if( factory == null )
		//	return;
		//factory.getFunction( IModelProvider.S_MODEL_PROVIDER_ID).search(filter);
		SketchPreferences preferences = SketchPreferences.getInstance();
		preferences.setGetDate();
	}
}
