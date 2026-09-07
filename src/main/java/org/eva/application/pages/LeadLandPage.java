package org.eva.application.pages;

import java.util.List;

import org.eva.application.utils.WebUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class LeadLandPage {
	
	private WebUtils wu;

	public LeadLandPage(WebUtils wb) {
		
		this.wu=wb;	
		
		PageFactory.initElements(wu.getDriver(), this);
	}

	/////// @FindBy use of annotation..............

	@FindBy(xpath="//img[@src=\'themes/softed/images/btnL3Add.gif\']")
	private WebElement clickPlusButton;

	@FindBy(xpath="//input[@value='Delete']")
	private WebElement deleteBtn;
	
	@FindBy(xpath="//input[@value='Edit']")
	private WebElement editBtn;

	@FindBy(xpath = "//input[@id='row_56']")
	private WebElement selectlead;
	
	public void selectlead() {
		wu.click(selectlead);
	}
	
	

	public void clickPlusButton() {
		wu.click(clickPlusButton);
	}
	public void clickOnDeleteButton() {
		wu.click(deleteBtn);
		wu.acceptAlert();
		 System.out.println("Clicked Delete and accepted alert.");
	}
	public void clickEdit() {
		wu.click(editBtn);
	}
	 // Select the first lead checkbox in the table
    public void selectFirstLeadCheckbox() {
        try {
            By firstCheckbox = By.xpath("//table[@class='lvt small']/tbody/tr[2]/td[1]/input[@type='checkbox']");
            WebElement element = wu.getDriver().findElement(firstCheckbox);
            wu.click(element);
            System.out.println("✅ Selected the first lead in the list.");
        } catch (Exception e) {
            System.out.println("❌ No lead available to select!");
            throw e;
        }
    }

    
    public String getFirstLeadName() {
        try {
            By firstLeadNameLocator = By.xpath("//table[@class='lvt small']/tbody/tr[2]/td[3]/a");
            WebElement element = wu.getDriver().findElement(firstLeadNameLocator);
            return wu.getText(element);
        } catch (Exception e) {
            System.out.println("❌ Unable to get first lead name.");
            throw e;
        }
    }

    // Click a lead by name to open its details page
    public void clickLeadByName(String leadName) {
        try {
            By leadLinkLocator = By.xpath("//a[text()='" + leadName + "']");
            WebElement leadLink = wu.getDriver().findElement(leadLinkLocator);
            wu.click(leadLink);
            System.out.println("Opened lead details for: " + leadName);
        } catch (Exception e) {
            System.out.println("❌ Lead not found: " + leadName);
            throw e;
        }
    }

    // Check if a lead is present in the list
    public boolean isLeadPresent(String leadName) {
        try {
            By leadLocator = By.xpath("//a[text()='" + leadName + "']");
            List<WebElement> elements = wu.getDriver().findElements(leadLocator);
            return !elements.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
	
	
	
	
	
	
	
	
	
	
	
}
