package com.vilt.minium.mobile;

import org.openqa.selenium.WebElement;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class MobileInterationPerformer {
    private String preset;

    public MobileInterationPerformer(String preset) {
        this.preset = preset;
    }
    
    public void click(DefaultMobileElements webElements) {
        first(webElements).click();
    }
    
    public void fill(DefaultMobileElements webElements, String text) {
        WebElement webElement = first(webElements);
        webElement.clear();
        webElement.sendKeys(text);
    }
    
    public void waitWhileEmpty(DefaultMobileElements webElements) {
        getNotEmptyElements(webElements);
    }
    
    public void waitWhileNotEmpty(DefaultMobileElements webElements) {
        if (preset == null) {
            webElements.wait(empty());
        }
        else {
            webElements.wait(preset, empty());
        }
    }

    private DefaultMobileElements getNotEmptyElements(DefaultMobileElements webElements) {
        if (preset == null) {
            return webElements.wait(notEmpty());
        }
        else {
            return webElements.wait(preset, notEmpty());
        }
    }

    private WebElement first(DefaultMobileElements webElements) {
        getNotEmptyElements(webElements);
        return Iterables.getFirst(webElements, null);
    }

    private Predicate<? super DefaultMobileElements> notEmpty() {
        return new Predicate<DefaultMobileElements>() {

            public boolean apply(DefaultMobileElements input) {
                return !Iterables.isEmpty(input);
            }
        };
    }
    
    private Predicate<? super DefaultMobileElements> empty() {
        return new Predicate<DefaultMobileElements>() {
            
            public boolean apply(DefaultMobileElements input) {
                return Iterables.isEmpty(input);
            }
        };
    }
}
