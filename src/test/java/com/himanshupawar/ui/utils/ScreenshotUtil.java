package com.himanshupawar.ui.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Captures failure screenshots as base64 and embeds them directly in the
 * Extent report. The old framework wrote a PNG per failure to a committed
 * {@code screenshot/} directory — this keeps the report self-contained and
 * leaves nothing behind for CI (or a developer's working tree) to clean up.
 */
public final class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    public static String captureBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
}
