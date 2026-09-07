package org.eva.application.pages;

import org.eva.application.utils.WebUtils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
	
	private WebUtils wu;
	
	
	public HomePage(WebUtils we) {
		this.wu=we;
		PageFactory.initElements(wu.getDriver(), this);
	}

	// Locators
    @FindBy(xpath = "//a[@href='index.php?module=Leads&action=index']")
    private WebElement ClickLeadsLink;

    // Methods
    public void clickLeads() {
        wu.click(ClickLeadsLink);
    }
    
	
	
	
}
