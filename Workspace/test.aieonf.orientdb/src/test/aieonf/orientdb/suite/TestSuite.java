package test.aieonf.orientdb.suite;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.aieonf.commons.db.IDatabaseConnection;
import org.aieonf.commons.db.IDatabaseConnection.Requests;
import org.aieonf.commons.http.ResponseEvent;
import org.aieonf.commons.security.AbstractSecureProvider;
import org.aieonf.commons.security.ISecureGenerator;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.concept.library.URLAieon;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.filter.ModelFilter;
import org.aieonf.model.rest.AbstractRestDatabase;
import org.aieonf.model.serialise.ModelTypeAdapter;
import org.condast.commons.test.core.AbstractTestSuite;
import org.condast.commons.test.core.ITestEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import test.aieonf.orientdb.core.TestFactory;
import test.aieonf.orientdb.service.LoginDispatcher;

public class TestSuite extends AbstractTestSuite<String, String> {

	protected TestSuite() {
		super("Test2");
	}

	public enum Tests{
		TEST_OPEN_AND_CLOSE,
		TEST_SERIALSE,
		TEST_DESERIALSE,
		TEST_ADD_AND_READ,
		TEST_REGISTER,
		TEST_MODEL_BUILDER,
		TEST_REST_ADD,
		TEST_REST_ADD_MODEL;
	}
	private static TestSuite suite = new TestSuite();
	
	//private static IModelDatabase<IDomainAieon,IModelLeaf<IDescriptor>> database;
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	public static TestSuite getInstance(){
		return suite;
	}
	
