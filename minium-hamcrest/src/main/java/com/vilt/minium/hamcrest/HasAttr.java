package com.vilt.minium.hamcrest;

import static com.vilt.minium.actions.Interactions.checkNotEmpty;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.vilt.minium.CoreWebElements;

public class HasAttr<T extends CoreWebElements<T>> extends TypeSafeMatcher<T> {

	private final T elems;
	private final String key;
	private final String value;

    public HasAttr(T elems, String key, String value) {
        this.elems = elems;
		this.key = key;
		this.value = value;
    }
    
    @Override
    public boolean matchesSafely(T s) {
    	return checkNotEmpty(elems.withAttr(key, value));
    }
    
    @Override
    public void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescription.appendText("attr " + key + " was \"").appendText(item.attr(key)).appendText("\"");
    }
    
    @Override
    public void describeTo(Description description) {
        description
        	.appendText("a webelements with ")
        	.appendText(key)
        	.appendText(" attr \"")
            .appendText(value)
            .appendText("\"");
    }

    @Factory
    public static <T extends CoreWebElements<T>> Matcher<T> hasAttr(T elems, String key, String value) {
        return new HasAttr<T>(elems, key, value);
    }

}
