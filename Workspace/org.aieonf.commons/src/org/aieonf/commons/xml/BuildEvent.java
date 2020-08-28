package org.aieonf.commons.xml;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.xml.sax.Attributes;

public class BuildEvent<D extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private String name, id;
	private String event;
	private D data;
	private Map<String, String> attributes;

	public BuildEvent(Object arg0, String id, String name, String event ) {
		this( arg0, id, name, event, new HashMap<String, String>(), null);
	}

	public BuildEvent(Object arg0, String name, String event, D data ) {
		this( arg0, null, name, event, new HashMap<String, String>(), data);
	}

	public BuildEvent(Object arg0, String id, String name, String event, Attributes attrs, D data ) {
		this( arg0, id, name, event, getParameters(attrs), data );
	}

	public BuildEvent(Object arg0, String id, String name, String event, Map<String, String> attrs, D data ) {
		super(arg0);
		this.name = name;
		this.id = id;
		this.event = event;
		this.attributes = attrs;
		this.data = data;
	}

	public String getEvent() {
		return event;
	}
	
	protected void setEvent(String event) {
		this.event = event;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	/**
	 * Get the attribute value for the given enum
	 * @param attributes
	 * @param enm
	 * @return
	 */
	public String getAttribute( Enum<?> enm ){
		if(( attributes == null ) ||( attributes.size() == 0 ))
			return null;
		String str = StringStyler.xmlStyleString( enm.name() );
		return attributes.get( str);
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public D getData() {
		return data;
	}
	
	protected void setData(D data) {
		this.data = data;
	}

	public static Map<String, String> getParameters( Attributes attrs){
		Map<String,String> attributes = new HashMap<String,String>();
		if( attrs == null )
			return attributes;
		for( int i =0; i< attrs.getLength(); i++ ) {
			String attr = attrs.getQName(i);
			if( StringUtils.isEmpty(attr.trim()))
				continue;
			attributes.put( attr, attrs.getValue(attr));
		}
		return attributes;
	}

}
