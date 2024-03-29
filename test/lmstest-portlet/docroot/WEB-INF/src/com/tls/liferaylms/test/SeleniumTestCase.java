package com.tls.liferaylms.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.tls.liferaylms.test.util.Context;
import com.tls.liferaylms.test.util.Sleep;

public class SeleniumTestCase {
	protected WebDriver driver;
	protected String baseUrl;
	private Log log = null;
	private boolean acceptNextAlert = true;

	@Before
	public void setUp() throws Exception {
		driver = SeleniumDriverUtil.getDriver();
		baseUrl = Context.getBaseUrl();	
	}

	@After
	public void tearDown() throws Exception {

	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} catch (ElementNotVisibleException e) {
			return false;
		} catch (ElementNotFoundException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public WebElement getElement(By by){
		try {
//			Sleep.waitFor(by, driver);
			return driver.findElement(by);
		} catch(TimeoutException e){
			return driver.findElement(by);
		} catch (NoSuchElementException e) {
			return null;
		} catch (ElementNotVisibleException e) {
			return null;
		} catch (ElementNotFoundException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	public WebElement getElement(WebElement we,By by){
		try {
//			Sleep.waitFor(by, driver);
			return we.findElement(by);
		} catch(TimeoutException e){
			return we.findElement(by);
		} catch (NoSuchElementException e) {
			return null;
		} catch (ElementNotVisibleException e) {
			return null;
		} catch (ElementNotFoundException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<WebElement> getElements(WebElement we,By by){
		try {
			return we.findElements(by);
		} catch (NoSuchElementException e) {
			return null;
		} catch (ElementNotVisibleException e) {
			return null;
		} catch (ElementNotFoundException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public List<WebElement> getElements(By by){
		try {
			return driver.findElements(by);
		} catch (NoSuchElementException e) {
			return null;
		} catch (ElementNotVisibleException e) {
			return null;
		} catch (ElementNotFoundException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	public String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
	
	public Log getLog(){
		if(log==null){
			log = LogFactoryUtil.getLog(this.getClass());
		}
		return log;
	}
	
	/** Get the current line number.
	 * @return String - Current line number.
	 */
	public String getLineNumber() {
	    return ". At line ".concat(String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
	}
}
