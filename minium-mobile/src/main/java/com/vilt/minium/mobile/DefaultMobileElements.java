package com.vilt.minium.mobile;

import com.vilt.minium.mobile.impl.MobileElementsImpl;

public class DefaultMobileElements extends MobileElementsImpl<DefaultMobileElements> {

    public DefaultMobileElements(MobileElementsImpl<?> parent) {
        super(parent);
    }
    public DefaultMobileElements(MobileWebDriver<?> driver) {
        super(driver);
    }

}
