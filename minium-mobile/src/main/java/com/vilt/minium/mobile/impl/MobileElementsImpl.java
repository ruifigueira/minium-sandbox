package com.vilt.minium.mobile.impl;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Duration;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.vilt.minium.mobile.DefaultMobileElements;
import com.vilt.minium.mobile.MobileElements;
import com.vilt.minium.mobile.MobileWebDriver;

public class MobileElementsImpl<T extends MobileElements<T>> implements MobileElements<T> {
    
    private static final Pattern PATTERN = Pattern.compile("\\[\\s*name\\s*=\\s*((?:\\S|\\S.*\\S))\\s*\\]");
    
    public static final class AllParentsIterable implements Iterable<WebElement> {
        
        private WebElement webElement;

        AllParentsIterable(WebElement webElement) {
            this.webElement = webElement;
        }
        
        @Override
        public Iterator<WebElement> iterator() {
            return new AbstractIterator<WebElement>() {
                
                private WebElement current = webElement;
                
                public WebElement computeNext() {
                    List<WebElement> parent = current.findElements(By.xpath(".."));
                    current = parent.isEmpty() ? null : parent.get(0);
                    if (current == null) endOfData();
                    return current;
                }
            };
        }
    };
    
    private static final class ContainingTextMobileElements extends DefaultMobileElements {
        private final String text;

        private ContainingTextMobileElements(MobileElementsImpl<?> parent, String text) {
            super(parent);
            this.text = text;
        }

        @Override
        public Iterator<WebElement> iterator() {
            return Iterables.filter(parent, new Predicate<WebElement>() {

                public boolean apply(WebElement input) {
                    String textInput = input.getText();
                    return textInput != null && textInput.contains(text);
                }
            }).iterator();
        }
    }
    private static final class WithNameMobileElements extends DefaultMobileElements {
        private final String name;

        private WithNameMobileElements(MobileElementsImpl<?> parent, String name) {
            super(parent);
            this.name = name;
        }
        
//        @Override
//        protected Iterable<WebElement> doApply(WebElement input) {
//            String xpath = parent.toXPath() + "[@name='" + StringEscapeUtils.escapeXml(name) + "']";
//            return input.findElements(By.xpath(xpath));
//        }

        @Override
        public Iterator<WebElement> iterator() {
            return Iterables.filter(parent, new Predicate<WebElement>() {
                public boolean apply(WebElement input) {
                    return Objects.equal(input.getAttribute("name"), name);
                }
            }).iterator();
        }
    }
    private static final class WithTextMobileElements extends DefaultMobileElements {
        private final String text;

        private WithTextMobileElements(MobileElementsImpl<?> parent, String text) {
            super(parent);
            this.text = text;
        }
        
        @Override
        protected Iterable<WebElement> doApply(WebElement input) {
            String xpath = parent.toXPath() + "[@text='" + StringEscapeUtils.escapeXml(text) + "']";
            return input.findElements(By.xpath(xpath));
        }
        
        @Override
        public Iterator<WebElement> iterator() {
            return Iterables.filter(parent, new Predicate<WebElement>() {

                public boolean apply(WebElement input) {
                    return Objects.equal(text, input.getText());
                }
            }).iterator();
        }
    }
    private static final class EqMobileElements extends DefaultMobileElements {
        private int index;

        private EqMobileElements(MobileElementsImpl<?> parent, int index) {
            super(parent);
            this.index = index;
        }

        @Override
        public Iterator<WebElement> iterator() {
            WebElement webElement = Iterables.get(parent, index, null);
            if (webElement == null) {
                return Iterators.emptyIterator();
            }
            else {
                return Iterators.singletonIterator(webElement);
            }
        }
    }
    
    private static final class FirstMobileElements extends DefaultMobileElements {
        
        private FirstMobileElements(MobileElementsImpl<?> parent) {
            super(parent);
        }
        
