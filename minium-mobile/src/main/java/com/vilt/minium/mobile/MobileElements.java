package com.vilt.minium.mobile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Duration;

import com.google.common.base.Predicate;

public interface MobileElements<T extends MobileElements<T>> extends Iterable<WebElement> {
    public T find(String selector);
    public T eq(int index);
    public T first();
    public T last();
    public T withText(String text);
    public T withName(String name);
    public T containingText(String text);
    public T has(String selector);
    public T has(T webElements);
    
    public String text();
    public String attr(String name);
    
    public T wait(Predicate<? super T> predicate);   
    public T wait(Duration timeout, Predicate<? super T> predicate);
    public T wait(String preset, Predicate<? super T> predicate);
}
