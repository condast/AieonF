package org.aieonf.sketch.swt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.aieonf.commons.filter.WildcardFilter;
import org.aieonf.commons.filter.HierarchicalFilter.HierarchyRules;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.filter.HierarchicalModelAttributeFilter;
import org.aieonf.sketch.factory.SelectedFactory;
import org.aieonf.sketch.factory.SketchModelFactory;
import org.aieonf.sketch.preferences.SketchPreferences;
import org.condast.js.commons.controller.AbstractJavascriptController;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

public class SketchController extends AbstractJavascriptController {

	public static final String S_INITIALISTED_ID = "SketchInitialisedId";
	public static final String S_IS_INITIALISTED = "isInitialised";
	public static final String S_BAR_CLICKED = "barclicked";

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

	private SketchPreferences prefs = SketchPreferences.getInstance();
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );

	private SelectedFactory selected = SelectedFactory.getInstance();
	
	private BrowserFunction barfunction;

	public SketchController(Browser browser ) {
		super(browser, S_INITIALISTED_ID);
		barfunction = new BrowserFunction(browser, S_BAR_CLICKED){

			@Override
			public Object function(Object[] arguments) {
				prefs.setSearchType( arguments[0].toString());
				prefs.setWildcard( arguments[1].toString());
				prefs.setCleared(false);
				logger.info( arguments.toString() );
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
	
	/**
	 * Create a table from a wildcard search of names
	 * 
	 * @param response
	 * @throws Exception 
	 */
	public synchronized void search( String choice_str, String wildcard ) throws Exception
	{
		if( Descriptor.isNull( wildcard ))
			wildcard = WildcardFilter.S_ALL;
		AttributeFilter<IModelLeaf<IDescriptor>> categoryFilter = new AttributeFilter<IModelLeaf<IDescriptor>>( AttributeFilter.Rules.Wildcard, 
				CategoryAieon.Attributes.CATEGORY, wildcard );
		AttributeFilter<IModelLeaf<IDescriptor>> urlFilter = new AttributeFilter<IModelLeaf<IDescriptor>>( AttributeFilter.Rules.Wildcard, 
				IDescriptor.Attributes.DESCRIPTION, wildcard );
		HierarchicalModelAttributeFilter<IDescriptor> filter = new HierarchicalModelAttributeFilter<IDescriptor>( categoryFilter, urlFilter );
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
		SketchModelFactory factory = selected.getFactory();
		if( factory == null )
			return;
		factory.search(filter);
		SketchPreferences preferences = SketchPreferences.getInstance();
		preferences.setGetDate();
	}

}
