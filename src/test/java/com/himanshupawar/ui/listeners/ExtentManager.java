package com.himanshupawar.ui.listeners;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * One ExtentReports instance per JVM, written to a fixed path under
 * {@code target/} (generated, gitignored) so CI can upload/publish it
 * without parsing a timestamped filename — the old framework named each
 * report run-{timestamp}.html, which worked for a human browsing
 * test-output/ locally but not for a CI step needing one stable path.
 */
public final class ExtentManager {

    private static final String REPORT_PATH = "target/extent-report/index.html";
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            new File("target/extent-report").mkdirs();
            ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_PATH);
            spark.config().setDocumentTitle("Selenium DDF Framework — Test Report");
            spark.config().setReportName("Sauce Demo UI Regression");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Target", "https://www.saucedemo.com");
            extent.setSystemInfo("Framework", "Selenium 4 + TestNG");
        }
        return extent;
    }

    public static void setTest(ExtentTest test) {
        CURRENT_TEST.set(test);
    }

    public static ExtentTest getTest() {
        return CURRENT_TEST.get();
    }

    public static void unload() {
        CURRENT_TEST.remove();
    }
}
