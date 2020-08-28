package org.aieonf.commons.xml;

public interface IXMLBuilder<T extends Object> {

	/**
	 * Returns true if the url points to a valid resource
	 * @return
	 */
	boolean canCreate();

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#build()
	 */
	void build();

	/**
	 * Get all the units that were built
	 * @return
	 */
	public T[] getUnits();
	
	/**
	 * Get the unit with the given id.
	 * @param id
	 * @return
	 */
	public T getParsedUnit(String id);

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#isCompleted()
	 */
	boolean isCompleted();

	boolean hasFailed();

}