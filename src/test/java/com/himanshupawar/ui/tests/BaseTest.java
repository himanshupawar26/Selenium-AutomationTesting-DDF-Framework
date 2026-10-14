package com.himanshupawar.ui.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.himanshupawar.ui.config.Config;
import com.himanshupawar.ui.driver.DriverFactory;

/**
 * One WebDriver per test method, via {@link DriverFactory}'s ThreadLocal —
 * what makes {@code testng.xml}'s {@code parallel="methods"} safe. The old
 * framework's single {@code driver} field on a shared base class would hand
 * two parallel tests the same browser instance.
 */
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver(Config.browser(), Config.headless());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }
}
