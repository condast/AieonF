package org.aieonf.template.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.filter.HierarchicalModelDescriptorFilter;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.util.parser.ParseException;

public abstract class AbstractModelProvider<T extends ILoaderAieon, U extends IDescriptor,V extends IDescribable<U>> implements IModelProvider<T,V> {

	public static final String S_ERR_PROVIDER_NOT_OPEN = "The provider is not open";

	private Collection<IModelBuilderListener> listeners;
	private boolean open;

	private Collection<V> models;

	private IModelLeaf<T> model;
	private ManifestAieon manifest;
	
	private IContextAieon context;

	protected AbstractModelProvider( IContextAieon context, IModelLeaf<T> model ) {
		listeners = new ArrayList<IModelBuilderListener>();
		this.context = context;
		manifest = this.setup( model);
		this.model = model;
		models = new ArrayList<V>();
	}

	protected ManifestAieon getManifest() {
		return manifest;
	}

	protected Collection<V> getModels() {
		return models;
	}

	public void addListener( IModelBuilderListener listener ){
		this.listeners.add( listener );
	}

	public void removeListener( IModelBuilderListener listener ){
		this.listeners.remove( listener );
	}

	protected final void notifyListeners( ModelBuilderEvent event ){
		for( IModelBuilderListener listener: this.listeners )
			listener.notifyChange(event);
	}

	/**
	 * Set up the model provider
	 */
	protected abstract void onSetup( ManifestAieon manifest );

	protected ManifestAieon setup(IModelLeaf<T> model) {
		ManifestAieon manifest = new ManifestAieon( model.getDescriptor() );
		try {
			manifest.fill(model.getDescriptor());
		} catch (ConceptException e) {
			e.printStackTrace();
		}
		this.onSetup(manifest);
		int hashcode = this.context.getSource().hashCode();
		manifest.set( IDescriptor.Attributes.ID, String.valueOf( hashcode ));
		return manifest;	
	}

	public void open(){
		this.open = true;
	}

	public boolean isOpen() {
		return open;
	}

	public void close(){
		this.open = false;
	}

	
	@Override
	public boolean contains(IModelLeaf<?> leaf) {
		// TODO Auto-generated method stub
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
			T manifest = model.getDescriptor();
			concept.setProvider( manifest.getIdentifier() );
			concept.setProviderName( manifest.getProviderName() );
			concept.set( IConcept.Attributes.SOURCE.name(), manifest.getIdentifier() );
			BodyFactory.sign( manifest, concept );
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
			T manifest = model.getDescriptor();
			BodyFactory.sign( manifest, concept );
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
		buffer.append( model.getDescriptor().getSource() + ":" );

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
