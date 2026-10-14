package com.himanshupawar.ui.driver;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Owns one WebDriver instance per test thread. Selenium Manager (built into
 * Selenium since 4.6) resolves and downloads the matching browser driver at
 * runtime — the old framework pointed {@code webdriver.chrome.driver} etc. at
 * committed, Windows-only .exe binaries that only ever worked on one machine.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialized for this thread — call initDriver() first");
        }
        return driver;
    }

    public static void initDriver(String browser, boolean headless) {
        WebDriver driver = switch (browser.toLowerCase()) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                yield new FirefoxDriver(options);
            }
            default -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1920,1080", "--disable-gpu", "--no-sandbox");
                yield new ChromeDriver(options);
            }
        };
        // No implicit wait: every page object waits explicitly for the
        // condition it actually needs, which is what keeps assertion
        // failures meaningful instead of being masked by a blanket timeout.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        DRIVER.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
