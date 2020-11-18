package test.aieonf.serialise.core;

import java.util.HashMap;
import java.util.Map;

public class TestObject {

	private long id;
	private String name;
	private boolean leaf;
	private boolean reverse;
	private Map<String, String> base;
	private Map<String, String> data;
	
	private Map<TestObject, String> children;
	
	protected TestObject(long id, String name, boolean leaf, Map<String, String> base, Map<String, String> data) {
		super();
		this.children = new HashMap<>();
		this.id = id;
		this.name = name;
		this.leaf = leaf;
		this.reverse = false;
		this.base = base;
		this.data = data;
	}
	
	public long getID() {
		return id;
	}
	public String getName() {
		return name;
	}
	public boolean isLeaf() {
		return leaf;
	}
	
	public boolean isReverse() {
		return reverse;
	}
		
	public Map<String, String> getBase() {
		return base;
	}
		
	public Map<String, String> getData() {
		return data;
	}
	
	public void addChild( TestObject child, String arg ) {
		this.children.put(child, arg);
	}
	
	public Map<TestObject, String> getChildren() {
		return children;
	}
}
