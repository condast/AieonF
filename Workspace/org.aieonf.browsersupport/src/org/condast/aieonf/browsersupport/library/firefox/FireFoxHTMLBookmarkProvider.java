package org.condast.aieonf.browsersupport.library.firefox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Collection;
import java.util.Stack;

import org.condast.aieonf.utils.base64.Base64Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.collections.CollectionException;
import org.aieonf.concept.datauri.DataURI;
import org.aieonf.concept.datauri.IDataURI;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.Model;
import org.aieonf.model.ModelLeaf;
import org.aieonf.template.provider.AbstractModelProvider;
import org.aieonf.util.filter.IFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;

public class FireFoxHTMLBookmarkProvider<T extends ILoaderAieon> extends AbstractModelProvider<T,IConcept, IModelLeaf<IConcept>>
{
	private int depth = 0;
	private boolean category = false;
	private FireFoxReference currentURL;
	private Stack<IModelNode<IConcept>> stack;

	public FireFoxHTMLBookmarkProvider( IContextAieon context, IModelLeaf<T> model )
	{
		super( context, model );
	}

	@Override
	protected void onSetup( ManifestAieon manifest ) {
	}

	@Override
	public Collection<IModelLeaf<IConcept>> onSearch(IFilter<IDescriptor> filter) {
		Collection<IModelLeaf<IConcept>> results = super.getModels();
		stack = new Stack<IModelNode<IConcept>>();
		CategoryAieon root = new CategoryAieon();
		root.setHidden( true );
		root.setReadOnly( true );
		root.setCategory( "root" );
		stack.push( new Model<IConcept>( root ));

		URI uri = URI.create( super.getManifest().getSource());
		File file = new File( uri );
		FileInputStream reader = null;
		try {
			reader = new FileInputStream( file );
			Lexer lexer= new Lexer( new Page( reader, null ));
			org.htmlparser.util.NodeIterator iterator =
					new org.htmlparser.util.IteratorImpl( lexer, new org.htmlparser.util.DefaultParserFeedback() );
			org.htmlparser.Node node;
			while( iterator.hasMoreNodes() ){
				node = iterator.nextNode();
				this.parseNode( node );
			}
		}
		catch (Exception e) {
			e.printStackTrace();;
		}
		finally{
			if( reader != null )
				try {
					reader.close();
				}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	/**
	 * Parses the given node
	 *
	 * @param node Node
	 * @throws Exception
	 */
	protected void parseNode( org.htmlparser.Node node ) throws Exception
	{
		if( node == null )
			return;
		String str = node.toString();
		String[] split = str.split( "[:]" );
		if( split.length < 2 )
			return;
		if(( split[ 0 ].startsWith( "Tag" )) && ( split[ 1 ].trim().equals( "DL" )))
			depth++;
		if(( split[ 0 ].startsWith( "End" )) && ( split[ 1 ].trim().equals( "/DL" ))){
			stack.pop();
			depth--;
		}
		//Only start parsing with depth = 0;
		if( depth == 0 )
			return;

		IModelNode<IConcept> cat;
		if( category ){
			cat = this.createCategoryEon( split[ 0 ], split[ 1 ] );
			if( cat != null )
				category = false;
		}
		FireFoxReference urlEon = this.createURLEon( split[ 0 ], node.getText() );
		if( urlEon != null )
			this.currentURL = urlEon;

		if(( this.currentURL != null ) && ( split[ 0 ].startsWith( "Txt" ) == true )){
			cat = ( IModelNode<IConcept> )this.stack.lastElement();
			urlEon = this.currentURL;

			String[] split1 = split[ 1 ].trim().split( "[:]" );
			String name = split1[ split1.length - 1 ];
			if(( name == null ) || ( name.trim().isEmpty() ))
				return;

			String description = name;
			name = Descriptor.makeValidName( name );
			urlEon.set( IDescriptor.Attributes.NAME.toString(), name );
			urlEon.setDescription( description );
			cat.addChild( new ModelLeaf<IConcept>( urlEon ));
			if( Descriptor.isNull( urlEon.getResource() )){
				String datauri = Base64Utils.getBase64DataURI( this.getClass(), "/images/firefox.png");
				try {
					if( !Descriptor.isNull( datauri ))
						urlEon.setResource( datauri );
				}
				catch (ConceptException e) {
					throw new CollectionException( e );
				}
			}
			
			this.currentURL = null;
		}
		if(( split[ 0 ].startsWith( "Tag" )) && ( split[ 1 ].trim().startsWith( "H" )))
			category = true;
		if(( split[ 0 ].startsWith( "End" )) && ( split[ 1 ].trim().equals( "/H" )))
			depth--;
	}

	/**
	 * Create a category eon if the two stings allow it
	 * @param split0 String
	 * @param split1 String
	 * @return CategoryEon
	 * @throws Exception
	 */
	protected IModelNode<IConcept> createCategoryEon( String split0, String split1 )
			throws Exception
	{
		if(( split0.startsWith( "Txt" ) == false ) || ( split1.trim().startsWith( "\\" )))
			return null;
		String name = Descriptor.makeValidName( split1.trim() );
		CategoryAieon cat = new CategoryAieon( name );
		this.IDFactory( cat );
		cat.setVersion( 1 );
		cat.setReadOnly( true );
		cat.setScope( IConcept.Scope.PRIVATE );
		cat.setProvider( super.getManifest().getIdentifier() );
		Calendar calendar = Calendar.getInstance();
		cat.set( IDescriptor.Attributes.CREATE_DATE.toString(), calendar.getTime().toString() );
		cat.set( IDescriptor.Attributes.UPDATE_DATE.toString(), calendar.getTime().toString() );

		cat.set( IConcept.Attributes.SOURCE.toString(), super.getManifest().getIdentifier() );
		BodyFactory.sign( super.getManifest(), cat );
		IModelNode<IConcept> model = new Model<IConcept>( cat );
		stack.push( model );
		return model;
	}

	/**
	 * Create an url eon if the two stings allow it
	 * @param split0 String
	 * @param split1 String
	 * @return URLEon
	 * @throws Exception
	 */
	protected FireFoxReference createURLEon( String split0, String split1  )
			throws Exception
	{
		if(( split0.startsWith( "Tag" ) == false ) ||
				( split1.trim().startsWith( "A" ) == false )){
			return null;
		}

		// Extract the data uri
		FireFoxReference urlEon = new FireFoxReference();
		urlEon.fill( split1 );

		if( urlEon.isDataUri() ){
			IDataURI resource = new DataURI();
			resource.fill( "ICON", urlEon.getResource());
			if( !Base64Utils.isArrayByteBase64(resource.getResource().getBytes("UTF-8" ))){
				String datauri = Base64Utils.getBase64DataURI( this.getClass(), "/images/firefox.png");
				try {
					if( !Descriptor.isNull( datauri ))
						urlEon.setResource( datauri );
				}
				catch (ConceptException e) {
					throw new CollectionException( e );
				}
			}
		}
		super.fill( urlEon );
		super.getModels().add( new ModelLeaf<IConcept>( urlEon ));
		return urlEon;
	}
}
