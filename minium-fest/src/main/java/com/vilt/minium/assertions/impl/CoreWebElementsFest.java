package com.vilt.minium.assertions.impl;

import org.apache.commons.lang3.StringUtils;
import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Failures;
import org.fest.assertions.internal.Objects;
import org.fest.util.VisibleForTesting;

import com.vilt.minium.CoreWebElements;
import com.vilt.minium.assertions.WebElementsErrorMessageFactory;

public class CoreWebElementsFest {

	private static final CoreWebElementsFest INSTANCE = new CoreWebElementsFest();
	
	@VisibleForTesting
	Failures failures = Failures.instance();

	public static CoreWebElementsFest instance() {
		return INSTANCE;
	}

	protected CoreWebElementsFest() {
	}
	
	public void assertNullOrEmpty(AssertionInfo info, CoreWebElements<?> actual) {
		if (actual == null) return;
		
		int size = actual.size();
		if (size == 0) return;
		
		throw failures.failure(info, new WebElementsErrorMessageFactory(actual, "expecting empty but has size:<%s>", size));
	}
	
	public void assertNotEmpty(AssertionInfo info, CoreWebElements<?> actual) {
		assertNotNull(info, actual);
		
		int size = actual.size();
		if (size != 0) return;
		
		throw failures.failure(info, new WebElementsErrorMessageFactory(actual, "expecting not empty but is empty"));
	}
	
	public void assertHasVal(AssertionInfo info, CoreWebElements<?> actual, String expected) {
		assertNotEmpty(info, actual);

		String val = actual.val();
		if (StringUtils.equals(val, expected)) return;
		throw this.failures.failure(info, new WebElementsErrorMessageFactory(actual, "expecting val:<%s> to be <%s>", val, expected));
	}
	
	public void assertHasText(AssertionInfo info, CoreWebElements<?> actual, String expected) {
		assertNotEmpty(info, actual);
		
		String val = actual.text();
		if (StringUtils.equals(val, expected)) return;
		this.failures.failure(info, new WebElementsErrorMessageFactory(actual, "expecting text:<%s> to be <%s>", val, expected));
	}
	
	public void assertHasHtml(AssertionInfo info, CoreWebElements<?> actual, String expected) {
		assertNotEmpty(info, actual);
		
		String val = actual.html();
		if (StringUtils.equals(val, expected)) return;
		this.failures.failure(info, new WebElementsErrorMessageFactory(actual, "expecting html:<%s> to be <%s>", val, expected));
	}

	public void assertHasName(AssertionInfo info, CoreWebElements<?> actual, String expected) {
		assertNotEmpty(info, actual);
		
		String val = actual.attr("name");
		if (StringUtils.equals(val, expected)) return;
		this.failures.failure(info, new WebElementsErrorMessageFactory(actual, "expecting name:<%s> to be <%s>", val, expected));
	}
	
	public void assertHasAttr(AssertionInfo info, CoreWebElements<?> actual, String attr, String expected) {
		assertNotEmpty(info, actual);
		
	}

	private void assertNotNull(AssertionInfo info, CoreWebElements<?> actual) {
		Objects.instance().assertNotNull(info, actual);
	}
}
