package org.aieonf.model.seriliasable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.core.ConceptBase;

public abstract class AbstractDescribableAdapter<S extends IDescribable> extends TypeAdapter<S>{

	protected AbstractDescribableAdapter() {}

	   @Override 
	   public S read(JsonReader reader) throws IOException { 
		     ConceptBase base = new ConceptBase(); 
		      reader.beginObject(); 
		      String fieldName = null;
		      while (reader.hasNext()) { 
		         JsonToken token = reader.peek();            
		         if (token.equals(JsonToken.NAME)) {     
		             fieldName = reader.nextName(); 
		             base.set(fieldName, reader.nextString());
		          } 
		      } 
		      reader.endObject(); 
		      return null;//describable; 
	   } 
	   
	   @Override 
	   public void write(JsonWriter writer, S describable) throws IOException { 
		   Iterator<Map.Entry<String, String>> iterator = describable.getDescriptor().iterator();
		   while( iterator.hasNext() ) {
			   Map.Entry<String, String> entry = iterator.next();
			   writer.beginObject(); 
			   writer.name(entry.getKey()); 
			   writer.value( entry.getValue()); 
			   writer.endObject(); 
		   }
	   } 

/*
	   public S deserialise( ConceptBaseTypeAdapter<S> adapter, String str, Class<S> clss ) {
		   GsonBuilder builder = new GsonBuilder(); 
		builder.registerTypeAdapter(clss, adapter); 
		Gson gson = builder.create(); 
		S result = gson.fromJson( str, clss); 
		return result;
	}
//	*/
}
