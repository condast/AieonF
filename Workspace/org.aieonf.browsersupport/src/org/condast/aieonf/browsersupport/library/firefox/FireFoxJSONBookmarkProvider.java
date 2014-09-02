package org.condast.aieonf.browsersupport.library.firefox;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.template.provider.AbstractModelProvider;
import org.aieonf.util.filter.IFilter;

public class FireFoxJSONBookmarkProvider<T extends ILoaderAieon> extends AbstractModelProvider<T,IConcept, IModelLeaf<IConcept>>
{

	public FireFoxJSONBookmarkProvider( IContextAieon context, IModelLeaf<T> model )
	{
		super( context, model );
	}

	@Override
	protected void onSetup( ManifestAieon manifest ) {
	}

	@Override
	public Collection<IModelLeaf<IConcept>> onSearch(IFilter<IDescriptor> filter) {
		{
			Collection<IModelLeaf<IConcept>> models = new ArrayList<IModelLeaf<IConcept>>();
			URI uri = URI.create( super.getManifest().getSource());
			JsonFactory f = new JsonFactory();
			try {
				File file = new File( uri );
				JsonParser jp = f.createParser( file );
				jp.nextToken();
				while (jp.nextToken() != JsonToken.END_OBJECT) {
					//String fieldname = jp.getCurrentName();
					jp.nextToken(); // move to value, or START_OBJECT/START_ARRAY
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return models;
		}
	}
}