	@Override
	protected void testSuite() throws Exception {
		Tests test = Tests.TEST_SERIALSE;
		try{
			switch( test ){
			case TEST_ADD_AND_READ:
				testAddAndGet();
				break;
			case TEST_SERIALSE:
				testSerialise();
				break;
			case TEST_DESERIALSE:
				testDeserialise();
				break;
			case TEST_REGISTER:
				testRegister();
				break;
			case TEST_MODEL_BUILDER:
				testModelBuilder();
				break;
			case TEST_REST_ADD:
				testRESTAdd();
				break;
			case TEST_REST_ADD_MODEL:
				testRESTAddModel();
				break;
			default:
				break;
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		logger.info("Tests completed");
		//System.exit(0);
	}

	@SuppressWarnings("unchecked")
	public void testModelBuilder() {
		TestFactory factory = TestFactory.getInstance();
		factory.createTemplate();
		IModelNode<IDescriptor> model = createModel();
		
		Gson gson = new Gson();
		String result = gson.toJson(model.getDescriptor());
		logger.info( result );
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(model.getClass(), new ModelTypeAdapter());
		gson = builder.create();
		result = gson.toJson(model, model.getClass());
		logger.info( result );
		IModelNode<IDescriptor> model2 = gson.fromJson(result, model.getClass());
		logger.info( model2.toString());
	}

	private final void testSerialise() throws Exception{
		TestFactory factory = TestFactory.getInstance();
		IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) factory.createModel();
		URLAieon urlAieon = new URLAieon();
		urlAieon.setURI( "http://www.condast.com" );
		urlAieon.setDescription( "description" );
		urlAieon.setScope( IConcept.Scope.PUBLIC );
		urlAieon.setVersion(1);
		IModelLeaf<URLAieon> urlModel = new ModelLeaf<URLAieon>( urlAieon );
		urlModel.setIdentifier("identifier");
		model.addChild( urlModel);

		
		IModelLeaf<IDescriptor>[] arr = new IModelLeaf[1];
		arr[0] = model;
		GsonBuilder builder = new GsonBuilder(); 
		builder.enableComplexMapKeySerialization();
		builder.registerTypeAdapter( IModelLeaf.class, new ModelTypeAdapter());
		Gson gson = builder.create();
		String data = gson.toJson(arr, IModelLeaf[].class);
		logger.info( "\n\n" + data + "\n\n" );
		IModelLeaf<?>[] results = gson.fromJson(data, IModelLeaf[].class);
	}

	private final void testDeserialise() throws Exception{
		String data = "[{\"children\":[[{},null],[{},null]]," +
				"\"descriptor\":{\"implicit\":{},\"attribute\":\"LogEntry\","+ 
				"\"descriptor\":{\"base\":{\"properties\":{\"Locale\":\"en\"," + 
				"\"ORG.AIEONF.CONCEPT.LIBRARY.LOCALEAIEON.ATTRIBUTES.COUNTRY\":\"GB\"," + 
				"\"DESCRIPTION\":\"{Description}\",\"SCOPE\":\"{Scope}\",\"ORG.AIEONF.CONCEPT.IDESCRIPTOR.ATTRIBUTES.CLASS\":\"org.aieonf.concept.implicit.ImplicitAieon\"," +
				"\"Title\":\"{Title}\",\"ORG.AIEONF.CONCEPT.IDESCRIPTOR.ATTRIBUTES.VERSION\":\"0\"," + 
				"\"ORG.AIEONF.CONCEPT.IDESCRIPTOR.ATTRIBUTES.NAME\":\"LogEntry\",\"class\":\"org.aieonf.concept.core.Concept\"," + 
				"\"version\":\"1\",\"NAME\":\"LogEntry\"}}}},\"leaf\":false,\"parent\":{}," + 
				"\"properties\":{\"CREATE_DATE\":\"2020-08-27:10-04-50\",\"VERSION\":\"0\","+ 
				"\"CLASS\":\"org.aieonf.template.core.TemplateNode\",\"ID\":\"-1\",\"IDENTIFIER\":\"org.aieonf.model\"," + 
				"\"DEPTH\":\"1\",\"class\":\"org.aieonf.concept.core.Descriptor\"}}]";
		GsonBuilder builder = new GsonBuilder(); 
		builder.enableComplexMapKeySerialization();
		builder.registerTypeAdapter( IModelLeaf.class, new ModelTypeAdapter());
		Gson gson = builder.create();
		logger.info( data );
		IModelLeaf<?> results = gson.fromJson(data, IModelLeaf.class);

	}

	private final void testRegister() throws Exception{
		LoginDispatcher dispatcher = LoginDispatcher.getInstance();
		/*
		LoginEvent le = new LoginEvent( this, LoginEvents.REGISTER, "test", "test_pwd" );
		dispatcher.setLoginEvent(le);
		le = new LoginEvent( this );
		dispatcher.setLoginEvent(le);
		le = new LoginEvent( this, LoginEvents.LOGIN, "test", "test_pwd" );
		dispatcher.setLoginEvent(le);
		le = new LoginEvent( this );
		dispatcher.setLoginEvent(le);
		*/
	}

	private final void testAddAndGet() throws Exception{
		IModelNode<IDescriptor> model = createModel();
		TestFactory factory = TestFactory.getInstance();
		RestDatabase database = new RestDatabase( new SecureGenerator(), factory.getDomain(), IDatabaseConnection.REST_URL );	
		try{		
			database.open( factory.getDomain());
			database.add(model);
			IModelFilter<IModelLeaf<IDescriptor>> filter = 
					new ModelFilter<IModelLeaf<IDescriptor>>( new AttributeFilter<IDescriptor>( AttributeFilter.Rules.WILDCARD, IDescriptor.Attributes.NAME.name(), "CATEGORY"));
			Collection<IModelLeaf<IDescriptor>> results = database.search(filter);
			for( IModelLeaf<? extends IDescriptor> leaf: results )
				logger.info( leaf.getDescriptor().toString() );
			if( results.isEmpty()){
				logger.info( "No Results. Stopping");
				return;
			}
			logger.info( "Results found: " + results.size() );
			for( IModelLeaf<? extends IDescriptor> leaf: results )
				logger.info("MODEL:\n" + leaf.toString());
			database.remove( results.iterator().next());
			results = database.search(filter);
			logger.info( "Results After Remove: " + results.size() );
			for( IModelLeaf<? extends IDescriptor> leaf: results )
				logger.info("MODEL:\n" + leaf.toString());
			
			database.add(model);
				
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally{
			database.close();
			database.deactivate();
		}
	}

	private final void testRESTAdd() throws Exception{
		IModelNode<IDescriptor> model = createModel();
		TestFactory factory = TestFactory.getInstance();
		RestDatabase database = new RestDatabase( new SecureGenerator(), factory.getDomain(), IDatabaseConnection.REST_URL );	
		try{		
			database.open( factory.getDomain());
			database.add(model);
			IModelFilter<IModelLeaf<IDescriptor>> filter = 
					new ModelFilter<IModelLeaf<IDescriptor>>( new AttributeFilter<IDescriptor>( AttributeFilter.Rules.WILDCARD, IDescriptor.Attributes.NAME, "CATEGORY"));
			Collection<IModelLeaf<IDescriptor>> results = database.search(filter);
			for( IModelLeaf<? extends IDescriptor> leaf: results )
				logger.info( leaf.getDescriptor().toString() );
			if( results.isEmpty()){
				logger.info( "No Results. Stopping");
			}
			logger.info( "Results found: " + results.size() );
			database.remove( results.iterator().next());
			results = database.search(filter);
			logger.info( "Results found: " + results.size() );
			
			database.add(model);
				
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally{
			database.close();
			database.deactivate();
		}
	}

	private final void testRESTAddModel() throws Exception{
		IModelNode<IDescriptor> model = createModel();
		TestFactory factory = TestFactory.getInstance();
		RestDatabase database = new RestDatabase( new SecureGenerator(), factory.getDomain(), IDatabaseConnection.REST_URL );	
		try{		
			database.open( factory.getDomain());
			logger.info("WAIT");
			TimeUnit.SECONDS.sleep(15);
			logger.info("START TEST");
			database.add(model);
			IModelFilter<IModelLeaf<IDescriptor>> filter = 
					new ModelFilter<IModelLeaf<IDescriptor>>( new AttributeFilter<IDescriptor>( AttributeFilter.Rules.WILDCARD, IDescriptor.Attributes.NAME.name(), "CATEGORY"));
			Collection<IModelLeaf<IDescriptor>> results = database.search(filter);
			for( IModelLeaf<? extends IDescriptor> leaf: results )
				logger.info( leaf.getDescriptor().toString() );
			if( results.isEmpty()){
				logger.info( "No Results. Stopping");
				return;
			}
			logger.info( "Results found: " + results.size() );
			database.remove( results.iterator().next());
			results = database.search(filter);
			logger.info( "Results found: " + results.size() );				
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally{
			database.close();
			database.deactivate();
		}
	}

	private IModelNode<IDescriptor> createModel() {
		String category = "Test";
		CategoryAieon cat = new CategoryAieon( category );
		cat.setVersion(1);
		
		URLAieon urlAieon = new URLAieon( "MyURL" );
		//urlAieon.setURI( "http://www.condast.com" );
		urlAieon.setDescription( "description" );
		urlAieon.setScope( IConcept.Scope.PUBLIC );
		urlAieon.setVersion(1);
		IModelLeaf<IDescriptor> urlModel = new ModelLeaf<IDescriptor>( urlAieon );		

		IModelNode<IDescriptor> model = new Model<IDescriptor>( cat);
		model.addChild( urlModel );
		return model;
	}


	@Override
	protected void onPrepare(ITestEvent<String, String> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPerform(ITestEvent<String, String> event) {
		// TODO Auto-generated method stub
		
	}
	
	private class SecureGenerator extends AbstractSecureProvider{

		@Override
		protected long createId(String domain) {
			return domain.hashCode();
		}

		@Override
		protected long createToken(String domain) {
			return domain.hashCode();
		}	
	}
	
	private class RestDatabase extends AbstractRestDatabase<IDescriptor, IModelLeaf<IDescriptor>>{

		protected RestDatabase(ISecureGenerator generator, IDomainAieon domain, String path) {
			super(generator, domain, path);
		}

		@Override
		protected String onSerialise(IModelLeaf<? extends IDescriptor>[] leafs) {
			GsonBuilder builder = new GsonBuilder();
			builder.enableComplexMapKeySerialization();
			builder.registerTypeAdapter(IModelLeaf.class, new ModelTypeAdapter());
			Gson gson = builder.create();
			String str = gson.toJson(leafs );
			//SerialisableModel.serialise(leafs);
			return str;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void getResults(ResponseEvent<Requests, IModelLeaf<IDescriptor>[]> event, Collection<IModelLeaf<IDescriptor>> results ) {
			GsonBuilder builder = new GsonBuilder();
			builder.enableComplexMapKeySerialization();
			builder.registerTypeAdapter(IModelLeaf.class, new ModelTypeAdapter());
			Gson gson = builder.create();
			logger.info("\n\n" + event.getResponse());
			IModelLeaf<IDescriptor>[] found = gson.fromJson(event.getResponse(), IModelLeaf[].class );
			//Collection<IModelLeaf<? extends IDescriptor>> found = SerialisableModel.deserialise(event.getResponse() );

			results.addAll( Arrays.asList(found )); 
		}

		@Override
		public IModelLeaf<? extends IDescriptor> onCreateModel() {
			// TODO Auto-generated method stub
			return null;
		}		
	}
}
