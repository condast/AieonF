package org.aieonf.model.serialise;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;
import org.aieonf.model.core.ModelLeaf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SerialisableModel extends SerialisableNode{

	//children
	private Map<SerialisableNode, String> c;

	protected SerialisableModel() {
		super();
		c = new HashMap<>();
	}
	
	public SerialisableModel( IModelLeaf<? extends IDescriptor> model ) {
		super( model );
		if( !( model instanceof IModelNode<?> ))
			return;
		c = new HashMap<>();
		IModelNode<?> node=  (IModelNode<?>) model;
		Collection<Map.Entry<IModelLeaf<? extends IDescriptor>, String>> children = node.getChildren().entrySet();
		for( Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry: children ) {
			c.put( new SerialisableModel( entry.getKey()), entry.getValue());
		}
	}
	
	protected void addChild( SerialisableModel child, String label ) {
		c.put(child, label);	
	}

	public boolean isleaf() {
		return Utils.assertNull(c);
	}

	public Map<SerialisableNode, String> getChildren() {
		return c;
	}
	
	/**
	 * Create a model from the given serialisable model
	 * @param sm
	 * @return
	 */
	public static IModelLeaf<IDescriptor> createModel( SerialisableModel sm ) {
		IDescriptor descriptor = new Descriptor();
		for( Map.Entry<String, String> entry: sm.getDescriptors().entrySet())
			descriptor.set(entry.getKey(), entry.getValue());
		IModelLeaf<IDescriptor> leaf = sm.isleaf()? new ModelLeaf<IDescriptor>( descriptor): new Model<IDescriptor>( descriptor );
		for( Map.Entry<String, String> entry: sm.getProperties().entrySet())
			leaf.set(entry.getKey(), entry.getValue());
		if(sm.isleaf()) 
			return leaf;
		IModelNode<IDescriptor> model = (IModelNode<IDescriptor>) leaf;
		for( Map.Entry<SerialisableNode, String> entry: sm.getChildren().entrySet())
			model.addChild( createModel( entry.getKey() ), entry.getValue());
		return model;
	}

	/**
	 * Serialise the leafs. note the the tree node structure is quite delicate, so 
	 * changing the settings can have adverse effects, in particular not serialising the child nodes  
	 * @param leafs
	 * @return
	 */
	public static String serialise(SerialisableModel[] models) {
		GsonBuilder builder = new GsonBuilder();
		builder.enableComplexMapKeySerialization();
		Gson gson = builder.create();
		Type tsn = new TypeToken<SerialisableModel[]>(){}.getType();
		String str = gson.toJson(models, tsn);
		return str;
	}

	/**
	 * Serialise the leafs. note the the tree node structure is quite delicate, so 
	 * changing the settings can have adverse effects, in particular not serialising the child nodes  
	 * @param leafs
	 * @return
	 */
	public static String serialise(IModelLeaf<? extends IDescriptor>[] leafs) {
		Collection<SerialisableModel> results = new ArrayList<>();
		for( IModelLeaf<? extends IDescriptor> leaf: leafs )
			results.add( new SerialisableModel( leaf ));
		return serialise( results.toArray( new SerialisableModel[ results.size()]));
	}

	public static Collection<IModelLeaf<? extends IDescriptor>> deserialise( String str ) {
		Collection<IModelLeaf<? extends IDescriptor>> results=  new ArrayList<>();
		if( StringUtils.isEmpty(str))
			return results;
		GsonBuilder builder = new GsonBuilder();
		builder.enableComplexMapKeySerialization();
		Gson gson = builder.create();
		//Type tsn = new TypeToken<SerialisableModel[]>(){}.getType();
		
		SerialisableModel[] models = gson.fromJson(str, SerialisableModel[].class); 
		if( Utils.assertNull(models))
			return results;
		for( SerialisableModel sm: models ) {
			IModelLeaf<IDescriptor> leaf = SerialisableModel.createModel(sm);
			results.add(leaf);
		}
		return results;
	}		

}
