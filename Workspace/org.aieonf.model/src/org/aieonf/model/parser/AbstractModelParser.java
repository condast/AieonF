package org.aieonf.model.parser;

import java.util.Iterator;
import java.util.Map;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

public abstract class AbstractModelParser<D extends IDescriptor, M extends IModelLeaf<D>> extends DescribableParser<D,M> implements IModelParser<D, M>
{	
	public enum ParseTypes{
		DESCRIPTOR,
		MODEL;
	}

	private IParserListener<D,M> listener = new IParserListener<D,M>() {

		@Override
		public void notifyChange(ParseEvent<D, M> event) {
			ParseTypes type = ParseTypes.valueOf(event.getType());
			switch(event.getStatus()) {
			case PREPARE:
				result &= onPrepare(type, event);
				break;
			case BUSY:
				result &= onBusy(type, event, event.getKey(), event.getValue());
				break;
			case COMPLETED:
				result &= onComplete(type, event);
				break;
			}

		}
	};
	
	private M root;
	
	private boolean result;

	protected DescribableParser<D, IDescribable> descriptorParser;
		
	protected AbstractModelParser(){
		super( ParseTypes.DESCRIPTOR.name());
		this.result = false;
	}

	protected M getRoot() {
		return root;
	}
	
	protected abstract boolean onPrepare( ParseTypes type, ParseEvent<D, M> event );
	protected abstract boolean onBusy( ParseTypes type, ParseEvent<D, M> event, String key, String value );
	protected abstract boolean onComplete( ParseTypes type, ParseEvent<D, M> event );
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean parseModel(M model){
		this.root = model;
		this.result = true;
		super.addListener(listener);
		super.parseModel(model);//first parse the descriptor
		String type = ParseTypes.MODEL.name();
		notifyListeners( new ParseEvent<D,M>(this, model, type, IModelParser.Status.PREPARE ));
		Iterator<Map.Entry<String, String>> iterator = model.entrySet().iterator();
		while( result & iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			notifyListeners( new ParseEvent<D,M>(this, model, type, entry.getKey(), entry.getValue()));
		}
		notifyListeners( new ParseEvent<D,M>( this, model, type, IModelParser.Status.COMPLETED ));
		if( !result || model.isLeaf())
			return result;
		IModelNode<D> mn = (IModelNode<D>) model; 
		for( IModelLeaf<? extends IDescriptor> child: mn.getChildren().keySet()) {
			M mc = (M) child;
			result = parseModel( mc );
		}
		super.removeListener(listener);
		return result;
	}
}