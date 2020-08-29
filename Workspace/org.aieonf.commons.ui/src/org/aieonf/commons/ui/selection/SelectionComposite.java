package org.aieonf.commons.ui.selection;

import org.aieonf.commons.Utils;
import org.aieonf.concept.domain.DomainEvent;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.domain.IDomainListener;
import org.aieonf.concept.domain.IDomainSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;

public class SelectionComposite extends Composite{
	private static final long serialVersionUID = -5732294353764094623L;

	private ExpandItem item;
	private List list;
	private IDomainAieon[] domains;
	
	private IDomainSelection domainSelection;
	
	private IDomainListener listener = new IDomainListener() {
		
		@Override
		public void notifyDomainChange( DomainEvent event) {
			if( IDomainSelection.SelectionEvents.SELECT_DOMAIN.equals(event.getType()))
				return;
			getDisplay().asyncExec( new Runnable() {

				@Override
				public void run() {
					setDomain();
				}			
			});
			
		}
	};
	
	private Display display;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SelectionComposite( ExpandBar bar, int style)
	{
		super(bar, style);
		this.createComposite(bar, style);
	}
	
	private final void createComposite( ExpandBar bar, int style) { 
		display = bar.getDisplay();
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		item = new ExpandItem( bar, SWT.NONE );
		item.setHeight( computeSize( SWT.DEFAULT, SWT.DEFAULT ).y);
		item.setControl( this );

		list = new List(this, SWT.SINGLE );
		list.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;
					
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				item.setText(list.getSelection()[0]);
				item.setExpanded( false );
				IDomainAieon selected = domains[ list.getSelectionIndex()];
				domainSelection.setActiveDomain(null, selected.getDomain());
				super.widgetDefaultSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				item.setText(list.getSelection()[0]);
				item.setExpanded( false );
				IDomainAieon selected = domains[ list.getSelectionIndex()];
				domainSelection.setActiveDomain(null, selected.getDomain());
				super.widgetSelected(e);
			}
		});
		String[] selection = list.getSelection();
		if( !( selection == null ) && ( selection.length > 0 ))
			item.setText(selection[0]);
		
		bar.pack();
		bar.addListener(SWT.Resize, new Listener() {
		private static final long serialVersionUID = 1L;

			public void handleEvent(Event event) {
				/* 
				 * The following is done asynchronously to allow the Text's width
				 * to be changed before re-calculating its preferred height. 
				 */
				display.asyncExec(new Runnable() {
					public void run() {
						if (list.isDisposed()) return;
						Point size = list.computeSize(list.getSize().x, SWT.DEFAULT);
						if (item.getHeight() != size.y) {
							item.setHeight(size.y);
						}
					}
				});
			}
		});		
	}

	public void setInput( IDomainSelection domainSelection ) {
		if(( this.domainSelection != null ) && !this.domainSelection.equals(domainSelection))
			domainSelection.removeListener(listener);
		this.domainSelection = domainSelection;
		if( this.domainSelection == null )
			return;
		domainSelection.addListener(listener);
		setDomain();
	}

	protected void setDomain() {
		domains = domainSelection.getDomains();
		list.removeAll();
		if( Utils.assertNull(domains))
			return;
		String[] names=  new String[ domains.length ];
		int index = 0;
		for( int i=0; i<domains.length; i++ ) {
			names[i] = domains[i].getShortName();
			if( domains[i].equals( domainSelection.getActiveDomain()))
				index = i;
		}
		list.setItems(names );	
		list.select(0);
		item.setText( names[index]);
		item.setExpanded( false );
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void dispose() {
		domainSelection.removeListener(listener);
		super.dispose();
	}
}
