package org.condast.aieonf.browsersupport.service;

import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;
import org.eclipselabs.osgi.ds.broker.service.AbstractProvider;

public class ActiveLinkProvider extends AbstractProvider<String, Object, ActiveLinkData>
{
	private static ActiveLinkProvider provider = new ActiveLinkProvider();
	
	private ActiveLinkProvider()
	{
		super( new Palaver());
	}

	public static ActiveLinkProvider getInstance(){
		return provider;
	}
	
	@Override
	protected void onDataReceived(Object datum) {
		if(!( datum instanceof String ))
			return;
		//super.update(data);
	}

	private static class Palaver extends AbstractPalaver<String>{

		private static final String S_INTRODUCTION = "org.aieonf.activelink.introduction";
		private static final String S_TOKEN = "org.aieonf.activelink.token";

		private String providedToken;

		protected Palaver() {
			super( S_INTRODUCTION );
		}

		protected Palaver( String introduction, String token ) {
			super(  introduction );
			this.providedToken = token;
		}

		@Override
		public String giveToken() {
			if( providedToken == null )
				return S_TOKEN;
			return providedToken;
		}

		@Override
		public boolean confirm(Object token) {
			boolean retval = false;
			if( providedToken == null )
				retval = S_TOKEN .equals( token );
			else
				retval = providedToken.equals(token);
			return retval;
		}		
	}
}