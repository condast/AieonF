package org.aieonf.model.constraints;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.implicit.ImplicitAieon;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Aspect extends ImplicitAieon implements IAspect
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3243066463369378315L;

	public Aspect()
	{
		super( IAspect.S_ASPECT );
	}

	public Aspect(String name)
	{
		super(IAspect.S_ASPECT );
		this.setAspect(name);
		this.setDepth(0 );
	}

	public void fill( NamedNodeMap list ) throws ConceptException{
		Node node;
		for( int i=0; i<list.getLength(); i++ ){
  		node = list.item( i );
 			String key = node.getNodeName();
 			String value = node.getTextContent();
 			if( key.equals( IAspect.Attributes.Depth.name().toLowerCase()))
 				this.setDepth( Integer.valueOf( value ));
 			else
 				super.set(key, value);
		}
	}
	
	@Override
	public String getAspect()
	{
		return super.get( IAspect.Attributes.Aspect.name() );
	}

	/**
	 * Set the aspect
	 * @param choice
	 */
	public void setAspect( String aspect )
	{
		super.set( IAspect.Attributes.Aspect.name(), aspect );
	}

	@Override
	public int getDepth()
	{
		return super.getInteger( IAspect.Attributes.Depth.name() );
	}

	/**
	 * Set the aspect depth
	 * @param depth
	 */
	public void setDepth( int depth )
	{
		super.set( IAspect.Attributes.Depth.name(), String.valueOf( depth ));
	}

	@Override
	public boolean test( IDescriptor desc)
	{
		if( desc == null )
			return false;
		if( !( desc instanceof IDescriptor ))
			return false;
		IDescriptor descriptor = desc; 
		if( !descriptor.getName().equals( this.getName() ))
			return false;
		String aspect = descriptor.get( IAspect.Attributes.Aspect.name() );
		if( Descriptor.assertNull( aspect ))
			return false;
		String thisAspect = this.getAspect();
		return thisAspect.equals( aspect );
	}

	@Override
	public boolean accept(IDescriptor descriptor)
	{
		if( descriptor.equals( this ))
			return true;
		if( !isFamily( descriptor ))
			return false;
		return this.test( descriptor );
	}
	
	@Override
	public boolean isFamily( Object descriptor)
	{
		if( !( descriptor instanceof IDescriptor ))
			return false;
		return ImplicitAieon.isFamily( this, S_ASPECT, descriptor);
	}

	@Override
	public boolean isAspectDepthReached(int depth)
	{
		if( this.getDepth() < 0 )
			return false;
		return depth >= this.getDepth();
	}	
}
