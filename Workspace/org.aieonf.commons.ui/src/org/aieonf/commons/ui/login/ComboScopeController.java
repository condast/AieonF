package org.aieonf.commons.ui.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IConcept.Scope;
import org.eclipse.swt.widgets.Combo;

public class ComboScopeController {

	private Combo combo;
	
	private Collection<IConcept.Scope> scopes;
	
	public ComboScopeController( Combo combo) {
		this.combo = combo;
		scopes = EnumSet.of(Scope.PUBLIC);
		combo.add( Scope.PUBLIC.toString());
		combo.select(0);
	}

	public void setUser(ILoginUser user) {
		if( user != null ) {
			scopes = EnumSet.allOf(IConcept.Scope.class);
		}else
			scopes = EnumSet.of(Scope.PUBLIC);
		combo.removeAll();
		for( Scope scope: scopes )
			combo.add( scope.toString());
		combo.select(0);
	}

	public Scope getSelected() {
		List<Scope> list = new ArrayList<>( scopes);
		if( combo.getSelectionIndex() < 0)
			
			return Scope.PUBLIC;
		return list.get(combo.getSelectionIndex());
	}
	
	public void select( Scope scope ) {
		List<Scope> list = new ArrayList<>( scopes);
		combo.select( list.indexOf(scope));
	}	
}
