package org.eva.application.testscripts;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{

	 private int retryCount = 0;       // retry counter
	    private int maxRetryCount = 2;
	
	@Override
	public boolean retry(ITestResult result) {
		if(retryCount <maxRetryCount ) {
			retryCount++;
			System.out.println("🔄 Retrying test: " + result.getName() + " | Attempt: " + retryCount);
			return true;
		}
		return false;
	}
	

}
