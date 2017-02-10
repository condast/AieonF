package org.aieonf.sketch.factory;

public class SelectedFactory {

	private static SelectedFactory selected = new SelectedFactory();
	
	private SketchModelFactory factory; 
	
	private SelectedFactory() {}
	
	public static SelectedFactory getInstance(){
		return selected;
	}

	public synchronized SketchModelFactory getFactory() {
		return factory;
	}

	public synchronized void setFactory(SketchModelFactory factory) {
		this.factory = factory;
	}
}
