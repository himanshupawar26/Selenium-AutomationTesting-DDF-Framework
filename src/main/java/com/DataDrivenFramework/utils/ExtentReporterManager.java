package com.DataDrivenFramework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;

public class ExtentReporterManager {
	
	//public static ExtentHtmlReporter htmlreporter;
	public static ExtentReports report;
	
	public static ExtentReports getReportInstance() {
		if ( report == null){
			String reportName= DateUtils.getTimeStamp()+".html";
			
			ExtentHtmlReporter htmlreporter = new ExtentHtmlReporter(System.getProperty("user.dir")+"\\test-output\\"+reportName);
			report =  new ExtentReports();
			report.attachReporter(htmlreporter);
			
			report.setSystemInfo("OS", "win 10");
			report.setSystemInfo("env", "uat");
			report.setSystemInfo("build", "20.10");
			report.setSystemInfo("OS", "chrome");
			
			htmlreporter.config().setDocumentTitle("UAT UI automation result");
			htmlreporter.config().setReportName("ALL head TEST REPORTS");
			htmlreporter.config().setTestViewChartLocation(ChartLocation.TOP);
			htmlreporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
			
			
		}
		return report;
	}
}