        @Override
        public Iterator<WebElement> iterator() {
            WebElement webElement = Iterables.getFirst(parent, null);
            if (webElement == null) {
                return Iterators.emptyIterator();
            }
            else {
                return Iterators.singletonIterator(webElement);
            }
        }
    }
    
    private static final class LastMobileElements extends DefaultMobileElements {
        
        private LastMobileElements(MobileElementsImpl<?> parent) {
            super(parent);
        }
        
        @Override
        public Iterator<WebElement> iterator() {
            WebElement webElement = Iterables.getLast(parent, null);
            if (webElement == null) {
                return Iterators.emptyIterator();
            }
            else {
                return Iterators.singletonIterator(webElement);
            }
        }
    }

    private static final class HasMobileElements extends DefaultMobileElements {
        
        private DefaultMobileElements contained;
        private List<WebElement> containedNativeElements;
        
        private HasMobileElements(MobileElementsImpl<?> parent, DefaultMobileElements contained) {
            super(parent);
            this.contained = contained;
        }
        
        @Override
        public Iterator<WebElement> iterator() {
            containedNativeElements = Arrays.asList(Iterables.toArray(contained, WebElement.class));
            return super.iterator();
        }
        
        @Override
        protected Iterable<WebElement> doApply(WebElement input) {
            for (WebElement containedNativeElement : containedNativeElements) {
                AllParentsIterable parentsToCheck = new AllParentsIterable(containedNativeElement);
                if (Iterables.contains(parentsToCheck, input)) return Collections.singletonList(input);
            }
            return Collections.emptyList();
        }
    }
    
    private static final class ByTagNameMobileElements extends DefaultMobileElements {
        private final String tagName;

        private ByTagNameMobileElements(MobileElementsImpl<?> parent, String selector) {
            super(parent);
            this.tagName = selector;
        }

        @Override
        protected Iterable<WebElement> doApply(WebElement input) {
            return input.findElements(By.tagName(tagName));
        }

        @Override
        protected Iterable<WebElement> doApply(MobileWebDriver<?> input) {
            return input.findElements(By.tagName(tagName));
        }
        
        @Override
        protected String toXPath() {
            String parentXpath = StringUtils.defaultIfBlank(parent.toXPath(), "");
            return parentXpath + "//" + tagName;
        }
    }
    
    private static final class ByNameMobileElements extends DefaultMobileElements {
        private final String finalName;

        private ByNameMobileElements(MobileElementsImpl<?> parent, String finalName) {
            super(parent);
            this.finalName = finalName;
        }

        @Override
        protected Iterable<WebElement> doApply(WebElement input) {
            return input.findElements(By.name(finalName));
        }

        @Override
        protected Iterable<WebElement> doApply(MobileWebDriver<?> input) {
            return input.findElements(By.name(finalName));
        }
    }
    
    private static final class ByIdMobileElements extends DefaultMobileElements {
        private final String id;

        private ByIdMobileElements(MobileElementsImpl<?> parent, String id) {
            super(parent);
            this.id = id;
        }

        @Override
        protected Iterable<WebElement> doApply(WebElement input) {
            return input.findElements(By.id(id));
        }

        @Override
        protected Iterable<WebElement> doApply(MobileWebDriver<?> input) {
            return input.findElements(By.id(id));
        }
    }
    
    private static class MobileElementsWait<T> extends FluentWait<T> {

        public MobileElementsWait(T input, Duration timeout, Duration interval) {
            super(input);
            withTimeout(timeout.in(MILLISECONDS), MILLISECONDS);
            pollingEvery(interval.in(MILLISECONDS), MILLISECONDS);
        }
        
        @Override
        protected RuntimeException timeoutException(String message, Throwable lasteException) {
            return new TimeoutException(message, lasteException);
        }
    }
    
    protected MobileWebDriver<?> driver;
    protected MobileElementsImpl<?> parent;

    public MobileElementsImpl(MobileWebDriver<?> driver) {
        this.driver = driver;
    }

