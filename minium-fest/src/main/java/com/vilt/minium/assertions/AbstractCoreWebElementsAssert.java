package com.vilt.minium.assertions;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.error.BasicErrorMessageFactory;
import org.fest.assertions.error.ErrorMessageFactory;

import com.vilt.minium.CoreWebElements;
import com.vilt.minium.assertions.impl.CoreWebElementsFest;

public class AbstractCoreWebElementsAssert<A extends AbstractCoreWebElementsAssert<A, S>, S extends CoreWebElements<?>> extends AbstractAssert<A, S> {

	protected WritableAssertionInfo info = new WritableAssertionInfo();

	CoreWebElementsFest fest = CoreWebElementsFest.instance();
	
	protected AbstractCoreWebElementsAssert(S actual, Class<?> selfType) {
		super(actual, selfType);
	}
	
	public void isEmpty() {
		fest.assertNullOrEmpty(info, actual);	
	}
	
	public A isNotEmpty() {
		fest.assertNotEmpty(info, actual);	
		return myself;
	}
	
	public A hasVal(String expected) {
		fest.assertHasVal(info, actual, expected);
		return myself;
	}

	public A hasText(String text) {
		fest.assertHasText(info, actual, text);
		return myself;
	}
	
	public A hasHtml(String html) {
		fest.assertHasHtml(info, actual, html);
		return myself;
	}
	
	
	public A hasName(String name) {
		fest.assertHasName(info, actual, name);
		return myself;
	}

	public A hasAttr(String attr, String value) {
		fest.assertHasAttr(info, actual, attr, value);
		return myself;
	}
	
	protected ErrorMessageFactory shouldHaveVal(S actual, String val, String expected) {
		return new BasicErrorMessageFactory("val:<%s> but should be:<%s>", val, expected);
	}
}
