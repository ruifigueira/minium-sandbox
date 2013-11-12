package com.vilt.minium.hamcrest;

import static com.vilt.minium.actions.Interactions.checkNotEmpty;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.vilt.minium.CoreWebElements;

public class NotEmpty<T extends CoreWebElements<T>> extends TypeSafeMatcher<T> {

	private final T elems;

    public NotEmpty(T elems) {
        this.elems = elems;
    }
    
    @Override
    public boolean matchesSafely(T s) {
    	return checkNotEmpty(elems);
    }
    
    @Override
    public void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescription.appendText("was \"").appendText(item.toString()).appendText("\"");
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("a webelements ")
                   .appendText(elems.toString())
                   .appendText(" not empty");
    }

    @Factory
    public static <T extends CoreWebElements<T>> Matcher<T> notEmpty(T elems) {
        return new NotEmpty<T>(elems);
    }

}
