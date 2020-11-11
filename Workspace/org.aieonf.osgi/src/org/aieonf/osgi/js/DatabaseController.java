package org.aieonf.osgi.js;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.commons.filter.WildcardFilter;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.filter.HierarchicalModelAttributeFilter;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDatabase;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.google.gson.Gson;

public class DatabaseController<M extends IDescribable> extends AbstractJavascriptController {

	public static final String S_INITIALISTED_ID = "DatabaseInitialisedId";
	public static final String S_IS_INITIALISTED = "isInitialised";
	
	public static final String S_DATABASE_QUERY = "dbquery";

	public enum Pages{
		GET,
		SEARCH,
		ADD,
		REMOVE,
		UPDATE;

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

	private IModelDatabase<IDescriptor, M> database;
	private Class<M> clss;

	public DatabaseController(Browser browser ) {
		super(browser, S_INITIALISTED_ID);
		new BrowserFunction(browser, S_DATABASE_QUERY){

			@SuppressWarnings("unchecked")
			@Override
			public Object function(Object[] args) {
				Pages page = Pages.valueOf((String) args[1]);
				Gson gson = new Gson();
				M leaf = null;
				try {
					switch( page ){
					case GET:
						IDescriptor descriptor = gson.fromJson( (String) args[1], IDescriptor.class );
						database.get(descriptor);
						break;
					case SEARCH:
						IModelFilter<M> filter = gson.fromJson( (String) args[1], IModelFilter.class );
						database.search(filter);
						break;
					case ADD:
						leaf = gson.fromJson( (String) args[1], clss );
						database.add(leaf);
						break;
					case REMOVE:
						leaf = gson.fromJson( (String) args[1], clss );
						database.remove(leaf);
						break;
					case UPDATE:
						leaf = gson.fromJson( (String) args[1], clss );
						database.update(leaf);
						break;

					default:
						break;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				logger.info( args.toString() );
				return super.function(args);
			}
		};	
	}

	private DatabaseController(Class<M> clss, IModelDatabase<IDescriptor, M> database, Browser browser, String id, String url) {
		super(browser, id, url);
		this.database = database;
		this.clss = clss;
	}

	private DatabaseController(Class<M> clss, IModelDatabase<IDescriptor, M> database, Browser browser, String id, LoadTypes type, String url) {
		super(browser, id, type, url);
		this.database = database;
		this.clss = clss;
	}

	private DatabaseController(Class<M> clss, IModelDatabase<IDescriptor, M> database, Browser browser, String id, InputStream in) {
		super(browser, id, in);
		this.database = database;
		this.clss = clss;
	}

	public void setBrowser( String location ) throws FileNotFoundException {
		try {
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
	@SuppressWarnings("unused")
	public synchronized void search( String choice_str, String wildcard ) throws Exception
	{
		if( Descriptor.assertNull( wildcard ))
			wildcard = WildcardFilter.S_ALL;
		AttributeFilter<IModelLeaf<IDescriptor>> categoryFilter = new AttributeFilter<IModelLeaf<IDescriptor>>( AttributeFilter.Rules.WILDCARD, 
				CategoryAieon.Attributes.CATEGORY, wildcard );
		AttributeFilter<IModelLeaf<IDescriptor>> urlFilter = new AttributeFilter<IModelLeaf<IDescriptor>>( AttributeFilter.Rules.WILDCARD, 
				IDescriptor.Attributes.DESCRIPTION.name(), wildcard );
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
		SketchModelFactory factory = selected.getFactory();
		if( factory == null )
			return;
		factory.getFunction( IModelProvider.S_MODEL_PROVIDER_ID).search(filter);
		SketchPreferences preferences = SketchPreferences.getInstance();
		preferences.setGetDate();
		*/
	}

}
