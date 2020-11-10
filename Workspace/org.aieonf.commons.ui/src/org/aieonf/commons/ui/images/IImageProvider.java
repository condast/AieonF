package org.aieonf.commons.ui.images;

import org.eclipse.swt.graphics.Image;

public interface IImageProvider {

	/**
	 * Get the image with the given 
	 * @param identifier
	 * @return
	 */
	Image getImageFromName(String identifier);

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	Image getSizedImage(Enum<?> identifier);

	void dispose();

}