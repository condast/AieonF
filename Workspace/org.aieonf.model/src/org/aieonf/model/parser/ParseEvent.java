package org.aieonf.model.parser;

import java.util.EventObject;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public class ParseEvent<D extends IDescriptor, M extends IDescribable<D>> extends EventObject {
	private static final long serialVersionUID = -1266257260044093122L;

	private M model;
	
	private String type;
	
	private String key, value;
	
	private IModelParser.Status status; 

	public ParseEvent( Object source, M model, String type, IModelParser.Status status) {
		this( source, model, type, null, null, status );
	}

	public ParseEvent(Object source, M model, String type, String key, String value ) {
		this( source, model, type, key, value, IModelParser.Status.BUSY );
	}

	public ParseEvent(Object source, M model, String type, String key, String value, IModelParser.Status status ) {
		super(source);
		this.type = type;
		this.model = model;
		this.key = value;
		this.status = status;
	}

	public M getModel() {
		return model;
	}

	public String getType() {
		return type;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public IModelParser.Status getStatus() {
		return status;
	}
}
