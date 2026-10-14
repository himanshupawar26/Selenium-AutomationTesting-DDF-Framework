package com.himanshupawar.ui.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.himanshupawar.ui.driver.DriverFactory;
import com.himanshupawar.ui.utils.ScreenshotUtil;

/**
 * Drives the Extent report from TestNG's own lifecycle, instead of scattering
 * {@code logger.log(...)} calls through page objects and test methods (which
 * is what the old {@code BaseUI} did). A screenshot is captured only on
 * failure, and only here — one place, one responsibility.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.getInstance().createTest(
                result.getMethod().getMethodName(), result.getMethod().getDescription());
        ExtentManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        test.log(Status.FAIL, result.getThrowable());
        try {
            String screenshot = ScreenshotUtil.captureBase64(DriverFactory.getDriver());
            test.fail("Screenshot on failure",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build());
        } catch (Exception e) {
            test.log(Status.WARNING, "Could not capture failure screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().log(Status.SKIP, "Test skipped: "
                + (result.getThrowable() != null ? result.getThrowable().getMessage() : "no reason given"));
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getInstance().flush();
        ExtentManager.unload();
    }
}
