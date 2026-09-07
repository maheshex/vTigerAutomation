package org.eva.application.utils;


import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import org.testng.Assert;

/**
 * SeleniumWrapper - A comprehensive utility class for Selenium WebDriver operations
 * with robust exception handling and utility methods for web automation
 * 
 * Features:
 * - Comprehensive exception handling for all Selenium operations
 * - JavaScript fallbacks for problematic interactions
 * - Explicit wait utilities for various conditions
 * - Action class utilities for advanced interactions
 * - Validation methods for element states
 * - Window and frame handling methods
 * - Select dropdown utilities
 */
public class WebUtils {


	Logger logger=	LogManager.getLogger(WebUtils.class);





	private WebDriver driver;
	private ExtentTest extentTest;

	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Constructor to initialize WebDriver and related utilities
	 * @param webUtil WebDriver instance
	 * @param timeoutSeconds Default timeout in seconds for explicit waits
	 * @throws IllegalAccessException 
	 */
	//    public WebUtil() {
	//        this.driver = new ChromeDriver();
	//    }

	public void setExtentTest(ExtentTest exTest) {
		extentTest = exTest;
	}

	public void launchBrowser(String browserName) throws IllegalAccessException {
		if (browserName.equalsIgnoreCase("firefox")) {
			driver =new FirefoxDriver();
			logger.info("Browser Launched Successfully");
			extentTest.log(Status.INFO, "Fire Fox Browser has been launched.");
			

		}else if (browserName.equalsIgnoreCase("edge")) {
			driver =new EdgeDriver();
			extentTest.log(Status.INFO, "Edge Browser has been launched.");

		}else if (browserName.equalsIgnoreCase("chrome")) {
			driver =new ChromeDriver();
			extentTest.log(Status.INFO, "Chrme Browser has been launched.");

		}else {
			throw new IllegalAccessException("Unsupported Browser"+ browserName);

		}
		driver.manage().window().maximize();
	}

	public void openURL(String url) {
		driver.get(url);;
	}

	public void closeBrowser() {
		if(driver !=null) {
			driver.quit();
		}
	}

	/**
	 * Gets WebElement with robust exception handling for findElement operations
	 * @param xpath XPath string to locate the element
	 * @return WebElement if found
	 * @throws RuntimeException if element cannot be located with specific error details
	 */
	private WebElement getElement(String xpath) {
		try {
			extentTest.log(Status.INFO,"Attempting to find element with XPath: " + xpath);
			return driver.findElement(By.xpath(xpath));
		} catch (InvalidSelectorException e) {
			String errorMsg = "Invalid XPath syntax: " + xpath;
			//System.out.println("ERROR: " + errorMsg);
			extentTest.log(Status.FAIL,"ERROR: " + errorMsg);
			throw new RuntimeException(errorMsg, e);
		} catch (NoSuchElementException e) {
			String errorMsg = "Element not found with XPath: " + xpath;
			//System.out.println("ERROR: " + errorMsg);
			extentTest.log(Status.FAIL,"ERROR: " + errorMsg);
			throw new RuntimeException(errorMsg, e);
		} catch (Exception e) {
			String errorMsg = "Unexpected exception while finding element with XPath: " + xpath;
			//System.out.println("ERROR: " + errorMsg + " - " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: " + errorMsg + " - " + e.getMessage());
			throw new RuntimeException(errorMsg, e);
		}
	}

	/**
	 * Gets multiple WebElements with exception handling
	 * @param xpath XPath string to locate the elements
	 * @return List of WebElements if found
	 * @throws RuntimeException if elements cannot be located
	 */
	private List<WebElement> getElements(String xpath) {
		try {
			//System.out.println("Attempting to find elements with XPath: " + xpath);
			extentTest.log(Status.INFO,"Attempting to find elements with XPath: " + xpath);
			return driver.findElements(By.xpath(xpath));
		} catch (InvalidSelectorException e) {
			String errorMsg = "Invalid XPath syntax: " + xpath;
			//System.out.println("ERROR: " + errorMsg);
			extentTest.log(Status.FAIL, "ERROR: " + errorMsg);
			throw new RuntimeException(errorMsg, e);
		} catch (Exception e) {
			String errorMsg = "Unexpected exception while finding elements with XPath: " + xpath;
			//System.out.println("ERROR: " + errorMsg + " - " + e.getMessage());
			extentTest.log(Status.FAIL, "ERROR: " + errorMsg + " - " + e.getMessage());
			throw new RuntimeException(errorMsg, e);
		}
	}

	/**
	 * Clicks on an element identified by XPath with comprehensive exception handling
	 * Includes retry mechanism with JavaScript click for certain exceptions
	 * @param xpath XPath string to locate the element to click
	 */

	///   charAt ,  split  , substring, toUpperCase , toLowerCase , equals, equalsIgnoreCase , trim , length() 
	///
	///
	///
	public String getXpath(WebElement element) {
		String weText=element.toString();
		String[] arrText=weText.split("xpath:");
		String xpath=arrText[1];
		xpath=xpath.substring(0, xpath.length()-1);
		return xpath;
	}
	public void click(WebElement element) {
		System.out.println("Attempting to click element with XPath: " + element);

		try {
			//WebElement element = getElement(xpath);
			//[[ChromeDriver: chrome on windows (c8d933cfb930749c7bf1e5bb4a37cf80)] -> xpath: //input[@id='submitButton']]

			String xpath= getXpath(element)	;	
			
			
			if(element.isDisplayed()) {
				logger.info(xpath+"element is displayed");
				if(element.isEnabled()) {
					
					logger.info(xpath+"element is enabled");
					element.click();
				}else {
					logger.error(xpath+"element is disabled");
				}
			
			}else {
				logger.error(xpath+"element is invisible");
			}
		
			
			extentTest.log(Status.PASS,"Attempting to find elements with XPath: " + xpath);

		}catch (ElementClickInterceptedException e) {
			//System.out.println("Click intercepted, attempting JavaScript click...");
			logger.debug("exception occured."+e.getMessage());
			extentTest.log(Status.WARNING, "Click intercepted, attempting JavaScript click...");
			jsClick(element);
		}  catch (ElementNotInteractableException e) {
			//System.out.println("Element not interactable, attempting JavaScript click...");
			extentTest.log(Status.WARNING, "Element not interactable, attempting JavaScript click...");
			jsClick(element);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element, re-finding and retrying click...");
			extentTest.log(Status.WARNING, "Stale element, re-finding and retrying click...");
			// Wait a bit and retry
			waitForMillis(1000);
			click(element); // Recursive retry
		} catch (Exception e) {
			//System.out.println("Unexpected exception during click: " + e.getMessage());
			extentTest.log(Status.FAIL, "Unexpected exception during click: " + e.getMessage());
			jsClick(element); // Fallback to JavaScript click
		}
	}

