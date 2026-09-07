package org.eva.application.pages;

import java.time.Duration;

import org.eva.application.utils.WebUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

	private WebUtils wu;
	private WebDriverWait wait;


	public LoginPage (WebUtils wb) {

		this.wu=wb;

		PageFactory.initElements(wu.getDriver(), this);
		
		 this.wait = new WebDriverWait(wu.getDriver(), Duration.ofSeconds(10));
	}

	/////// @FindBy use of annotation..............

	@FindBy(xpath="//input[@name='user_name']")
	private WebElement username;

	@FindBy(xpath="//input[@name='user_password']")
	private WebElement userpassword;

	@FindBy(xpath ="//input[@id='submitButton']")
	private WebElement loginButtonClickElement;

	@FindBy(xpath = "//img[@src='themes/softed/images/Home.PNG']")  // Example: dashboard header
	private WebElement homeHeader;

	@FindBy(xpath = "//div[contains(@class,'successMessage')]") // Agar toast success message ho
	private WebElement successMessage;



	public void enterUserName(String value) {
		wu.sendKeys(username, value);


	}

	public void enterUserPassword(String value) {
		wu.sendKeys(userpassword, value);
	}


	public void clickLoginButton() {

		  wait.until(ExpectedConditions.elementToBeClickable(loginButtonClickElement));
	        wu.click(loginButtonClickElement); 
	
	}

	public String getHomeHeader() {
	    return wu.getText(homeHeader);
	}

	public String getSuccessMessage() {
	    return wu.getText(successMessage);
	}

}
