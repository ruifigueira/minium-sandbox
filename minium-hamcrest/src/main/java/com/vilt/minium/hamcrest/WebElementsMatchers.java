package com.vilt.minium.hamcrest;

import org.hamcrest.Matcher;

import com.vilt.minium.CoreWebElements;

public class WebElementsMatchers {

	public static <T extends CoreWebElements<T>> Matcher<T> notEmpty(T elems) {
		return NotEmpty.<T> notEmpty(elems);
	}

	public static <T extends CoreWebElements<T>> Matcher<T> hasText(T elems, String text) {
		return HasText.<T> hasText(elems, text);
	}

	public static <T extends CoreWebElements<T>> Matcher<T> hasAttr(T elems, String key, String value) {
		return HasAttr.<T> hasAttr(elems, key, value);
	}
}
