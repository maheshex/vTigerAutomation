package org.eva.application.testscripts;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eva.application.base.BaseClass;
import org.eva.application.pages.CreatLeadPage;
import org.eva.application.pages.HomePage;
import org.eva.application.pages.LeadInfoPage;
import org.eva.application.pages.LeadLandPage;
import org.eva.application.pages.LoginPage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.Assert;
import org.testng.annotations.Test;



public class VerifyLeadTestScript extends BaseClass {

	Logger logger=	LogManager.getLogger(VerifyLeadTestScript.class);




	@Test(retryAnalyzer = RetryAnalyzer.class)

	public void vt001VerifyCreateLead() {
		logger.info("vt001VerifyCreateLead testcase started");

		LoginPage login= new LoginPage(webUtil);
		webUtil.openURL("http://localhost:8888/");


		login.enterUserName("admin");
		login.enterUserPassword("admin");
		login.clickLoginButton();


		HomePage homePage=	new HomePage(webUtil);
		homePage.clickLeads();


		LeadLandPage clickplusb= new LeadLandPage(webUtil);
		clickplusb.clickPlusButton();


		CreatLeadPage creat = new CreatLeadPage(webUtil);
		creat.enterFirstName("Rohit");
		creat.enterLastName("Kumar");
		creat.enterCompanyName("Tech Pvt Ltd");
		creat.enterMobileNumber("7007454374");
		creat.enterEmail("bhadohiupe75@gmail.com");
		creat.street("Main road");
		creat.postBox("221404");
		creat.city("Bhadohi");
		creat.country("India");
		creat.firstNameSalutation("Mr.");
		creat.postalCode("221401");
		creat.state("U.P");
		creat.leadStatus("Contacted");
		creat.description("This is my first test description");
		creat.industry("Education");
		creat.clickOnSaveButton();
		//webUtil.navigateBack();
		//webUtil.navigateBack();



		// Step 5: Validate lead info
		LeadInfoPage leadInfo = new LeadInfoPage(webUtil);

		String actualHeader = leadInfo.getLeadHeader();
		String actualLastName = leadInfo.getLastName();
		String actualCompany = leadInfo.getCompanyName();


		System.out.println("🔍 Validating Lead Information...");
		System.out.println("Header Text: " + actualHeader);
		System.out.println("Last Name: " + actualLastName);
		System.out.println("Company: " + actualCompany);

		// Assertions for validation


		Assert.assertTrue(actualHeader.contains("Lead Information"), "❌ Header validation failed!");
		Assert.assertEquals(actualLastName, "Kumar", "❌ Last Name not matched!");
		Assert.assertEquals(actualCompany, "Tech Pvt Ltd", "❌ Company not matched!");

		System.out.println("✅ Lead created and validated successfully!");

	}

	@Test
	public void vt002VerifyLeadCreationMandatory() {

		logger.info("vt002VerifyLeadCreationMandatory testcase started");



		LoginPage login= new LoginPage(webUtil);
		webUtil.openURL("http://localhost:8888/");


		login.enterUserName("admin");
		login.enterUserPassword("admin");
		login.clickLoginButton();


		HomePage homePage=	new HomePage(webUtil);
		homePage.clickLeads();


		LeadLandPage clickplusb= new LeadLandPage(webUtil);
		clickplusb.clickPlusButton();

		CreatLeadPage creat = new CreatLeadPage(webUtil);

		creat.firstNameSalutation("Mr.");
		creat.enterFirstName("Raja");
		creat.enterLastName("Yadav");
		creat.enterCompanyName("AKTU");
		creat.leadStatus("Lost Lead");
		creat.clickOnSaveButton();

	}	
	@Test
	public void vt003VerifyLeadCreationInvalidData() {

		logger.info("vt003VerifyLeadCreationInvalidData testcase started");

		System.out.println("🧪 Starting test: Verify invalid data validation in Lead Creation...");


		LoginPage login= new LoginPage(webUtil);
		webUtil.openURL("http://localhost:8888/");


		login.enterUserName("admin");
		login.enterUserPassword("admin");
		login.clickLoginButton();


		HomePage homePage=	new HomePage(webUtil);
		homePage.clickLeads();


		LeadLandPage clickplusb= new LeadLandPage(webUtil);
		clickplusb.clickPlusButton();

		CreatLeadPage creat = new CreatLeadPage(webUtil);

		creat.firstNameSalutation("Mr.");
		creat.enterFirstName("Sachin");
		creat.enterLastName(" ");
		creat.enterCompanyName("123@");
		creat.leadStatus("Lost Lead");
		creat.enterMobileNumber("abc123");
		creat.enterCompanyName("abcd@");
		creat.clickOnSaveButton();


		//  Handle alert and validate message
		try {
			Alert alert = webUtil.getDriver().switchTo().alert();
			String alertText = alert.getText();
			System.out.println("Alert message: " + alertText);

			// Assertion for expected messages
			Assert.assertTrue(alertText.contains("Last Name cannot be empty") ||
					alertText.contains("Enter valid Company Name") ||
					alertText.contains("Enter valid Mobile Number"),
					"❌ Unexpected alert message!");

			alert.accept(); // Close the alert
			System.out.println("✅ Alert handled successfully.");
		} catch (NoAlertPresentException e) {
			System.out.println("❌ No alert present! Validation may not be working.");
			Assert.fail("Expected alert for invalid data was not displayed.");
		}

		System.out.println("✅ Invalid data validation test completed.");
	}


	public void vt004VerifyDeleteLead() throws InterruptedException {

		logger.info("vt004VerifyDeleteLead testcase started");


		LoginPage login= new LoginPage(webUtil);
		webUtil.openURL("http://localhost:8888/");


		login.enterUserName("admin");
		login.enterUserPassword("admin");
		login.clickLoginButton();

		HomePage homePage=	new HomePage(webUtil);
		homePage.clickLeads();

		LeadLandPage leadLandPage=	new LeadLandPage(webUtil);
		leadLandPage.selectlead();
		Thread.sleep(3000);
		leadLandPage.clickOnDeleteButton(); 



	}



}
