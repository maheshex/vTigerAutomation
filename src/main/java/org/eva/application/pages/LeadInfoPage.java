package org.eva.application.pages;

import org.eva.application.utils.WebUtils;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebElement;


public class LeadInfoPage {

	private WebUtils wu;

	public LeadInfoPage (WebUtils wb) {
		this.wu=wb;

		PageFactory.initElements(wu.getDriver(), this);

	}
	
	//Validation Steps:

//Verify success message is displayed indicating lead was created successfully

//Verify user is redirected to lead detail view page

//Validate all entered data is correctly displayed on detail page:

//First Name and Last Name match input values

//Company name matches input value

//Lead status matches selected value

//Email and phone values are correct

//Address information is properly saved

//Description text is preserved

//Verify lead is visible in Leads list view

//Verify lead ID is generated and displayed


	
	
	
	
	// Lead Information fields (after save)
    @FindBy(xpath = "//span[@id='dtlview_Last Name']")
    private WebElement lastNameText;

    @FindBy(xpath = "//span[@id='dtlview_Company']")
    private WebElement companyText;

    @FindBy(xpath = "//span[contains(text(),'Lead Information')]")
    private WebElement leadHeaderText;

    @FindBy(xpath = "//input[@id='email']")
    private WebElement email;

    @FindBy(xpath = "//span[contains(@class,'lvtHeaderText')]")
    private WebElement successMessage;
	
	
    // Methods to get text for validation
    public String getLastName() {
        return wu.getText(lastNameText);
    }
    public String getCompanyName() {
        return wu.getText(companyText);
    }
    
    public String getLeadHeader() {
        return wu.getText(leadHeaderText);
    }
    public String getEmail() {
        return wu.getAttribute(email, "value");
    }

    public String getSuccessMessage() {
        return wu.getText(successMessage);
    }
    
    
	
}
