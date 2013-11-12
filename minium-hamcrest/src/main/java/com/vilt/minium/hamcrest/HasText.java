package com.vilt.minium.hamcrest;

import static com.vilt.minium.actions.Interactions.checkNotEmpty;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.vilt.minium.CoreWebElements;

public class HasText<T extends CoreWebElements<T>> extends TypeSafeMatcher<T> {

	private final T elems;
	private String text;

    public HasText(T elems, String text) {
        this.elems = elems;
		this.text = text;
    }
    
    @Override
    public boolean matchesSafely(T s) {
    	return checkNotEmpty(elems.withText(text));
    }
    
    @Override
    public void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescription.appendText("text was \"").appendText(item.text()).appendText("\"");
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("a webelements with text \"")
                   .appendText(text)
                   .appendText("\"");
    }

    @Factory
    public static <T extends CoreWebElements<T>> Matcher<T> hasText(T elems, String text) {
        return new HasText<T>(elems, text);
    }

}
