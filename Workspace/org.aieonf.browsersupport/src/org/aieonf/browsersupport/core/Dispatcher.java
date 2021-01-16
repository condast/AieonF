package org.aieonf.browsersupport.core;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.library.CategoryAieon;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.provider.ConceptProviderUtils;
import org.aieonf.model.provider.IConceptProvider;

public class Dispatcher {

	private Collection<IConceptProvider> providers;
	
	private static Dispatcher dispatcher = new Dispatcher();

	private Dispatcher() {
		providers = new ArrayList<>();
	}

	/**
	 * Get an instance of the bar link service
	 * @return
	 */
	public static Dispatcher getInstance(){
		return dispatcher;
	}

	public void addProvider(IConceptProvider provider) {
		providers.add(provider);
	}

	public void removeProvider(IConceptProvider provider) {
		providers.remove(provider);
	}
	
	public void complete(IModelNode<IDescriptor> leaf) {
		for( IConceptProvider provider: providers ){
			ConceptProviderUtils cpu = new ConceptProviderUtils( provider );
			cpu.complete( CategoryAieon.Attributes.CATEGORY.name(), leaf );
		}	
	}
}
