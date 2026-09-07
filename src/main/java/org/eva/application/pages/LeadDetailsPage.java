package org.eva.application.pages;

import org.eva.application.utils.WebUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LeadDetailsPage {

	private WebUtils wu;


	public LeadDetailsPage(WebUtils wb) {

		this.wu=wb;
		
		PageFactory.initElements(wu.getDriver(), this);
	}


	@FindBy(xpath="//span[@id='dtlview_Last Name']")
	private WebElement leadName;

	@FindBy(xpath="//span[@id='dtlview_Company']")
	private WebElement companyName;

	



	// Get Lead Name
	public String getLeadName() {
		return wu.getText(leadName);  
	}

	// Get Company Name
	public String getCompanyName() {
		return wu.getText(companyName);
	}

	
	
	




}
