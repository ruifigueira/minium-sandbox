package com.vilt.minium.mobile;

import org.openqa.selenium.WebDriver;

public class DefaultMobileWebDriver extends MobileWebDriver<DefaultMobileElements> {

    public DefaultMobileWebDriver(WebDriver webDriver) {
        super(webDriver);
    }
}
