package org.aieonf.concept.body;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public abstract class AbstractBodyAieon implements IBodyAieon
{
  static final long serialVersionUID = 1L;

	public AbstractBodyAieon(){
  }


  /**
   * The body interface defines a body class that is used to create the body
   *
   * @return Class
   */
  @Override
	public Class<?> getBodyClass(){
    return this.getClass();
  }

  /**
   * Allow cloning of the aieon
   * @return Object
  */
  @Override
	public abstract Object clone();
}
