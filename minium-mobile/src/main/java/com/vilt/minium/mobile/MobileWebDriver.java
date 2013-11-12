package com.vilt.minium.mobile;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;

import com.vilt.minium.mobile.impl.ConfigurationImpl;

public class MobileWebDriver<T extends MobileElements<T>>
implements WebDriver, JavascriptExecutor, HasInputDevices, TakesScreenshot {

    private WebDriver webDriver;
    private Configuration configuration;

    public MobileWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        configuration = new ConfigurationImpl();
    }

    public void get(String url) {
        webDriver.get(url);
    }

    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    public String getTitle() {
        return webDriver.getTitle();
    }

    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    public WebElement findElement(By by) {
        return webDriver.findElement(by);
    }

    public String getPageSource() {
        return webDriver.getPageSource();
    }

    public void close() {
        webDriver.close();
    }

    public void quit() {
        webDriver.quit();
    }

    public Set<String> getWindowHandles() {
        return webDriver.getWindowHandles();
    }

    public String getWindowHandle() {
        return webDriver.getWindowHandle();
    }

    public TargetLocator switchTo() {
        return webDriver.switchTo();
    }

    public Navigation navigate() {
        return webDriver.navigate();
    }

    public Options manage() {
        return webDriver.manage();
    }

    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) this.webDriver).getScreenshotAs(target);
    }

    public Keyboard getKeyboard() {
        return ((HasInputDevices) this.webDriver).getKeyboard();
    }

    public Mouse getMouse() {
        return ((HasInputDevices) this.webDriver).getMouse();
    }

    public Object executeScript(String script, Object... args) {
        throw new UnsupportedOperationException();
    }

    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) this.webDriver).executeAsyncScript(script, args);
    }
    
    public Configuration configure() {
        return configuration;
    }
}
