package org.aieonf.model.seriliasable;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.serialise.IDeserialise;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.Model;

import com.google.gson.Gson;

public class ModelLeafDeserialise implements IDeserialise<IModelLeaf<? extends IDescriptor>> {

	private ModelLeafDeserialise() {
	}

	@Override
	public IModelLeaf<? extends IDescriptor> deserialise(String str) {
		Gson gson = new Gson();
		IModelLeaf<? extends IDescriptor> leaf = gson.fromJson(str, SerialisableModel.class);
		return leaf;
	}

	public static IModelLeaf<? extends IDescriptor> create( String str ) {
		ModelLeafDeserialise cs = new ModelLeafDeserialise();
		return cs.deserialise(str);
	}

	public static <D extends IDescriptor, M extends IModelLeaf<D>> String serialise( M leaf) {
		Gson gson = new Gson();
		String str = gson.toJson(leaf, SModel.class);
		return str;
	}

	private class SModel extends Model<Descriptor>{

		@SuppressWarnings("unused")
		public SModel( String id) {
			super( id );
		}
	}

}
