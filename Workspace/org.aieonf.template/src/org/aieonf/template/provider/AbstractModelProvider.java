package org.aieonf.template.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.sign.SignatureFactory;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.filter.HierarchicalModelDescriptorFilter;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;

public abstract class AbstractModelProvider<U extends IDescriptor,V extends IDescribable<U>> implements IModelProvider<V> {

	private static final String S_ERR_PROVIDER_NOT_OPEN = "The provider is not open";

	private Collection<IModelBuilderListener<V>> listeners;
	private boolean open;
	private boolean requestClose; //delay closing when searcgh is conducten in separate threads
	private int pending;
	
	private Collection<V> models;

	private ManifestAieon manifest;
	
	private IContextAieon context;
	private String identifier;
	private SignatureFactory signer = SignatureFactory.getInstance();

	protected AbstractModelProvider( String identifier, IContextAieon context, IModelLeaf<U> model ) {
		listeners = new ArrayList<IModelBuilderListener<V>>();
		this.identifier = identifier;
		this.context = context;
		manifest = this.setup( model);
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
	public void addListener( IModelBuilderListener<V> listener ){
		this.listeners.add( listener );
	}

	@Override
	public void removeListener( IModelBuilderListener<V> listener ){
		this.listeners.remove( listener );
	}

	protected final void notifyListeners( ModelBuilderEvent<V> event ){
		for( IModelBuilderListener<V> listener: this.listeners )
			listener.notifyChange(event);
	}

	/**
	 * Set up the model provider
	 */
	protected abstract void onSetup( ManifestAieon manifest );

	private ManifestAieon setup(IModelLeaf<U> model) {
		ManifestAieon manifest = new ManifestAieon( model.getDescriptor() );
		try {
			manifest.fill(model.getDescriptor());
			this.onSetup(manifest);
			int hashcode = this.context.getSource().hashCode();
			manifest.set( IDescriptor.Attributes.ID, String.valueOf( hashcode ));
			signer.init(manifest);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return manifest;	
	}

	@Override
	public void open( IDomainAieon domain){
		this.requestClose = false;
		this.open = true;
	}

	@Override
	public boolean isOpen( IDomainAieon domain) {
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
	public void close( IDomainAieon domain ){
		this.requestClose = true;
		this.open = false;
	}

	@Override
	public boolean hasFunction(String function) {
		return DefaultModels.PROVIDER.toString().equals( function );
	}

	//Default no contains.
	@Override
	public boolean contains(IModelLeaf<?> leaf) {
		return false;
	}

	@Override
	public Collection<V> get(IDescriptor descriptor) throws ParseException {
		IModelFilter<IDescriptor> flt = new HierarchicalModelDescriptorFilter<IDescriptor>( descriptor );
		return this.search(flt);
	}

	protected abstract Collection<V> onSearch(IModelFilter<IDescriptor> filter) throws ParseException;

	@Override
	public Collection<V> search(IModelFilter<IDescriptor> filter) throws ParseException {
		if( !open )
			throw new ParseException( S_ERR_PROVIDER_NOT_OPEN );
		models = onSearch( filter);
		notifyListeners( new ModelBuilderEvent<>(this, models ));
		return models;
	}

	@Override
	public String printDatabase() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void deactivate() {
		this.close( null);
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
		if( !Descriptor.isNull( concept.getID()))
			return;
		try{
			signer.sign( concept );
			String id = IDFactory( concept, models );
			concept.set( IDescriptor.Attributes.ID.name(), id);
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
	public String IDFactory( IDescriptor descriptor, Collection<? extends IDescribable<?>> descriptors )
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append( manifest.getSource() + ":" );

		long newId = descriptor.hashCode();
		boolean containsId = false;
		String hexStr;
		do{
			containsId = false;
			newId = ( long )( Math.random() * Long.MAX_VALUE );
			hexStr = Long.toHexString( newId );
			for( IDescribable<?> desc: descriptors ){
				if( desc.getDescriptor().getID().equals( hexStr )){
					containsId = true;
					break;
				}
			}
		}
		while( containsId == true );

		buffer.append( hexStr.toUpperCase());
		return buffer.toString();
	}

}
