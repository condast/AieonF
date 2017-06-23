package org.aieonf.model.function;

import java.io.File;
import java.net.URI;

import org.aieonf.commons.Utils;
import org.aieonf.commons.encryption.IEncryption.Algorithms;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.ModelLeaf;
import org.aieonf.model.builder.IFunctionProvider;

public abstract class AbstractFunctionProvider<D extends IDescribable<IDescriptor>, U extends Object> implements IFunctionProvider<String, U> {

	public static final String S_FUNCTION_PROVIDER_ID = "org.aieonf.function.provider";

	private static final String S_DEFAULT_ENVRYPTION_KEY = "aieonf.encryption";

	private String identifier;
	private IContextAieon aieon;
	
	protected AbstractFunctionProvider( String identifier, IContextAieon aieon ) {
		this.identifier = identifier;
		this.aieon = aieon;
	}

	protected String getIdentifier() {
		return identifier;
	}

	protected IContextAieon getAieon() {
		return aieon;
	}

	/**
	 * By default, no domain is supported
	 */
	@Override
	public boolean supportsDomain(IDomainAieon domain) {
		return false;
	}

	/**
	 * Returns true if the leaf's identifier conforms to the given one
	 * @param identifier
	 * @param leaf
	 * @return
	 */
	public boolean canProvide( String identifier ) {
		return S_FUNCTION_PROVIDER_ID.equals(identifier);
	}

	@Override
	public U getFunction(String descriptor) {
		if( !canProvide(descriptor))
			return null;
		return onCreateFunction(descriptor );
	}

	/**
	 * Create the access
	 * @return
	 */
	protected abstract U onCreateFunction( String describable); 

	/**
	 * Create a default loader for the given leaf
	 * @param leaf
	 * @return
	 */
	protected static ILoaderAieon getDefaultLoader( IContextAieon context ){
		LoaderAieon loader = new LoaderAieon();
		loader.setAieonCreatorClass( ILoaderAieon.class );
		loader.setIdentifier( loader.getScope().toString());
		loader.setStoreInternal( true );
		loader.setIdentifier( IModelLeaf.S_MODEL );
		loader.set( IDescriptor.Attributes.NAME, context.get( IConcept.Attributes.SOURCE ));
		loader.setVersion(1);
		loader.setEncryptionAlgorithm( Algorithms.AES );
		loader.setEncryptionKey( S_DEFAULT_ENVRYPTION_KEY );
		return loader;
	}

	/**
	 * Create a default model from the given leaf
	 * @param leaf
	 * @return
	 */
	protected static IModelLeaf<IDescriptor> getDefaultModel( IContextAieon context ){
		ILoaderAieon loader = getDefaultLoader( context );
		return getModelForLoader(loader, context);
	}

	/**
	 * Create a model for the given loader, extended with the given leaf
	 * @param loader
	 * @param context
	 * @return
	 */
	protected static IModelLeaf<IDescriptor> getModelForLoader( ILoaderAieon loader, IContextAieon context ){
		Descriptor.overwrite( loader, context.getDescriptor() );
		IModelLeaf<IDescriptor> model = new ModelLeaf<IDescriptor>( loader );
		if( !Utils.assertNull( context.getID()))
			model.setIdentifier( context.getID() );
		return model;
	}

	/**
	 * Fill the loader with the given details
	 * @param loader
	 * @param identifier
	 * @param folder
	 * @param reference
	 * @throws CollectionException
	 */
	public static void fillLoader( ILoaderAieon loader, String identifier, String providerName, String folder, String reference )
	{
		loader.setIdentifier( identifier );
		loader.setProviderName(providerName);
		loader.setURI( getDefaultSource( folder, reference ));
	}

	/**
	 * Get the default location for the personal database
	 * @param folder, the folder that serves as the root for the search
	 * @param reference, the actual file that has to be retrieved
	 * @return String
	 */
	public static URI getDefaultSource( String folder, String reference )
	{
		String ffRoot = System.getProperty( "user.home" ) + folder;
		File root = new File( ffRoot );
		root = findFile( root, reference );
		return root.toURI();
	}

	/**
	 * Find the correct bookmarks file
	 * @param file
	 * @return
	 */
	protected static File findFile( File file, String reference ){
		if( file.getName().equals( reference ))
			return file;
		if( file.isDirectory() == false )
			return null;
		File[] children = file.listFiles();
		File result;
		if(( children == null ) || ( children.length == 0))
			return null;
		for( File child: children ){
			result = findFile( child, reference );
			if( result != null ){
				return result;
			}
		}
		return null;
	}
}