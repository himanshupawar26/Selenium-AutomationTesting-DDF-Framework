package com.DataDrivenFramework.test.TestLogin;

import java.util.Hashtable;

import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.DataDrivenFramework.base.BaseUI;
import com.DataDrivenFramework.utils.TestDataProvider;
import com.aventstack.extentreports.Status;

public class LoginTest extends BaseUI{
	
	
	
	@Test(dataProvider="getTestOneData")
	public void test1(Hashtable<String,String> table) {
		
		
		logger = report.createTest("test1");
		
		
		invokeBrowser("chrome");
		openURL("url");
		click("signInBtn_xpath");
	//	enterText("user_xpath", "wrushu");
	//	enterText("pass_xpath", "wrushu123");
		
		//using dataProvider --testData2 excel col1 as user n col2 as pass 
		enterText("user_xpath",table.get("col1"));
		enterText("pass_xpath",table.get("col2"));
		
		logger.log(Status.PASS,"test success ");
		
		quitBrowser();
	}
	
	@AfterTest
	public void endReport() {
		report.flush();
	}
	
	@DataProvider
	public Object[][] getTestOneData(){
		return TestDataProvider.getTestData("testData2.xlsx", "Feature1", "test One");
	}
	
	@Test
    public void test2() {
		
	}
	
	@Test
   public void test3() {
	
    }

}
