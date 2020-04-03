package org.aieonf.orientdb.filter;

import java.util.Collection;

import org.aieonf.commons.filter.IFilter;

import com.tinkerpop.blueprints.Vertex;

public interface IGraphFilter extends IFilter<Vertex>{

	Collection<Vertex> doFilter();

}