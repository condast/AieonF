package org.aieonf.commons;

import java.util.Date;

public interface IUpdateable {

	
	/**
	 * Update the model
	 */
	public void update();

	/**
	 * Get the create date 
	 * @return
	 */
	public Date getCreateDate();

	public void setCreateDate(Date time);

	/**
	 * Get the update date 
	 * @return
	 */
	public Date getUpdateDate();

	public void setUpdateDate(Date time);


}
