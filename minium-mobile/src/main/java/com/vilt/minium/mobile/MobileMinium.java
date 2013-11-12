package com.vilt.minium.mobile;

import com.vilt.minium.mobile.impl.RootMobileElements;

public class MobileMinium {

    @SuppressWarnings("unchecked")
    public static <T extends MobileElements<T>> T $(MobileWebDriver<T> driver) {
        return (T) new RootMobileElements(driver);
    }
    @SuppressWarnings("unchecked")
    public static <T extends MobileElements<T>> T $(MobileWebDriver<T> driver, String selector) {
        return (T) new RootMobileElements(driver).find(selector);
    }
}