	/**
	 * Sends text to an input field identified by XPath
	 * @param xpath XPath string to locate the input element
	 * @param text Text to send to the input field
	 */
	public void sendKeys(WebElement element, String text) {
		//System.out.println("Attempting to send text '" + text + "' to element with XPath: " + we);
		extentTest.log(Status.PASS, "Attempting to send text '" + text + "' to element with XPath: " + element);
		String xpath= getXpath(element)	;	

		try {
			element.clear();
			element.sendKeys(text);
			//System.out.println("Text '" + text + "' sent successfully to element with XPath: " + xpath);
			extentTest.log(Status.PASS, "Text '" + text + "' sent successfully to element with XPath: " + xpath);
		} catch (ElementNotInteractableException e) {
			//System.out.println("Element not interactable, attempting JavaScript sendKeys...");
			extentTest.log(Status.WARNING, "Element not interactable, attempting JavaScript sendKeys...");
			jsSendKeys(element, text);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element, re-finding and retrying sendKeys...");
			extentTest.log(Status.WARNING, "Stale element, re-finding and retrying sendKeys...");
			waitForMillis(1000);
			sendKeys(element, text); // Recursive retry
		} catch (Exception e) {
			//System.out.println("Unexpected exception during sendKeys: " + e.getMessage());
			extentTest.log(Status.FAIL, "Unexpected exception during sendKeys: " + e.getMessage());
			jsSendKeys(element, text); // Fallback to JavaScript
		}
	}

	/**
	 * Performs JavaScript click on an element when regular click fails
	 * @param xpath XPath string to locate the element
	 */
	public void jsClick(WebElement element) {
		try {
			JavascriptExecutor js=(JavascriptExecutor)driver;
			//WebElement element = getElement(xpath);
			js.executeScript("arguments[0].click();", element);
			//System.out.println("JavaScript click performed successfully on element with XPath: " + we);
			extentTest.log(Status.INFO, "JavaScript click performed successfully on element with XPath: " + element);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in jsClick, retrying...");
			extentTest.log(Status.INFO, "Stale element in jsClick, retrying...");
			waitForMillis(1000);
			jsClick(element); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: JavaScript click failed: " + e.getMessage());
			extentTest.log(Status.INFO, "ERROR: JavaScript click failed: " + e.getMessage());
			throw new RuntimeException("JavaScript click failed for XPath: " + element, e);
		}
	}

	/**
	 * Performs JavaScript sendKeys when regular sendKeys fails
	 * @param xpath XPath string to locate the element
	 * @param text Text to send to the input field
	 */
	public void jsSendKeys(WebElement element, String text) {
		try {
			JavascriptExecutor js=(JavascriptExecutor)driver;
			//WebElement element = getElement(xpath);
			js.executeScript("arguments[0].value = arguments[1];", element, text);
			//System.out.println("JavaScript sendKeys performed successfully on element with XPath: " + we);
			extentTest.log(Status.INFO, "JavaScript sendKeys performed successfully on element with XPath: " + element);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in jsSendKeys, retrying...");
			extentTest.log(Status.INFO, "Stale element in jsSendKeys, retrying...");
			waitForMillis(1000);
			jsSendKeys(element, text); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: JavaScript sendKeys failed: " + e.getMessage());
			extentTest.log(Status.FAIL, "ERROR: JavaScript sendKeys failed: " + e.getMessage());
			throw new RuntimeException("JavaScript sendKeys failed for XPath: " + element, e);
		}
	}

	/**
	 * Gets text from an element identified by XPath
	 * @param xpath XPath string to locate the element
	 * @return Text content of the element
	 */
	public String getText(WebElement element) {
		try {
			//WebElement element = getElement(xpath);
			String text = element.getText();
			//System.out.println("Retrieved text: '" + text + "' from element with XPath: " + we);
			extentTest.log(Status.INFO, "Retrieved text: '" + text + "' from element with XPath: " + element);
			return text;
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in getText, retrying...");
			extentTest.log(Status.INFO, "Stale element in getText, retrying...");
			waitForMillis(1000);
			return getText(element); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to get text: " + e.getMessage());
			extentTest.log(Status.FAIL, "ERROR: Failed to get text: " + e.getMessage());
			throw new RuntimeException("Failed to get text for XPath: " + element, e);
		}
	}

	/**
	 * Gets attribute value from an element identified by XPath
	 * @param xpath XPath string to locate the element
	 * @param attributeName Name of the attribute to retrieve
	 * @return Attribute value
	 */
	public String getAttribute(WebElement element, String attributeName) {
		try {
			//WebElement element = getElement(xpath);
			String value = element.getAttribute(attributeName);
			//System.out.println("Retrieved attribute '" + attributeName + "': '" + value + "' from element with XPath: " + we);
			extentTest.log(Status.INFO, "Retrieved attribute '" + attributeName + "': '" + value + "' from element with XPath: " + element);
			return value;
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in getAttribute, retrying...");
			extentTest.log(Status.INFO, "Stale element in getAttribute, retrying...");
			waitForMillis(1000);
			return getAttribute(element, attributeName); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to get attribute: " + e.getMessage());
			extentTest.log(Status.FAIL, "ERROR: Failed to get attribute: " + e.getMessage());
			throw new RuntimeException("Failed to get attribute for XPath: " + element, e);
		}
	}

