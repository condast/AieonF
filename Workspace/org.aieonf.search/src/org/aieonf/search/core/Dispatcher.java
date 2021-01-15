package org.aieonf.search.core;

import java.util.Map.Entry;

import org.aieonf.commons.security.ISecureGenerator;

public class Dispatcher implements ISecureGenerator{

	private static Dispatcher dispatcher = new Dispatcher();

	private ISecureGenerator generator;

	/**
	 * Get an instance of the bar link service
	 * @return
	 */
	public static Dispatcher getInstance(){
		return dispatcher;
	}

	public ISecureGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(ISecureGenerator generator) {
		this.generator = generator;
	}
	
	@Override
	public Entry<Long, Long> createIdAndToken(String domain) {
		return this.generator.createIdAndToken(domain);
	}

}