    public MobileElementsImpl(MobileElementsImpl<?> parent) {
        this.driver = parent.driver;
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public T find(final String selector) {
        if (selector.startsWith("#")) {
            final String id = selector.substring(1);
            return (T) new ByIdMobileElements(this, id);
        } else {
            Matcher matcher = PATTERN.matcher(selector);
            if (matcher.find()) {
                String name = matcher.group(1).trim();
                if ((name.startsWith("\"") && name.endsWith("\"")) || (name.startsWith("'") && name.endsWith("'"))) {
                    name = name.substring(1, name.length() - 1);
                }
                final String finalName = name;
                return (T) new ByNameMobileElements(this, finalName); 
            }
        }
        return (T) new ByTagNameMobileElements(this, selector);
    }

    @SuppressWarnings("unchecked")
    public T eq(int index) {
        return (T) new EqMobileElements(this, index);
    }
    
    @SuppressWarnings("unchecked")
    public T first() {
        return (T) new FirstMobileElements(this);
    }
    
    @SuppressWarnings("unchecked")
    public T last() {
        return (T) new LastMobileElements(this);
    }

    @SuppressWarnings("unchecked")
    public T withText(final String text) {
        return (T) new WithTextMobileElements(this, text);
    }
    
    @SuppressWarnings("unchecked")
    public T withName(final String name) {
        return (T) new WithNameMobileElements(this, name);
    }

    @SuppressWarnings("unchecked")
    public T containingText(final String text) {
        return (T) new ContainingTextMobileElements(this, text);
    }

    public T has(String selector) {
        return has(this.find(selector));
    }

    @SuppressWarnings("unchecked")
    public T has(final T webElements) {
        return (T) new HasMobileElements(this, (DefaultMobileElements) webElements);
    }

    public Iterator<WebElement> iterator() {
        if (parent == null || parent instanceof RootMobileElements) {
            return doApply(driver).iterator();
        }
        
        return Iterables.concat(Iterables.transform(parent, new Function<WebElement, Iterable<WebElement>>() {

            public Iterable<WebElement> apply(WebElement input) {
                return doApply(input);
            }

        })).iterator();
    }

    public String text() {
        WebElement first = Iterables.getFirst(this, null);
        return first == null ? null : first.getText();
    }
    
    public String attr(String name) {
        WebElement first = Iterables.getFirst(this, null);
        return first == null ? null : first.getAttribute(name);
    }
    
    public T wait(Predicate<? super T> predicate) {     
        return this.wait((Duration) null, predicate);
    }

    public T wait(Duration timeout, Predicate<? super T> predicate) {
        return this.wait(timeout, (Duration) null, predicate);
    }
    public T wait(String preset, Predicate<? super T> predicate) {
        Duration timeout = driver.configure().waitingPreset(preset).timeout();
        Duration interval = driver.configure().waitingPreset(preset).interval();
        return this.wait(timeout, interval, predicate);
    }
    
    @SuppressWarnings("unchecked")
    protected T wait(Duration timeout, Duration interval, Predicate<? super T> predicate) {
        if (timeout == null) {
            timeout = new Duration(5, TimeUnit.SECONDS);
        }
        if (interval == null) {
            interval = new Duration(500, TimeUnit.MILLISECONDS);
        }
        
        Wait<T> wait = getWait(timeout, interval);
        
        Function<? super T, Boolean> function = Functions.forPredicate(predicate);
        wait.until(function);
        
        return (T) this;
    }

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Wait<T> getWait(Duration timeout, Duration interval) {
        return new MobileElementsWait(this, timeout, interval);
    }

    protected Iterable<WebElement> doApply(WebElement input) {
        return Collections.emptyList();
    }
    
    protected Iterable<WebElement> doApply(MobileWebDriver<?> input) {
        return Collections.emptyList();
    }
    
    protected String toXPath() {
        return null;
    }
}
