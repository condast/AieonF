package org.aieonf.template.core;

import org.aieonf.model.template.ITemplateAieon;
import org.aieonf.model.template.ITemplateNode;

public interface ITemplate extends ITemplateNode<ITemplateAieon>
{
	public static final String S_ROOT = "Root";
	
	public static final String S_ERR_INVALID_DESCRIPTOR = "The provided descriptor should be a template aieon";
}
