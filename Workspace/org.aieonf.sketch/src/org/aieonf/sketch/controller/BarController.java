package org.aieonf.sketch.controller;

import org.aieonf.commons.db.IDatabaseConnection;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.request.AbstractKeyEventService;
import org.aieonf.sketch.core.Dispatcher;

public class BarController extends AbstractKeyEventService<IDatabaseConnection.Requests>
{	
	public enum Attributes{
		MODEL_ID,
		CATEGORY,
		WILDCARD;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString( super.name());
		}
	}

	private Dispatcher dispatcher = Dispatcher.getInstance();	
	
	private BarController() {
		super( Dispatcher.getInstance().getActiveDomain());
		//super.addListener(dispatcher);
	}

	private static BarController service = new BarController();
	
	/**
	 * Get an instance of the bar link service
	 * @return
	 */
	public static BarController getInstance(){
		return service;
	}

	public void prepare(){
		super.setDomain(dispatcher.getActiveDomain());
		super.prepare(IDatabaseConnection.Requests.PREPARE);
	}
}
