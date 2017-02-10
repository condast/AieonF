package org.aieonf.sketch.factory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.sketch.Activator;
import org.aieonf.template.builder.DefaultModelBuilder;
import org.aieonf.template.context.AbstractProviderContextFactory;

public class SketchModelFactory extends AbstractProviderContextFactory<IModelLeaf<IDescriptor>> {

	public static final String S_WEB = "/web/";
	
	private File root;
	
	public SketchModelFactory(File root ) throws MalformedURLException {
		super( Activator.BUNDLE_ID, new DefaultModelBuilder( SketchModelFactory.class, getAieonFURL( root ) ));
		this.root = root;
	}

	public String getWebPath( String location ) throws MalformedURLException{
		Path path = Paths.get( root.getAbsolutePath(), S_WEB + location );
		return path.toUri().toString();
	}

	public URL getWebURL( String location ) throws MalformedURLException{
		File web = new File( root, S_WEB + location );
		return web.toURI().toURL();
	}

	private static URL getAieonFURL( File root ) throws MalformedURLException{
		File aieonf = new File( root, IModelBuilder.S_DEFAULT_LOCATION );
		return aieonf.toURI().toURL();
	}

	@Override
	protected void onBuildEvent(ModelBuilderEvent<IModelLeaf<IDescriptor>> event) {
		// TODO Auto-generated method stub
		
	}

}
