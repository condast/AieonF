package org.aieonf.model.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public class DescribableParser<D extends IDescriptor, M extends IDescribable> implements IModelParser<D, M>
{
	private String type;
	
	private Collection<IParserListener<D,M>> listeners;
	
	public DescribableParser( String type ){
		this.type = type;
		this.listeners = new ArrayList<>();
	}

	protected String getType() {
		return type;
	}

	@Override
	public void addListener(IParserListener<D,M> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(IParserListener<D,M> listener) {
		listeners.remove(listener);
	}

	protected void notifyListeners( ParseEvent<D,M> event ) {
		for( IParserListener<D,M> listener: this.listeners )
			listener.notifyChange(event);		
	}

	protected boolean parseData(M model){
		IDescriptor descriptor = model.getDescriptor();
		Iterator<Map.Entry<String, String>> iterator = descriptor.entrySet().iterator();
		while( iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			notifyListeners( new ParseEvent<D,M>(this, model, type, entry.getKey(), entry.getValue()));
		}
		return true;
	}

	@Override
	public boolean parseModel(M model){
		notifyListeners( new ParseEvent<D,M>(this, model, type, IModelParser.Status.PREPARE ));
		parseData(model);
		notifyListeners( new ParseEvent<D,M>(this, model, type, IModelParser.Status.COMPLETED ));
		return true;
	}
}