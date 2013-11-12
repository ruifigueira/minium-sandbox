package com.vilt.minium.assets;

import static com.vilt.minium.actions.Interactions.get;
import static com.vilt.minium.assertions.MINIUM.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.vilt.minium.DefaultWebElements;
import com.vilt.minium.DefaultWebElementsDriver;

public class MiniumTest {

	DefaultWebElementsDriver wd;
	
	@BeforeTest
	public void visitGoogle() {
		wd = new DefaultWebElementsDriver(new ChromeDriver());
		get(wd, "http://www.google.com/ncr");
	}

	@AfterTest
	public void close() {
		wd.quit();
	}
	
	@Test
	public void testHasVal() {
		DefaultWebElements webElems = wd.find(":text").withName("q");
		assertThat(webElems).hasVal("foo").hasAttr("href", "google.com");
	}
}
