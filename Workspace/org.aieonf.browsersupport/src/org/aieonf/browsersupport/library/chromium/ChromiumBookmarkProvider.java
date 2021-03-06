package org.aieonf.browsersupport.library.chromium;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.library.URLAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.template.provider.AbstractModelProvider;

class ChromiumBookmarkProvider extends AbstractModelProvider<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>>
{
	private static final String S_CHROMIUM = "Chrome";
	private static final String S_ROOTS = "roots";


	ChromiumBookmarkProvider( IDescriptor context ){
		super( S_CHROMIUM, context );
	}
	
	@Override
	protected void onSetup( ManifestAieon manifest ) {
	}

	@Override
	public boolean onOpen( IDomainAieon domain) {
		URI uri = super.getManifest().getURI();
		File file = new File( uri );
		return file.exists();
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> onSearch( IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter) {
		super.getModels().clear();
		try {
			parseTree( filter );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return super.getModels();
	}

	private void parseTree( IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter) throws ParseException{
		ObjectMapper m = new ObjectMapper();
		JsonNode rootNode;
		URI uri = super.getManifest().getURI();
		File file = new File( uri );
		try {
			rootNode = m.readValue( file, JsonNode.class);
			JsonNode roots = rootNode.path( S_ROOTS );	
			this.parseTree( null, roots, filter );
		}
		catch (IOException e) {
			throw new ParseException( e );
		}
		catch (ConceptException e) {
			throw new ParseException( e );
		}
	}

	private void parseTree( IModelNode<IDescriptor> leaf, JsonNode node, IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter ) throws ParseException, ConceptException{
		//if(( leaf != null ) && ( filter.accept( leaf )))
		//	models.add( leaf );

		String type = node.path("type").textValue();
		ChromiumAieon.Types ctype = ( StringUtils.isEmpty( type )? ChromiumAieon.Types.OTHER: ChromiumAieon.Types.valueOf( StringStyler.styleToEnum( type )));
		IModelLeaf<IDescriptor> model = null;
		ChromiumAieon aieon = new ChromiumAieon( ctype );
		aieon.fill(node );
		fill(aieon);
		//IDFactory( aieon );

		IModelNode<IDescriptor> parent = leaf;
		switch( ctype ){
		case FOLDER:
			Collection<IModelLeaf<IDescriptor>> models = super.getModels();
			parent = new Model<IDescriptor>( new CategoryAieon( aieon ));
			parent.setReadOnly(true);
			model = parent;
			if( filter.accept( model ))
				models.add( model );
			break;
		case URL:
			model = new ModelLeaf<IDescriptor>( aieon );
			model.setReadOnly(true);
			if(( parent != null )/* && ( filter.acceptChild(model ))*/)
				parent.addChild( model );
			break;
		default:
			model = new Model<IDescriptor>( aieon );
			model.setReadOnly(true);
			break;
		}
		Iterator<JsonNode> iterator = node.iterator();
		while( iterator.hasNext() ){
			JsonNode child = iterator.next();
			this.parseTree(parent, child, filter );
		}
	}

	public void parseStream() throws ParseException{
		URI uri = URI.create( super.getManifest().getSource());
		JsonFactory f = new JsonFactory();
		try {
			File file = new File( uri );
			JsonParser jp = f.createParser( file );
			String namefield;
			while (jp.nextToken() != JsonToken.END_OBJECT) {
				do{
					jp.nextToken();
					namefield = jp.getCurrentName();
				}while( !namefield.equals("children"));

				//Iterate through the children. These are categories
				ChromiumAieon category;
				ChromiumAieon urlAieon;
				jp.nextToken();
				while (jp.nextToken() != JsonToken.END_OBJECT) {
					namefield = jp.getCurrentName();
					if( Descriptor.assertNull( namefield ))
						continue;
					jp.nextToken();
					jp.nextToken();
					category = new ChromiumAieon( ChromiumAieon.Types.URL );
					category.fill(jp);
					while( jp.nextToken() != JsonToken.END_OBJECT ){
						urlAieon = new ChromiumAieon( ChromiumAieon.Types.URL);
						urlAieon.fill( jp );
						//urlAieon.addRelationship( new EmbeddedRelationship( urlAieon, new CategoryAieon( category )));
					}
				}
			}
		}
		catch (Exception e) {
			throw new ParseException( e );
		}
		finally{
		}

	}

	/**
	 * Fill the concept with the relevant values obtained from the manifest
	 * @param concept
	 * @throws ConceptException
	 */
	protected void fill( IDescriptor concept ) throws ConceptException{
		try {
			ILoaderAieon manifest = super.getManifest();
			IDFactory( (IConcept) concept );
			concept.setProvider( manifest.getIdentifier() );
			concept.setProviderName( manifest.getProviderName() );
			concept.set( IConcept.Attributes.SOURCE.name(), manifest.getIdentifier() );
			BodyFactory.sign( manifest, concept );
		}
		catch (IOException e) {
			throw new ConceptException( e );
		}
		catch (ConceptException e) {
			throw new ConceptException( e );
		}		
	}

	/**
	 * Create a unique id for the concepts
	 * @return String
	 * @throws CollectionException
	 */
	@Override
	protected void IDFactory( IConcept concept ) throws ConceptException
	{
		long id_def = concept.getID();
		try{
			BodyFactory.sign( super.getManifest(), concept );
			long id = IDFactory( concept, super.getModels() );
			id = ( id<0 )? id: id + id_def;
			concept.set( IDescriptor.Attributes.ID.name(), String.valueOf( id ));
		}
		catch( Exception ex ){
			throw new ConceptException( ex );
		}
	}

	/**
	 * Create an id for the given concept
	 *
	 * @param descriptor IConcept
	 * @param collection IConceptCollection
	 * @return String
	 * @throws CollectionException
	 */
	@Override
	public long IDFactory( IDescriptor descriptor, Collection<? extends IDescribable> descriptors )
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append( super.getManifest().getID() + ":" );

		long newId = descriptor.hashCode();
		boolean containsId = false;
		String hexStr;
		do{
			containsId = false;
			newId = ( long )( Math.random() * Long.MAX_VALUE );
			hexStr = Long.toHexString( newId );
			for( IDescribable desc: descriptors ){
				if( hexStr.equals( desc.getDescriptor().getID()  )){
					containsId = true;
					break;
				}
			}
		}
		while( containsId == true );

		buffer.append( hexStr.toUpperCase());
		return newId;
	}

	private static class ChromiumAieon extends Concept
	{
		private static final long serialVersionUID = -948073322887755282L;

		public static enum Attributes{
			CHILDREN,
			TYPE,
			NAME,
			GUID,
			ID,
			URL,
			DATE_ADDED,
			DATE_MODIFIED;

			@Override
			public String toString() {
				return super.toString().toLowerCase();
			}	
			
			public static boolean isAttribute( String str ){
				for( Attributes attr: values()){
					if( attr.toString().equals( str ))
						return true;
				}
				return false;
			}
			
			public static String convert( Attributes attr ){
				String convert = null;
				switch( attr ){
				case NAME:
					break;
				case ID:
					convert = attr.name();
					break;
				case DATE_ADDED:
					convert = IDescriptor.Attributes.CREATE_DATE.name();
					break;
				case DATE_MODIFIED:
					convert = IDescriptor.Attributes.UPDATE_DATE.name();
					break;
				case URL:
					convert = IConcept.Attributes.SOURCE.name();
					break;
				case TYPE:
					convert = ConceptBase.getAttributeKey( ILoaderAieon.Attributes.TYPE );
					break;
				case CHILDREN:
					break;
				default:
					convert = ConceptBase.getAttributeKey( attr );
					break;
				}
				return convert;
			}
		}

		public static enum Types{
			FOLDER,
			URL,
			OTHER;

			/* (non-Javadoc)
			 * @see java.lang.Enum#toString()
			 */
			@Override
			public String toString()
			{
				return StringStyler.prettyString( super.toString() );
			}
		}
		
		private ChromiumAieon( Types type ) {
			super();
			super.set( Attributes.TYPE, type.toString());
		}

		private ChromiumAieon(String name) {
			super(name);
		}

		public void fill( JsonNode node ) throws ConceptException 
		{
			//Fill the descriptor with the JSON node
			Iterator<Entry<String,JsonNode>> iterator = node.fields();
			while( iterator.hasNext() ){
				Entry<String,JsonNode> entry = iterator.next();
				if( !Attributes.isAttribute( entry.getKey() ))
					continue;
				Attributes attr = Attributes.valueOf( entry.getKey().toUpperCase());
				String value =  entry.getValue().textValue();
				if( Descriptor.assertNull( value ))
					continue;
				switch( attr) {
				case ID:
				case CHILDREN:
					break;
				case NAME:
					setIdentifier( value );
					setDescription(value);
					break;
				case URL:
					setValue( IConcept.Attributes.SOURCE, value );
					break;
				case GUID:
					long id = (long)S_CHROMIUM.hashCode();
					id <<=32;
					id += (long)value.hashCode();
					setID( id );
				default:
					set( Attributes.convert(attr), value );
					break;
				}
			}
			String type = super.get( Attributes.TYPE );
			if( type.equals( Types.FOLDER.toString() )){
				super.set( CategoryAieon.Attributes.CATEGORY.name(), S_CHROMIUM + ": " + getIdentifier());
				super.setValue( IDescriptor.Attributes.NAME, CategoryAieon.Attributes.CATEGORY.name() );
			}else{
				super.setValue( IDescriptor.Attributes.NAME, URLAieon.Attributes.URL.toString() );
			}
			super.fill();
		}

		public void fill( JsonParser parser) throws ConceptException, JsonParseException, IOException{
			while (parser.nextToken() != JsonToken.END_OBJECT) {
				String namefield = parser.getCurrentName();
				parser.nextToken();
				if( Descriptor.assertNull( namefield ))
					continue;
				Attributes attr = Attributes.valueOf(namefield);
				set( attr, parser.getText() );
			}
			String type = super.get( Attributes.TYPE );
			if( type.equals( Types.FOLDER.name() )){
				super.setValue( IDescriptor.Attributes.NAME, CategoryAieon.Attributes.CATEGORY.name() );
				super.set( CategoryAieon.Attributes.CATEGORY.name(), S_CHROMIUM + ": " + getIdentifier() );
			}else{
				String name = super.get( Attributes.NAME );
				super.setValue( IDescriptor.Attributes.NAME, URLAieon.Attributes.URL.name() );
				super.setValue( IConcept.Attributes.SOURCE, URLAieon.Attributes.URL.name() );
				super.setDescription( name );
			}
			super.remove( IDescriptor.Attributes.NAME.name() );
			super.fill();
		}
	}
}
