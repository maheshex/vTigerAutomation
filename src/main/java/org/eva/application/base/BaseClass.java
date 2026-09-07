package org.eva.application.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.eva.application.utils.WebUtils;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class BaseClass {

	protected WebUtils webUtil;
	private static ExtentReports extentReports;
	private ExtentTest extentTest;
	@BeforeSuite
	public void beforeSuit() {
		String x=new File("D:\\PritiMadam\\application_Automate\\Reports\\extent.html").getAbsolutePath();
		System.out.println(x);
		extentReports=new ExtentReports();
		ExtentSparkReporter	extentSparkReporter = new ExtentSparkReporter(x);
		extentReports.attachReporter(extentSparkReporter);
	}



	@Parameters({"browser"})
	@BeforeMethod
	public void beforeTestCase(String browserName, Method method) throws IllegalAccessException  {
		webUtil=new WebUtils();
		String testName = method.getName();
		extentTest  = extentReports.createTest(testName);
		webUtil.setExtentTest(extentTest);
		webUtil.launchBrowser(browserName);
	}
    @AfterMethod
	public void afterTestCase(ITestResult itr, Method mt) throws IOException {
	    if(!itr.isSuccess()) {
	        String testName=mt.getName();
	    	String screenshotPath=webUtil.takeScreenshot(testName);
		    extentTest.addScreenCaptureFromPath(screenshotPath);

	    }
	    webUtil.closeBrowser();

		
		extentReports.flush();
	}



}