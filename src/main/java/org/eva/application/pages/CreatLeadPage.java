package org.eva.application.pages;



import org.eva.application.utils.WebUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import io.opentelemetry.api.common.Value;
import net.bytebuddy.asm.Advice.This;

public class CreatLeadPage {
	
	private WebUtils wu;
	
	
	
	public CreatLeadPage (WebUtils wb) {
		
		this.wu=wb;
		
		PageFactory.initElements(wu.getDriver(), this);
		
	}
	
	 // Locators
	@FindBy(xpath = "//select[@name='salutationtype']")
    private WebElement firstNameSalutation;

	
    @FindBy(xpath = "//input[@name='firstname']")
    private WebElement firstName;

    @FindBy(xpath = "//input[@name='lastname']")
    private WebElement lastName;

    @FindBy(xpath = "//input[@name='company']")
    private WebElement companyName;

    @FindBy(xpath = "//input[@name='mobile']")
    private WebElement mobileNumber;
    
    @FindBy(xpath = "//input[@id='email']")
    private WebElement email;
    
    @FindBy(xpath = "//select[@name='leadstatus']")
    private WebElement leadStatus;
    
    @FindBy(xpath = "//textarea[@class='detailedViewTextBox']")
    private WebElement street;
    
    @FindBy(xpath = "//input[@id='pobox']")
    private WebElement poBox;
    
    @FindBy(xpath = "//input[@id='code']")
    private WebElement PostalCode;
    
    @FindBy(xpath = "//input[@id='city']")
    private WebElement City;
    
    @FindBy(xpath = "//input[@id='country']")
    private WebElement Country;
    
    @FindBy(xpath = "//input[@id='state']")
    private WebElement State;
    
    @FindBy(xpath = "//textarea[@name='description']")
    private WebElement Description;
    
    
    @FindBy(xpath = "//select[@name='industry']")
    private WebElement Industry;
    
    @FindBy(xpath="//input[@class='crmButton small save']")
    private WebElement saveButton;
    
    @FindBy(xpath = "//span[@class='errorMessage']")
    private WebElement validationMsg;

    

    // Methods to interact with elements
    
    public void firstNameSalutation(String value) {
		wu.selectFromDropdownByText(firstNameSalutation, value);
		
	}
    
    public void enterFirstName(String value) {
        wu.sendKeys(firstName, value);
    }

    public void enterLastName(String value) {
        wu.sendKeys(lastName, value);
    }

    public void enterCompanyName(String value) {
        wu.sendKeys(companyName, value);
    }

    public void enterMobileNumber(String value) {
        wu.sendKeys(mobileNumber, value);
    }
	
	public void enterEmail(String value) {
		wu.sendKeys(email, value);	
	}
	
	public void leadStatus(String value) {
		wu.selectByVisibleText(leadStatus, value);	
	}
 
	public void street(String value) {
		wu.sendKeys(street, value);	
	}
	
	public void postBox(String value) {
		wu.sendKeys(poBox, value);
		
	}
	
	public void postalCode(String value) {
		wu.sendKeys(PostalCode, value);
		
	}
	
	public void city(String value) {
		wu.sendKeys(City, value);
		
	}
	
	public void country(String value) {
		wu.sendKeys(Country, value);
		
	}
	
	public void state(String value) {
		wu.sendKeys(State, value);
		
	}
	
	public void description(String value) {
		wu.sendKeys(Description, value);
		
	}
	
	public void industry(String value) {
		wu.selectFromDropdownByText(Industry, value);
		
	}
	
	public void clickOnSaveButton() {
		wu.click(saveButton);
	}
	
	 // 8️⃣ Get Error Message for Validation
    public String getErrorMessage() {
        try {
            return wu.getText(validationMsg);
        } catch (Exception e) {
            return "❌ No validation message found!";
        }
	
	
    }
	
}
