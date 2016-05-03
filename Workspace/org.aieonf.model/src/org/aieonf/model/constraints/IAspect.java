package org.aieonf.model.constraints;

import org.aieonf.commons.implicit.IImplicit;
import org.aieonf.concept.IDescriptor;

public interface IAspect extends IDescriptor, IImplicit<IDescriptor>
{
  public static final String S_ASPECT = "Aspect";
	
	public enum Attributes{
  	Aspect,
  	Depth,
  }

	/**
	 * returns the aspect of the descriptor. The aspect is a submodel which describes a part
	 * of the total model. for instance, if you are developing an authentication context, then 
	 * one aspect would be the 'login' and the other the 'registration'
	 * @return String
	*/
	public String getAspect();

	/**
	 * returns the depth of the aspect. By default this is zero (only the current node), and a negative value
	 * stands for a complete submodel. An aspect is a submodel which describes a part
	 * of the total model. for instance, if you are developing an authentication context, then 
	 * one aspect would be the 'login' and the other the 'registration'. Typically a 'login' aspect will have
	 * depth zero, because all that is required is a user name and password. The registration is more involved, because
	 * it includes a model of the personal details.
	 * @return int
	*/
	public int getDepth();
	
	/**
	 * returns true if the given depth is equal or larger than the aspect depth
	 * @param depth
	 * @return
	 */
	public boolean isAspectDepthReached( int depth );
}
