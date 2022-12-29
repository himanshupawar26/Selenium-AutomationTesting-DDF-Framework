package com.DataDrivenFramework.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.asserts.SoftAssert;

import com.DataDrivenFramework.utils.DateUtils;
import com.DataDrivenFramework.utils.ExtentReporterManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class BaseUI {
	
	public WebDriver driver;
	public Properties prop;
	public ExtentReports report = ExtentReporterManager.getReportInstance();
	public ExtentTest logger;
	
	SoftAssert softAssert = new SoftAssert();
	
	public void invokeBrowser(String browser) {
		try {
		if(browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver","C:\\Users\\himanshu.pawar\\eclipse-workspace\\DataDrivenFramework\\src\\test\\resources\\driver\\chromedriver.exe");
			driver = new ChromeDriver();
		}else if (browser.equalsIgnoreCase("mozilla")) {
			System.setProperty("webdriver.gecko.driver","C:\\Users\\himanshu.pawar\\eclipse-workspace\\DataDrivenFramework\\src\\test\\resources\\driver\\geckodriver.exe");
			driver = new FirefoxDriver();
		}else {
			System.setProperty("webdriver.ie.driver","C:\\Users\\himanshu.pawar\\eclipse-workspace\\DataDrivenFramework\\src\\test\\resources\\driver\\msedgedriver.exe");
			driver = new EdgeDriver();
		}
	
		}catch(Exception e) {
			reportFail(e.getMessage());
		}
		
		
		if(prop==null) {
			prop = new Properties();
			try {
				FileInputStream file = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\objectRepository\\projectConfig.properties");
				prop.load(file);
				
			} catch (Exception e) {
				reportFail(e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	
	public void openURL(String url) {
		try {
		driver.get(prop.getProperty(url));
		reportPass(url+"IDENTIFIED");
		
	}catch (Exception e) {
		reportFail(e.getMessage());
		e.printStackTrace();
	}
	}
	public void tearDown() {
		driver.close();
		
	}
	
     public void quitBrowser() {
		driver.quit();
	}
    
    public void enterText(String xpath, String data) {
    	try {
    	getElement(xpath).sendKeys(data);
    	reportPass(data+"entered"+xpath);
    	}catch(Exception e) {
    		reportFail(e.getMessage());
    		
    	}
    }
    
   public void click(String xpath) {
    	
	   try {
	    	getElement(xpath).click();
	    	reportPass("locator identified"+xpath);
	    	}catch(Exception e) {
	    		reportFail(e.getMessage());
    }
   }
/// centralise locators function for finding webelement ///
   
   public WebElement getElement(String locatorKey) {
	   WebElement element =  null;
	   try {
	   if(locatorKey.endsWith("_id")) {
		   element = driver.findElement(By.id(prop.getProperty(locatorKey)));
		   logger.log(Status.INFO,"LOCATOR ::"+locatorKey);
	   }else if(locatorKey.endsWith("_xpath")) {
		   element = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
		   logger.log(Status.INFO,"LOCATOR ::"+locatorKey);
	   }else if(locatorKey.endsWith("_css")) {
		   element = driver.findElement(By.cssSelector(prop.getProperty(locatorKey)));
		   logger.log(Status.INFO,"LOCATOR ::"+locatorKey);
	   }
	   
	   
   }catch(Exception e) {
	   
	   reportFail(e.getMessage());
	   e.printStackTrace();
	   
	   Assert.fail("failing the test");
   }
	   return element;   

}
   
///   ///verify any element///
   public boolean isElementPresent(String locatorKey) {
	   try {
		   getElement(locatorKey).isDisplayed();
		   reportPass(locatorKey+"identified");
		   return true;
	   }catch(Exception e ) {
		   reportFail(e.getMessage());
	   } return false;
   }
   
   public boolean isElementSelected(String locatorKey) {
	   try {
		   getElement(locatorKey).isSelected();
		   reportPass(locatorKey+"identified");
		   return true;
	   }catch(Exception e ) {
		   reportFail(e.getMessage());
	   } return false;
   }
   
   public boolean isElementEnable(String locatorKey) {
	   try {
		   getElement(locatorKey).isEnabled();
		   reportPass(locatorKey+"identified");
		   return true;
	   }catch(Exception e ) {
		   reportFail(e.getMessage());
	   } return false;
   }
 
///  /// asertion methods///
   public void asserttrue(boolean flag) {
		   softAssert.assertTrue(flag);
   }
   
   public void assertfalse(boolean flag) {
	   softAssert.assertFalse(flag);
}
   public void assertequals(String act, String exp) {
	   softAssert.assertEquals(act,exp);
}
   @AfterMethod
   public void afterTest() {
	   softAssert.assertAll();
   }
   
///   ///reporting methods////
   public void reportFail(String reportString) {
	   logger.log(Status.FAIL,reportString);
	   takeScreenshotOnFail();
	   Assert.fail(reportString);
	   
   }
   
   public void reportPass(String reportString) {
	   logger.log(Status.PASS,reportString);
   }
   
////   //// screenshot ///
   public void takeScreenshotOnFail() {
	   TakesScreenshot takescreenshot = (TakesScreenshot )driver;
	   File srcFile = takescreenshot.getScreenshotAs(OutputType.FILE);
	   
	   
	   File destFile = new File(System.getProperty("user.dir")+"\\screenshot\\"+DateUtils.getTimeStamp()+".png");
	   
	   try {
		FileUtils.copyFile(srcFile, destFile);
		logger.addScreenCaptureFromPath(System.getProperty("user.dir")+"\\screenshot\\"+DateUtils.getTimeStamp()+".png");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	   
   }
}