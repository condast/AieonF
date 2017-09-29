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
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.template.provider.AbstractModelProvider;

public class FireFoxJSONBookmarkProvider extends AbstractModelProvider<IContextAieon, IDomainAieon, IModelLeaf<IDescriptor>>
{
	public static final String S_IDENTIFER = "FirefoxJsonBookmarks";

	public FireFoxJSONBookmarkProvider( IContextAieon template, IModelLeaf<IConcept> model )
	{
		super( S_IDENTIFER, template );
	}

	@Override
	protected void onSetup( ManifestAieon manifest ) {
	}

	@Override
	public Collection<IModelLeaf<IDescriptor>> onSearch(IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter) {
		{
			Collection<IModelLeaf<IDescriptor>> models = new ArrayList<IModelLeaf<IDescriptor>>();
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

	@Override
	protected boolean onOpen(IDomainAieon key) {
		return true;
	}
}