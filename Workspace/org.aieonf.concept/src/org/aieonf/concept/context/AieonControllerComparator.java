package org.aieonf.concept.context;

import java.util.Comparator;

/**
 * The comparator compares the order of the request listeners
 * @author Kees Pieters
 *
 * @param <T>
*/
public class AieonControllerComparator<T extends IAieonController> 
	implements Comparator<T> 
{

	/**
	 * Compare the order of the request listeners
	 * @param listener1
	 * @param listener2
	 * @return
	*/
	@Override
	public int compare( T listener1, T listener2) 
	{
		return -1;//listener1.compareTo( listener2 );
/*
		int order1 = listener1.getOrder();
		int order2 = listener2.getOrder();
		if(( order1 < 0 ) && ( order2 < 0 ))
			return order2 - order1;
		if(( order1 < 0 ) || ( order2 < 0 ))
			return order1;
		return order1 - order2;
		*/
	}

}
