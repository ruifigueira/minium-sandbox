package com.vilt.minium.assertions;

import org.fest.assertions.error.BasicErrorMessageFactory;

import com.vilt.minium.CoreWebElements;

public class WebElementsErrorMessageFactory extends BasicErrorMessageFactory {

	private CoreWebElements<?> elements;

	public WebElementsErrorMessageFactory(CoreWebElements<?> elems, String format, Object ... arguments) {
		super(format, arguments);
		this.elements = elems;
	}
	
	public CoreWebElements<?> elements() {
		return this.elements;
	}

}