	/**
	 * Checks if an element is displayed on the page
	 * @param xpath XPath string to locate the element
	 * @return true if element is displayed, false otherwise
	 */
	public boolean isDisplayed(WebElement element) {
		try {
			//WebElement element = getElement(xpath);
			boolean displayed = element.isDisplayed();
			//System.out.println("Element with XPath " + we + " is displayed: " + displayed);
			extentTest.log(Status.INFO, "Element with XPath " + element + " is displayed: " + displayed);
			return displayed;
		} catch (NoSuchElementException e) {
			//System.out.println("Element not found when checking display status: " + we);
			extentTest.log(Status.INFO, "Element not found when checking display status: " + element);
			return false;
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in isDisplayed, retrying...");
			extentTest.log(Status.INFO,"Stale element in isDisplayed, retrying...");
			waitForMillis(1000);
			return isDisplayed(element); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to check display status: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to check display status: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Checks if an element is enabled on the page
	 * @param xpath XPath string to locate the element
	 * @return true if element is enabled, false otherwise
	 */
	public boolean isEnabled(WebElement element) {
		try {
			//WebElement element = getElement(xpath);
			boolean enabled = element.isEnabled();
			//System.out.println("Element with XPath " + we + " is enabled: " + enabled);
			extentTest.log(Status.INFO,"Element with XPath " + element + " is enabled: " + enabled);
			return enabled;
		} catch (NoSuchElementException e) {
			//System.out.println("Element not found when checking enabled status: " + we);
			extentTest.log(Status.INFO,"Element not found when checking enabled status: " + element);
			return false;
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in isEnabled, retrying...");
			extentTest.log(Status.INFO,"Stale element in isEnabled, retrying...");
			waitForMillis(1000);
			return isEnabled(element); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to check enabled status: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to check enabled status: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Checks if an element is selected (for checkboxes, radio buttons)
	 * @param xpath XPath string to locate the element
	 * @return true if element is selected, false otherwise
	 */
	public boolean isSelected(WebElement element) {
		try {
			//WebElement element = getElement(xpath);
			boolean selected = element.isSelected();
			//System.out.println("Element with XPath " + we + " is selected: " + selected);
			extentTest.log(Status.INFO,"Element with XPath " + element + " is selected: " + selected);
			return selected;
		} catch (NoSuchElementException e) {
			//System.out.println("Element not found when checking selected status: " + we);
			extentTest.log(Status.INFO,"Element not found when checking selected status: " + element);
			return false;
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in isSelected, retrying...");
			extentTest.log(Status.INFO,"Stale element in isSelected, retrying...");
			waitForMillis(1000);
			return isSelected(element); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to check selected status: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to check selected status: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Waits for an element to be visible with explicit wait
	 * @param xpath XPath string to locate the element
	 * @param timeoutSeconds Timeout in seconds
	 */
	public void waitForVisibility(String xpath, int timeoutSeconds) {
		//System.out.println("Waiting for element to be visible with XPath: " + xpath + ", timeout: " + timeoutSeconds + "s");
		extentTest.log(Status.INFO,"Waiting for element to be visible with XPath: " + xpath + ", timeout: " + timeoutSeconds + "s");
		try {
			WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			customWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			//System.out.println("Element became visible within " + timeoutSeconds + " seconds");
			extentTest.log(Status.INFO,"Element became visible within " + timeoutSeconds + " seconds");
		} catch (TimeoutException e) {
			//System.out.println("Timeout: Element not visible within " + timeoutSeconds + " seconds");
			extentTest.log(Status.INFO,"Timeout: Element not visible within " + timeoutSeconds + " seconds");
			throw new RuntimeException("Element not visible within " + timeoutSeconds + " seconds: " + xpath, e);
		} catch (Exception e) {
			//System.out.println("ERROR: Exception while waiting for visibility: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Exception while waiting for visibility: " + e.getMessage());
			throw new RuntimeException("Failed to wait for visibility: " + xpath, e);
		}
	}

	/**
	 * Waits for an element to be invisible with explicit wait
	 * @param xpath XPath string to locate the element
	 * @param timeoutSeconds Timeout in seconds
	 */
	public void waitForInvisibility(String xpath, int timeoutSeconds) {
		//System.out.println("Waiting for element to be invisible with XPath: " + xpath + ", timeout: " + timeoutSeconds + "s");
		extentTest.log(Status.INFO,"Waiting for element to be invisible with XPath: " + xpath + ", timeout: " + timeoutSeconds + "s");
		try {
			WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			customWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
			//System.out.println("Element became invisible within " + timeoutSeconds + " seconds");
			extentTest.log(Status.INFO,"Element became invisible within " + timeoutSeconds + " seconds");
		} catch (TimeoutException e) {
			//System.out.println("Timeout: Element still visible after " + timeoutSeconds + " seconds");
			extentTest.log(Status.INFO,"Timeout: Element still visible after " + timeoutSeconds + " seconds");
			throw new RuntimeException("Element still visible after " + timeoutSeconds + " seconds: " + xpath, e);
		} catch (Exception e) {
			//System.out.println("ERROR: Exception while waiting for invisibility: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Exception while waiting for invisibility: " + e.getMessage());
			throw new RuntimeException("Failed to wait for invisibility: " + xpath, e);
		}
	}

	/**
	 * Waits for an element to be clickable with explicit wait
	 * @param xpath XPath string to locate the element
	 * @param timeoutSeconds Timeout in seconds
	 */
	public void waitForClickable(String xpath, int timeoutSeconds) {
		System.out.println("Waiting for element to be clickable with XPath: " + xpath + ", timeout: " + timeoutSeconds + "s");
		extentTest.log(Status.INFO,"Waiting for element to be invisible with XPath: " + xpath + ", timeout: " + timeoutSeconds + "s");
		try {
			WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			customWait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
			//System.out.println("Element became clickable within " + timeoutSeconds + " seconds");
			extentTest.log(Status.INFO,"Element became clickable within " + timeoutSeconds + " seconds");
		} catch (TimeoutException e) {
			//System.out.println("Timeout: Element not clickable within " + timeoutSeconds + " seconds");
			extentTest.log(Status.INFO,"Timeout: Element not clickable within " + timeoutSeconds + " seconds");
			throw new RuntimeException("Element not clickable within " + timeoutSeconds + " seconds: " + xpath, e);
		} catch (Exception e) {
			//System.out.println("ERROR: Exception while waiting for clickable: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Exception while waiting for clickable: " + e.getMessage());
			throw new RuntimeException("Failed to wait for clickable: " + xpath, e);
		}
	}

	/**
	 * Waits for text to be present in element with explicit wait
	 * @param xpath XPath string to locate the element
	 * @param text Text to wait for
	 * @param timeoutSeconds Timeout in seconds
	 */
	public void waitForText(String xpath, String text, int timeoutSeconds) {
		System.out.println("Waiting for text '" + text + "' in element with XPath: " + xpath + ", timeout: " + timeoutSeconds + "s");
		extentTest.log(Status.INFO,"Waiting for text '" + text + "' in element with XPath: " + xpath + ", timeout: " + timeoutSeconds + "s");
		try {
			WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
			customWait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(xpath), text));
			//System.out.println("Text '" + text + "' found in element within " + timeoutSeconds + " seconds");
			extentTest.log(Status.INFO,"Text '" + text + "' found in element within " + timeoutSeconds + " seconds");
		} catch (TimeoutException e) {
			//System.out.println("Timeout: Text '" + text + "' not found within " + timeoutSeconds + " seconds");
			extentTest.log(Status.INFO,"Timeout: Text '" + text + "' not found within " + timeoutSeconds + " seconds");
			throw new RuntimeException("Text '" + text + "' not found within " + timeoutSeconds + " seconds: " + xpath, e);
		} catch (Exception e) {
			//System.out.println("ERROR: Exception while waiting for text: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Exception while waiting for text: " + e.getMessage());
			throw new RuntimeException("Failed to wait for text: " + xpath, e);
		}
	}

	/**
	 * Custom fluent wait with configurable parameters
	 * @param xpath XPath string to locate the element
	 * @param timeoutSeconds Maximum time to wait
	 * @param pollIntervalSeconds Interval between checks
	 * @param condition Condition to wait for
	 */
	public void fluentWait(String xpath, int timeoutSeconds, int pollIntervalSeconds, Function<WebDriver, Boolean> condition) {
		//System.out.println("Starting fluent wait for element with XPath: " + xpath);
		extentTest.log(Status.INFO,"Starting fluent wait for element with XPath: " + xpath);
		try {
			FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
					.withTimeout(Duration.ofSeconds(timeoutSeconds))
					.pollingEvery(Duration.ofSeconds(pollIntervalSeconds))
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class);

			fluentWait.until(condition);
			//System.out.println("Fluent wait condition satisfied for element with XPath: " + xpath);
			extentTest.log(Status.INFO,"Fluent wait condition satisfied for element with XPath: " + xpath);
		} catch (TimeoutException e) {
			//System.out.println("Timeout: Fluent wait condition not satisfied within " + timeoutSeconds + " seconds");
			extentTest.log(Status.INFO,"Timeout: Fluent wait condition not satisfied within " + timeoutSeconds + " seconds");
			throw new RuntimeException("Fluent wait condition not satisfied: " + xpath, e);
		} catch (Exception e) {
			//System.out.println("ERROR: Exception during fluent wait: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Exception during fluent wait: " + e.getMessage());
			throw new RuntimeException("Fluent wait failed: " + xpath, e);
		}
	}

	/**
	 * Scrolls to an element using JavaScript
	 * @param xpath XPath string to locate the element
	 */
	public void jsScrollToElement(WebElement element) {
		try {
			JavascriptExecutor js=(JavascriptExecutor)driver;

			//WebElement element = getElement(xpath);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			//System.out.println("Scrolled to element with XPath: " + we);
			extentTest.log(Status.INFO,"Scrolled to element with XPath: " + element);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in jsScrollToElement, retrying...");
			extentTest.log(Status.INFO,"Stale element in jsScrollToElement, retrying...");
			waitForMillis(1000);
			jsScrollToElement(element); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: JavaScript scroll failed: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: JavaScript scroll failed: " + e.getMessage());
			throw new RuntimeException("JavaScript scroll failed for XPath: " + element, e);
		}
	}

	/**
	 * Scrolls to the top of the page using JavaScript
	 */
	public void jsScrollToTop() {
		try {
			JavascriptExecutor js=(JavascriptExecutor)driver;

			js.executeScript("window.scrollTo(0, 0);");
			//System.out.println("Scrolled to top of the page");
			extentTest.log(Status.INFO,"Scrolled to top of the page");
		} catch (Exception e) {
			//System.out.println("ERROR: JavaScript scroll to top failed: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: JavaScript scroll to top failed: " + e.getMessage());
			throw new RuntimeException("JavaScript scroll to top failed", e);
		}
	}

	/**
	 * Scrolls to the bottom of the page using JavaScript
	 */
	public void jsScrollToBottom() {
		try {
			JavascriptExecutor js=(JavascriptExecutor)driver;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			//System.out.println("Scrolled to bottom of the page");
			extentTest.log(Status.INFO,"Scrolled to bottom of the page");
		} catch (Exception e) {
			//System.out.println("ERROR: JavaScript scroll to bottom failed: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: JavaScript scroll to bottom failed: " + e.getMessage());
			throw new RuntimeException("JavaScript scroll to bottom failed", e);
		}
	}

	/**
	 * Uses Actions class to perform a click
	 * @param xpath XPath string to locate the element
	 */
	public void actionsClick(WebElement element) {
		try {
			Actions actions=new Actions(driver);
			//WebElement element = getElement(xpath);
			actions.click(element).perform();
			//System.out.println("Actions click performed on element with XPath: " + we);
			extentTest.log(Status.INFO,"Actions click performed on element with XPath: " + element);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in actionsClick, retrying...");
			extentTest.log(Status.INFO,"Stale element in actionsClick, retrying...");
			waitForMillis(1000);
			actionsClick(element); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: Actions click failed: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Actions click failed: " + e.getMessage());
			throw new RuntimeException("Actions click failed for XPath: " + element, e);
		}
	}

	/**
	 * Uses Actions class to perform a double click
	 * @param xpath XPath string to locate the element
	 */
	public void actionsDoubleClick(WebElement element) {
		try {
			Actions actions=new Actions(driver);

			//WebElement element = getElement(xpath);
			actions.doubleClick(element).perform();
			//System.out.println("Actions double click performed on element with XPath: " + we);
			extentTest.log(Status.INFO,"Actions double click performed on element with XPath: " + element);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in actionsDoubleClick, retrying...");
			extentTest.log(Status.INFO,"Stale element in actionsDoubleClick, retrying...");
			waitForMillis(1000);
			actionsDoubleClick(element); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: Actions double click failed: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Actions double click failed: " + e.getMessage());
			throw new RuntimeException("Actions double click failed for XPath: " + element, e);
		}
	}

	/**
	 * Uses Actions class to perform a context click (right-click)
	 * @param xpath XPath string to locate the element
	 */
	public void actionsContextClick(WebElement element) {
		try {
			Actions actions=new Actions(driver);

			//WebElement element = getElement(xpath);
			actions.contextClick(element).perform();
			//System.out.println("Actions context click performed on element with XPath: " + we);
			extentTest.log(Status.INFO,"Actions context click performed on element with XPath: " + element);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in actionsContextClick, retrying...");
			extentTest.log(Status.INFO,"Stale element in actionsContextClick, retrying...");
			waitForMillis(1000);
			actionsContextClick(element); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: Actions context click failed: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Actions context click failed: " + e.getMessage());
			throw new RuntimeException("Actions context click failed for XPath: " + element, e);
		}
	}

	/**
	 * Uses Actions class to drag and drop an element
	 * @param sourceXpath XPath string to locate the source element
	 * @param targetXpath XPath string to locate the target element
	 */
	public void actionsDragAndDrop(WebElement sourceXpath, WebElement targetXpath) {
		try {
			Actions actions=new Actions(driver);

			//			WebElement source = getElement(sourceXpath);
			//			WebElement target = getElement(targetXpath);
			actions.dragAndDrop(sourceXpath, targetXpath).perform();
			//System.out.println("Actions drag and drop performed from " + sourceXpath + " to " + targetXpath);
			extentTest.log(Status.INFO,"Actions drag and drop performed from " + sourceXpath + " to " + targetXpath);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in actionsDragAndDrop, retrying...");
			extentTest.log(Status.INFO,"Stale element in actionsDragAndDrop, retrying...");
			waitForMillis(1000);
			actionsDragAndDrop(sourceXpath, targetXpath); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: Actions drag and drop failed: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Actions drag and drop failed: " + e.getMessage());
			throw new RuntimeException("Actions drag and drop failed from " + sourceXpath + " to " + targetXpath, e);
		}
	}

	/**
	 * Uses Actions class to send keys to an element
	 * @param xpath XPath string to locate the element
	 * @param text Text to send to the element
	 */
	public void actionsSendKeys(WebElement element, String text) {
		try {
			Actions actions=new Actions(driver);

			//WebElement element = getElement(xpath);
			actions.sendKeys(element, text).perform();
			//System.out.println("Actions sendKeys performed on element with XPath: " + we);
			extentTest.log(Status.INFO,"Actions sendKeys performed on element with XPath: " + element);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in actionsSendKeys, retrying...");
			extentTest.log(Status.INFO,"Stale element in actionsSendKeys, retrying...");
			waitForMillis(1000);
			actionsSendKeys(element, text); // Recursive retry
		} catch (Exception e) {
			//System.out.println("ERROR: Actions sendKeys failed: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Actions sendKeys failed: " + e.getMessage());
			throw new RuntimeException("Actions sendKeys failed for XPath: " + element, e);
		}
	}

	/**
	 * Selects an option from dropdown by visible text
	 * @param xpath XPath string to locate the dropdown element
	 * @param text Visible text of the option to select
	 */
	public void selectByVisibleText(WebElement element, String text) {
		try {
			//WebElement element = getElement(xpath);
			Select dropdown = new Select(element);
			dropdown.selectByVisibleText(text);
			//System.out.println("Selected option by visible text '" + text + "' from dropdown with XPath: " + we);
			extentTest.log(Status.INFO,"Selected option by visible text '" + text + "' from dropdown with XPath: " + element);
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in selectByVisibleText, retrying...");
			extentTest.log(Status.INFO,"Stale element in selectByVisibleText, retrying...");
			waitForMillis(1000);
			selectByVisibleText(element, text); // Recursive retry
		} catch (NoSuchElementException e) {
			//System.out.println("Option with text '" + text + "' not found in dropdown");
			extentTest.log(Status.INFO,"Option with text '" + text + "' not found in dropdown");
			throw new RuntimeException("Option with text '" + text + "' not found in dropdown: " + element, e);
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to select by visible text: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to select by visible text: " + e.getMessage());
			throw new RuntimeException("Failed to select by visible text: " + element, e);
		}
	}

	/**
	 * Selects an option from dropdown by value
	 * @param xpath XPath string to locate the dropdown element
	 * @param value Value attribute of the option to select
	 */
	public void selectByValue(WebElement element, String value) {
		try {
			//WebElement element = getElement(xpath);
			Select dropdown = new Select(element);
			dropdown.selectByValue(value);
			//	System.out.println("Selected option by value '" + value + "' from dropdown with XPath: " + we);
			extentTest.log(Status.INFO,"Selected option by value '" + value + "' from dropdown with XPath: " + element);	
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in selectByValue, retrying...");
			extentTest.log(Status.INFO,"Stale element in selectByValue, retrying...");	
			waitForMillis(1000);
			selectByValue(element, value); // Recursive retry
		} catch (NoSuchElementException e) {
			//System.out.println("Option with value '" + value + "' not found in dropdown");
			extentTest.log(Status.INFO,"Option with value '" + value + "' not found in dropdown");	
			throw new RuntimeException("Option with value '" + value + "' not found in dropdown: " + element, e);
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to select by value: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to select by value: " + e.getMessage());	
			throw new RuntimeException("Failed to select by value: " + element, e);
		}
	}

	/**
	 * Selects an option from dropdown by index
	 * @param xpath XPath string to locate the dropdown element
	 * @param index Index of the option to select
	 */
	public void selectByIndex(WebElement element, int index) {
		try {
			//WebElement element = getElement(xpath);
			Select dropdown = new Select(element);
			dropdown.selectByIndex(index);
			//System.out.println("Selected option by index " + index + " from dropdown with XPath: " + we);
			extentTest.log(Status.INFO,"Selected option by index " + index + " from dropdown with XPath: " + element);	
		} catch (StaleElementReferenceException e) {
			//System.out.println("Stale element in selectByIndex, retrying...");
			extentTest.log(Status.INFO,"Stale element in selectByIndex, retrying...");	
			waitForMillis(1000);
			selectByIndex(element, index); // Recursive retry
		} catch (NoSuchElementException e) {
			//System.out.println("Option with index " + index + " not found in dropdown");
			extentTest.log(Status.INFO,"Option with index " + index + " not found in dropdown");	
			throw new RuntimeException("Option with index " + index + " not found in dropdown: " + element, e);
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to select by index: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to select by index: " + e.getMessage());	
			throw new RuntimeException("Failed to select by index: " + element, e);
		}
	}

	/**
	 * Switches to a frame by XPath
	 * @param xpath XPath string to locate the frame element
	 */
	public void switchToFrame(WebElement element) {
		try {
			//WebElement frame = getElement(xpath);
			driver.switchTo().frame(element);
			//System.out.println("Switched to frame with XPath: " + we);
			extentTest.log(Status.INFO,"Switched to frame with XPath: " + element);	
		} catch (NoSuchFrameException e) {
			//System.out.println("Frame not found with XPath: " + we);
			extentTest.log(Status.INFO,"Frame not found with XPath: " + element);	
			throw new RuntimeException("Frame not found: " + element, e);
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to switch to frame: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to switch to frame: " + e.getMessage());	
			throw new RuntimeException("Failed to switch to frame: " + element, e);
		}
	}

	/**
	 * Switches back to the default content from a frame
	 */
	public void switchToDefaultContent() {
		try {
			driver.switchTo().defaultContent();
			//System.out.println("Switched to default content");
			extentTest.log(Status.INFO,"Switched to default content");	
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to switch to default content: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to switch to default content: " + e.getMessage());	
			throw new RuntimeException("Failed to switch to default content", e);
		}
	}

	/**
	 * Switches to a window by title
	 * @param title Title of the window to switch to
	 */
	public void switchToWindow(String title) {
		try {
			Set<String> windowHandles = driver.getWindowHandles();

			for (String handle : windowHandles) {
				driver.switchTo().window(handle);
				if (driver.getTitle().equals(title)) {
					//System.out.println("Switched to window with title: " + title);
					extentTest.log(Status.INFO,"Switched to window with title: " + title);	
					return;
				}
			}

			//System.out.println("Window with title '" + title + "' not found");
			extentTest.log(Status.INFO,"Window with title '" + title + "' not found");	
			throw new RuntimeException("Window with title '" + title + "' not found");
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to switch to window: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to switch to window: " + e.getMessage());	
			throw new RuntimeException("Failed to switch to window: " + title, e);
		}
	}

	/**
	 * Switches to a window by URL
	 * @param url URL of the window to switch to
	 */
	public void switchToWindowByUrl(String url) {
		try {
			Set<String> windowHandles = driver.getWindowHandles();

			for (String handle : windowHandles) {
				driver.switchTo().window(handle);
				if (driver.getCurrentUrl().equals(url)) {
					//System.out.println("Switched to window with URL: " + url);
					extentTest.log(Status.INFO,"Switched to window with URL: " + url);	
					return;
				}
			}

			//System.out.println("Window with URL '" + url + "' not found");
			extentTest.log(Status.INFO,"Window with URL '" + url + "' not found");	
			throw new RuntimeException("Window with URL '" + url + "' not found");
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to switch to window by URL: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to switch to window by URL: " + e.getMessage());	
			throw new RuntimeException("Failed to switch to window by URL: " + url, e);
		}
	}

	/**
	 * Refreshes the current page
	 */
	public void refreshPage() {
		try {
			driver.navigate().refresh();
			//System.out.println("Page refreshed successfully");
			extentTest.log(Status.INFO,"Page refreshed successfully");	
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to refresh page: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to refresh page: " + e.getMessage());	
			throw new RuntimeException("Failed to refresh page", e);
		}
	}

	/**
	 * Navigates back to the previous page
	 */
	public void navigateBack() {
		try {
			driver.navigate().back();
			//System.out.println("Navigated back successfully");
			extentTest.log(Status.INFO,"Navigated back successfully");	
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to navigate back: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to navigate back: " + e.getMessage());	
			throw new RuntimeException("Failed to navigate back", e);
		}
	}

	/**
	 * Navigates forward to the next page
	 */
	public void navigateForward() {
		try {
			driver.navigate().forward();
			//System.out.println("Navigated forward successfully");
			extentTest.log(Status.INFO,"Navigated forward successfully");	
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to navigate forward: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to navigate forward: " + e.getMessage());	
			throw new RuntimeException("Failed to navigate forward", e);
		}
	}

	/**
	 * Gets the current URL
	 * @return Current URL
	 */
	public String getCurrentUrl() {
		try {
			String url = driver.getCurrentUrl();
			//System.out.println("Current URL: " + url);
			extentTest.log(Status.INFO,"Current URL: " + url);	
			return url;
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to get current URL: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to get current URL: " + e.getMessage());	
			throw new RuntimeException("Failed to get current URL", e);
		}
	}

	/**
	 * Gets the page title
	 * @return Page title
	 */
	public String getPageTitle() {
		try {
			String title = driver.getTitle();
			//System.out.println("Page title: " + title);
			extentTest.log(Status.INFO,"Page title: " + title);	
			return title;
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to get page title: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to get page title: " + e.getMessage());	
			throw new RuntimeException("Failed to get page title", e);
		}
	}

	/**
	 * Accepts an alert
	 */
	public void acceptAlert() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			System.out.println("Alert accepted successfully");
			extentTest.log(Status.INFO,"Alert accepted successfully");	
		} catch (NoAlertPresentException e) {
			//System.out.println("No alert present to accept");
			extentTest.log(Status.INFO,"No alert present to accept");	
			throw new RuntimeException("No alert present to accept", e);
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to accept alert: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to accept alert: " + e.getMessage());	
			throw new RuntimeException("Failed to accept alert", e);
		}
	}

	/**
	 * Dismisses an alert
	 */
	public void dismissAlert() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.dismiss();
			//System.out.println("Alert dismissed successfully");
			extentTest.log(Status.INFO,"Alert dismissed successfully");	
		} catch (NoAlertPresentException e) {
			//System.out.println("No alert present to dismiss");
			extentTest.log(Status.INFO,"No alert present to dismiss");	
			throw new RuntimeException("No alert present to dismiss", e);
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to dismiss alert: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to dismiss alert: " + e.getMessage());
			throw new RuntimeException("Failed to dismiss alert", e);
		}
	}

	/**
	 * Gets text from an alert
	 * @return Alert text
	 */
	public String getAlertText() {
		try {
			Alert alert = driver.switchTo().alert();
			String text = alert.getText();
			//System.out.println("Alert text: " + text);
			extentTest.log(Status.INFO,"Alert text: " + text);
			return text;
		} catch (NoAlertPresentException e) {
			//System.out.println("No alert present to get text from");
			extentTest.log(Status.INFO,"No alert present to get text from");
			throw new RuntimeException("No alert present to get text from", e);
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to get alert text: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to get alert text: " + e.getMessage());
			throw new RuntimeException("Failed to get alert text", e);
		}
	}

	/**
	 * Utility method to wait for milliseconds
	 * @param millis Milliseconds to wait
	 */
	private void waitForMillis(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			//System.out.println("Wait interrupted: " + e.getMessage());
			extentTest.log(Status.INFO,"Wait interrupted: " + e.getMessage());
		}
	}


	/**
	 * Validates if element text equals expected text
	 * @param xpath XPath of the element
	 * @param expectedText Expected text to validate
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateTextEquals(WebElement element, String expectedText, String message) {
		String actualText = getText(element);
		if (!actualText.equals(expectedText)) {
			String errorMsg = message + " - Expected: '" + expectedText + "', Actual: '" + actualText + "'";
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);

		}else {
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);
	}
		Assert.assertEquals(actualText,expectedText);
	}
	
	

	/**
	 * Validates if element text contains expected text
	 * @param xpath XPath of the element
	 * @param expectedText Expected text to contain
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateTextContains(WebElement element, String expectedText, String message) {
		String actualText = getText(element);
		if (!actualText.contains(expectedText)) {
			String errorMsg = message + " - Expected to contain: '" + expectedText + "', Actual: '" + actualText + "'";
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);

			
		}else {
		
		//	System.out.println("VALIDATION PASSED: " + message);
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);
	}

		Assert.assertTrue(actualText.contains(expectedText));
	}
	
	/**
	 * Validates if element attribute equals expected value
	 * @param xpath XPath of the element
	 * @param attributeName Name of the attribute to validate
	 * @param expectedValue Expected attribute value
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateAttributeEquals(WebElement element, String attributeName, String expectedValue, String message) {
		String actualValue = getAttribute(element, attributeName);
		if (!actualValue.equals(expectedValue)) {
			String errorMsg = message + " - Attribute: " + attributeName + ", Expected: '" + expectedValue + "', Actual: '" + actualValue + "'";
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			throw new AssertionError(errorMsg);
		}
		//System.out.println("VALIDATION PASSED: " + message);
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);

	}

	/**
	 * Validates if element is present on the page
	 * @param xpath XPath of the element
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateElementPresent(WebElement element, String message) {
		if(element.isDisplayed()) {
			extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);
			
		}else {
			String errorMsg = message + " - Element not found with XPath: " + element;
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			
		}
		Assert.assertTrue(element.isDisplayed());
	}

	/**
	 * Validates if element is NOT present on the page
	 * @param xpath XPath of the element
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateElementNotPresent(WebElement element, String message) {
		try {
			//getElement(we);
			String errorMsg = message + " - Element should not be present but was found with XPath: " + element;
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			throw new AssertionError(errorMsg);
		} catch (Exception e) {
			//System.out.println("VALIDATION PASSED: " + message);
			extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);

		}
	}



	/**
	 * Validates if element is visible
	 * @param xpath XPath of the element
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateElementVisible(WebElement element, String message) {
		if (!isDisplayed(element)) {
			String errorMsg = message + " - Element not visible with XPath: " + element;
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			throw new AssertionError(errorMsg);
		}
		//System.out.println("VALIDATION PASSED: " + message);
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);

	}

	/**
	 * Validates if element is NOT visible
	 * @param xpath XPath of the element
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateElementNotVisible(WebElement element, String message) {
		if (isDisplayed(element)) {
			String errorMsg = message + " - Element should not be visible but was found with XPath: " + element;
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			throw new AssertionError(errorMsg);
		}
		//System.out.println("VALIDATION PASSED: " + message);
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);
	}

	/**
	 * Validates if element is enabled
	 * @param xpath XPath of the element
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateElementEnabled(WebElement element, String message) {
		if (!isEnabled(element)) {
			String errorMsg = message + " - Element not enabled with XPath: " + element;
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			throw new AssertionError(errorMsg);
		}
		//System.out.println("VALIDATION PASSED: " + message);
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);
	}

	/**
	 * Validates if element is disabled
	 * @param xpath XPath of the element
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateElementDisabled(WebElement element, String message) {
		if (isEnabled(element)) {
			String errorMsg = message + " - Element should be disabled but was enabled with XPath: " + element;
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			throw new AssertionError(errorMsg);
		}
		//System.out.println("VALIDATION PASSED: " + message);
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);
	}

	/**
	 * Validates if current URL equals expected URL
	 * @param expectedUrl Expected URL
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateUrlEquals(String expectedUrl, String message) {
		String actualUrl = getCurrentUrl();
		if (!actualUrl.equals(expectedUrl)) {
			String errorMsg = message + " - Expected URL: '" + expectedUrl + "', Actual URL: '" + actualUrl + "'";
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			throw new AssertionError(errorMsg);
		}
		//System.out.println("VALIDATION PASSED: " + message);
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);
	}

	/**
	 * Validates if current URL contains expected text
	 * @param expectedText Expected text to contain in URL
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateUrlContains(String expectedText, String message) {
		String actualUrl = getCurrentUrl();
		if (!actualUrl.contains(expectedText)) {
			String errorMsg = message + " - Expected URL to contain: '" + expectedText + "', Actual URL: '" + actualUrl + "'";
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			throw new AssertionError(errorMsg);
		}
		//System.out.println("VALIDATION PASSED: " + message);
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);
	}

	/**
	 * Validates if page title equals expected title
	 * @param expectedTitle Expected page title
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateTitleEquals(String expectedTitle, String message) {
		String actualTitle = getPageTitle();
		if (!actualTitle.equals(expectedTitle)) {
			String errorMsg = message + " - Expected Title: '" + expectedTitle + "', Actual Title: '" + actualTitle + "'";
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			throw new AssertionError(errorMsg);
		}
		//System.out.println("VALIDATION PASSED: " + message);
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);
	}

	/**
	 * Validates if page title contains expected text
	 * @param expectedText Expected text to contain in title
	 * @param message Custom error message for assertion failure
	 * @throws AssertionError if validation fails
	 */
	public void validateTitleContains(String expectedText, String message) {
		String actualTitle = getPageTitle();
		if (!actualTitle.contains(expectedText)) {
			String errorMsg = message + " - Expected Title to contain: '" + expectedText + "', Actual Title: '" + actualTitle + "'";
			//System.out.println("VALIDATION FAILED: " + errorMsg);
			extentTest.log(Status.FAIL, "VALIDATION FAILED: " + errorMsg);
			throw new AssertionError(errorMsg);
		}
		//System.out.println("VALIDATION PASSED: " + message);
		extentTest.log(Status.PASS, "VALIDATION PASSED: " + message);
	}

	/**
	 * Uploads a file to a file input element
	 * @param xpath XPath of the file input element
	 * @param filePath Full path of the file to upload
	 */
	public void uploadFile(WebElement element, String filePath) {
		try {
			//WebElement fileInput = getElement(xpath);
			element.sendKeys(filePath);
			//	System.out.println("File uploaded successfully: " + filePath);
			extentTest.log(Status.INFO, "File uploaded successfully: " + filePath);
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to upload file: " + e.getMessage());
			extentTest.log(Status.FAIL, "ERROR: Failed to upload file: " + e.getMessage());
			throw new RuntimeException("Failed to upload file to element: " + element, e);
		}
	}

	/**
	 * Selects an option from dropdown by visible text with validation
	 * @param xpath XPath of the dropdown element
	 * @param text Visible text of the option to select
	 */
	public void selectFromDropdownByText(WebElement element, String text) {
		try {
			//WebElement element = getElement(xpath);
			Select dropdown = new Select(element);
			dropdown.selectByVisibleText(text);

			// Validate selection
			String selectedText = dropdown.getFirstSelectedOption().getText();
			if (!selectedText.equals(text)) {
				throw new RuntimeException("Failed to select option by text: " + text);
			}
			//	System.out.println("Selected option by text '" + text + "' from dropdown with XPath: " + we);
			extentTest.log(Status.INFO, "Selected option by text '" + text + "' from dropdown with XPath: " + element);
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to select by visible text: " + e.getMessage());
			extentTest.log(Status.FAIL, "ERROR: Failed to select by visible text: " + e.getMessage());
			throw new RuntimeException("Failed to select by visible text: " + element, e);
		}
	}

	/**
	 * Selects an option from dropdown by value with validation
	 * @param xpath XPath of the dropdown element
	 * @param value Value attribute of the option to select
	 */
	public void selectFromDropdownByValue(WebElement element, String value) {
		try {
			//WebElement element = getElement(xpath);
			Select dropdown = new Select(element);
			dropdown.selectByValue(value);

			// Validate selection
			String selectedValue = dropdown.getFirstSelectedOption().getAttribute("value");
			if (!selectedValue.equals(value)) {
				throw new RuntimeException("Failed to select option by value: " + value);
			}
			//System.out.println("Selected option by value '" + value + "' from dropdown with XPath: " + we);
			extentTest.log(Status.INFO,"Selected option by value '" + value + "' from dropdown with XPath: " + element);
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to select by value: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to select by value: " + e.getMessage());	
			throw new RuntimeException("Failed to select by value: " + element, e);
		}
	}

	/**
	 * Selects an option from dropdown by index with validation
	 * @param xpath XPath of the dropdown element
	 * @param index Index of the option to select
	 */
	public void selectFromDropdownByIndex(WebElement element, int index) {
		try {
			//WebElement element = getElement(xpath);
			Select dropdown = new Select(element);
			dropdown.selectByIndex(index);
			//System.out.println("Selected option by index " + index + " from dropdown with XPath: " + we);
			extentTest.log(Status.INFO,"Selected option by index " + index + " from dropdown with XPath: " + element);	
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to select by index: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to select by index: " + e.getMessage());	
			throw new RuntimeException("Failed to select by index: " + element, e);
		}
	}

	/**
	 * Gets the currently selected option text from a dropdown
	 * @param xpath XPath of the dropdown element
	 * @return Text of the selected option
	 */
	public String getSelectedDropdownOption(WebElement element) {
		try {
			//WebElement element = getElement(xpath);
			Select dropdown = new Select(element);
			String selectedText = dropdown.getFirstSelectedOption().getText();
			//System.out.println("Selected option text: '" + selectedText + "' from dropdown with XPath: " + we);
			extentTest.log(Status.INFO,"Selected option text: '" + selectedText + "' from dropdown with XPath: " + element);	
			return selectedText;
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to get selected dropdown option: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to get selected dropdown option: " + e.getMessage());	
			throw new RuntimeException("Failed to get selected dropdown option: " + element, e);
		}
	}

	/**
	 * Gets all available options from a dropdown
	 * @param xpath XPath of the dropdown element
	 * @return List of option texts
	 */
	public List<String> getAllDropdownOptions(WebElement element) {
		try {
			//WebElement element = getElement(xpath);
			Select dropdown = new Select(element);
			List<WebElement> options = dropdown.getOptions();
			List<String> optionTexts = options.stream()
					.map(WebElement::getText)
					.collect(java.util.stream.Collectors.toList());
			//System.out.println("Found " + optionTexts.size() + " options in dropdown with XPath: " + we);
			extentTest.log(Status.INFO,"Found " + optionTexts.size() + " options in dropdown with XPath: " + element);	
			return optionTexts;
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to get dropdown options: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to get dropdown options: " + e.getMessage());	
			throw new RuntimeException("Failed to get dropdown options: " + element, e);
		}
	}

	/**
	 * Checks if a specific option exists in the dropdown
	 * @param xpath XPath of the dropdown element
	 * @param optionText Text of the option to check
	 * @return true if option exists, false otherwise
	 */
	public boolean isDropdownOptionPresent(WebElement element, String optionText) {
		try {
			List<String> options = getAllDropdownOptions(element);
			boolean exists = options.contains(optionText);
			//System.out.println("Option '" + optionText + "' exists in dropdown: " + exists);
			extentTest.log(Status.INFO,"Option '" + optionText + "' exists in dropdown: " + exists);	
			return exists;
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to check dropdown option: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to check dropdown option: " + e.getMessage());	
			return false;
		}
	}


	/**
	 * Hovers over an element using Actions class
	 * @param xpath XPath of the element to hover over
	 */
	public void mouseOver(WebElement element) {
		try {
			Actions actions=new Actions(driver);

			//WebElement element = getElement(xpath);
			actions.moveToElement(element).perform();
			//System.out.println("Hovered over element with XPath: " + we);
			extentTest.log(Status.INFO,"Hovered over element with XPath: " + element);	
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to hover over element: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to hover over element: " + e.getMessage());	
			throw new RuntimeException("Failed to hover over element: " + element, e);
		}
	}


	/**
	 * Switches to the most recently opened window
	 */
	public void switchToNewWindow() {
		try {
			String mainWindow = driver.getWindowHandle();
			Set<String> allWindows = driver.getWindowHandles();

			for (String window : allWindows) {
				if (!window.equals(mainWindow)) {
					driver.switchTo().window(window);
					//System.out.println("Switched to new window: " + window);
					extentTest.log(Status.INFO,"Switched to new window: " + window);	
					return;
				}
			}
			throw new RuntimeException("No new window found");
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to switch to new window: " + e.getMessage());
			extentTest.log(Status.FAIL,"ERROR: Failed to switch to new window: " + e.getMessage());	
			throw new RuntimeException("Failed to switch to new window", e);
		}
	}

	/**
	 * Closes the current window and switches back to main window
	 */
	public void closeCurrentWindow() {
		try {
			String mainWindow = driver.getWindowHandle();
			Set<String> allWindows = driver.getWindowHandles();

			if (allWindows.size() > 1) {
				driver.close();
				driver.switchTo().window(mainWindow);
				//System.out.println("Closed current window and switched to main window");
				extentTest.log(Status.INFO, "Closed current window and switched to main window");	
			} else {
				//System.out.println("Only one window open, cannot close current window");
				extentTest.log(Status.INFO, "Only one window open, cannot close current window");	
			}
		} catch (Exception e) {
			//System.out.println("ERROR: Failed to close current window: " + e.getMessage());
			extentTest.log(Status.FAIL, "ERROR: Failed to close current window: " + e.getMessage());
			throw new RuntimeException("Failed to close current window", e);
		}
	}

	public String takeScreenshot(String testName) throws IOException {
		TakesScreenshot tss=(TakesScreenshot) driver;
		File file=tss.getScreenshotAs(OutputType.FILE);
		File fileToSave=new File("test-output\\"+testName+".png");
		Files.copy(file, fileToSave);
		String fullPathScreenshot=fileToSave.getAbsolutePath();
		return fullPathScreenshot;
	}


}