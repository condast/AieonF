package org.aieonf.sketch.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.sketch.Activator;
import org.aieonf.template.builder.TemplateInterpreterFactory;
import org.aieonf.template.context.AbstractProviderContextFactory;

public class SketchModelFactory extends AbstractProviderContextFactory<IContextAieon, IModelLeaf<IContextAieon>> {

	public static final String S_AIEONF_INF = "AIEONF-INF";
	public static final String S_AIEONF = "aieonf-";
	public static final String S_XML = ".xml";
	public static final String S_REGEX = "^" + S_AIEONF + ".*\\" + S_XML + "$";
	
	public static final String S_WEB = "/web/";
	
	private File root;
	
	public SketchModelFactory( File root ){
		super( Activator.BUNDLE_ID, new SketchInterpreterFactory( SketchModelFactory.class, root ));
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

	public String getFilePath( String location ) throws MalformedURLException{
		File file = new File( root, S_WEB + location );
		return file.getAbsolutePath();
	}

	@Override
	public boolean hasFunction(String function) {
		// TODO Auto-generated method stub
		return false;
	}

	public static SketchModelFactory getFactory( String root, IDomainAieon domain ) throws MalformedURLException {
		File file = new File( root );
		if( !file.exists() || !file.isDirectory())
			return null;
		for( File child: file.listFiles()) {
			if( !child.isDirectory())
				continue;
			File[] aieonf = child.listFiles( new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return S_AIEONF_INF.equals(name);
				}	
			});
			if( Utils.assertNull( aieonf ))
				continue;
			File aieonfdir = aieonf[0];
			aieonf = aieonfdir.listFiles( new FileFilter());
			if( Utils.assertNull( aieonf ))
				continue;
			
			SketchModelFactory factory = new SketchModelFactory( child );
			factory.createTemplate();
			IDomainAieon check = factory.getDomain();
			if( check.equals(domain))
				return factory;
		}
		return null;
	}
	
	public static Map<IDomainAieon, SketchModelFactory> getFactories( String root ) throws MalformedURLException {
		Map<IDomainAieon, SketchModelFactory> results = new HashMap<>();
		File file = new File( root );
		if( !file.exists() || !file.isDirectory())
			return null;
		for( File child: file.listFiles()) {
			if( !child.isDirectory())
				continue;
			File[] aieonf = child.listFiles( new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return S_AIEONF_INF.equals(name);
				}	
			});
			if( Utils.assertNull( aieonf ))
				continue;
			File aieonfdir = aieonf[0];
			aieonf = aieonfdir.listFiles( new FileFilter());
			if( Utils.assertNull( aieonf ))
				continue;
			
			SketchModelFactory factory = new SketchModelFactory( child );
			factory.createTemplate();
			IDomainAieon domain = factory.getDomain();
			results.put(domain,  factory);
		}
		return results;
	}

	private static class SketchInterpreterFactory extends TemplateInterpreterFactory<IContextAieon>{

		private File root;
		
		public SketchInterpreterFactory(Class<?> clss, File root) {
			super(clss);
			this.root = root;
		}

		@Override
		public InputStream createInputStream(String resource) {
			File aieonf = new File( root, IModelBuilder.S_DEFAULT_LOCATION );
			try {
				return new FileInputStream( aieonf.getAbsolutePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	private static class FileFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String name) {
			return ( StringUtils.isEmpty(name)? false: name.toLowerCase().matches( S_REGEX ));
		}			
	}
}
