//package org.eva.application.testscripts;
//
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.reporter.ExtentSparkReporter;
//
//public class TestReports {
//	
//	public static void main (String[]args) {
//		
//		ExtentReports ext=	new ExtentReports();
//		ExtentSparkReporter spark =	new ExtentSparkReporter("application_report.html");
//		ext.attachReporter(spark);
//		
//		
//	ExtentTest extentTest =	ext.createTest("testLogin");
//	extentTest.log(Status.PASS, "username is correct");
//	extentTest.log(Status.FAIL, "password is incorrect");
//	extentTest.log(Status.INFO, "password is incorrect");
//	
//	ext.flush();
//		
//		
//		
//	}
//
//}
