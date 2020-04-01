package org.aieonf.template.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.sign.SignatureFactory;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.HierarchicalModelDescriptorFilter;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;

public abstract class AbstractModelProvider<T extends IDescribable, K extends Object, V extends IDescribable> implements IModelProvider<K, V> {

	private static final String S_ERR_PROVIDER_NOT_OPEN = "The provider is not open";

	private Collection<IModelListener<V>> listeners;
	private boolean open;
	private boolean requestClose; //delay closing when search is conducted in separate threads
	private int pending;
	
	private Collection<V> models;

	private ManifestAieon manifest;
	
	private T template;
	private String identifier;
	private SignatureFactory signer = SignatureFactory.getInstance();

	protected AbstractModelProvider( String identifier, T template ) {
		listeners = new ArrayList<IModelListener<V>>();
		this.identifier = identifier;
		this.template = template;
		manifest = this.setup( template);
		models = new ArrayList<V>();
		this.pending = 0;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	protected ManifestAieon getManifest() {
		return manifest;
	}

	protected Collection<V> getModels() {
		return models;
	}

	

	@Override
	public void addListener(IModelListener<V> listener) {
		this.listeners.add( listener );
	}

	@Override
	public void removeListener(IModelListener<V> listener) {
		this.listeners.remove( listener );
	}

	protected final void notifyListeners( ModelEvent<V> event ){
		for( IModelListener<V> listener: this.listeners )
			listener.notifyChange(event);
	}

	/**
	 * Set up the model provider
	 */
	protected abstract void onSetup( ManifestAieon manifest );

	private ManifestAieon setup( T template ){
		ManifestAieon manifest = new ManifestAieon( template.getDescriptor() );
		try {
			manifest.fill(template.getDescriptor());
			this.onSetup(manifest);
			int hashcode = this.template.getDescriptor().hashCode();
			manifest.set( IDescriptor.Attributes.ID, String.valueOf( hashcode ));
			signer.init(manifest);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return manifest;	
	}

	/**
	 * Provide a mechanism to access the function
	 * @param key
	 * @return
	 */
	protected abstract boolean onOpen( K key );
	
	@Override
	public void open( K key ){
		this.open = this.onOpen(key);
		this.requestClose = !this.open;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	protected boolean isRequestClose() {
		return requestClose;
	}

	protected void setRequestClose(boolean requestClose) {
		this.requestClose = requestClose;
	}

	protected void setBusy(){
		this.pending++;
	}
	
	protected boolean isPending(){
		return ( pending > 0 );
	}
	
	protected void release(){
		this.pending--;
	}
	
	/**
	 * Sync the actual model with the database
	 */
	@Override
	public void sync(){}

	@Override
	public void close(){
		this.requestClose = true;
		this.open = false;
	}

	@Override
	public boolean hasFunction(String function) {
		return DefaultModels.PROVIDER.toString().equals( function );
	}

	//Default no contains.
	@Override
	public boolean contains( V leaf) {
		return false;
	}

	@Override
	public Collection<V> get(IDescriptor descriptor) throws ParseException {
		@SuppressWarnings("unchecked")
		IModelFilter<IDescriptor,V> flt = (IModelFilter<IDescriptor, V>) new HierarchicalModelDescriptorFilter<IDescriptor>( descriptor );
		return this.search(flt);
	}

	protected abstract Collection<V> onSearch(IModelFilter<IDescriptor,V> filter) throws ParseException;

	
	@Override
	public Collection<V> search(IModelFilter<IDescriptor,V> filter) throws ParseException {
		if( !open )
			throw new ParseException( S_ERR_PROVIDER_NOT_OPEN );
		models = onSearch( filter);
		notifyListeners( new ModelEvent<V>(this, models ));
		return models;
	}
	
	@Override
	public void deactivate() {
		this.close();
	}

	/**
	 * Fill the concept with the relevant values obtained from the manifest
	 * @param concept
	 * @throws ConceptException
	 */
	protected void fill( IConcept concept ) throws ConceptException{
		try {
			IDFactory( concept );
			concept.setProvider( manifest.getIdentifier() );
			concept.setProviderName( manifest.getProviderName() );
			concept.set( IConcept.Attributes.SOURCE.name(), manifest.getIdentifier() );
			signer.sign( concept );
		}
		catch (IOException e) {
			throw new ConceptException( e );
		}
	}

	/**
	 * Create a unique id for the concepts
	 * @return String
	 * @throws ConceptException 
	 * @throws CollectionException
	 */
	protected void IDFactory( IConcept concept ) throws ConceptException
	{
		if( StringUtils.isEmpty(concept.getID()))
			return;
		try{
			signer.sign( concept );
			long id = IDFactory( concept, models );
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
	public long IDFactory( IDescriptor descriptor, Collection<? extends IDescribable> descriptors )
	{
		long newId = descriptor.hashCode();
		boolean containsId = false;
		do{
			containsId = false;
			newId = ( long )( Math.random() * Long.MAX_VALUE );
			for( IDescribable desc: descriptors ){
				if( desc.getDescriptor().getID().equals( newId )){
					containsId = true;
					break;
				}
			}
		}
		while( containsId == true );
		return newId;
	}

}
