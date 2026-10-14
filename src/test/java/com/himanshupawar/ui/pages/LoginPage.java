package com.himanshupawar.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        waitVisible(USERNAME_INPUT);
        return this;
    }

    public void login(String username, String password) {
        type(USERNAME_INPUT, username == null ? "" : username);
        type(PASSWORD_INPUT, password == null ? "" : password);
        click(LOGIN_BUTTON);
    }

    public boolean hasError() {
        return isVisible(ERROR_MESSAGE);
    }

    public String getErrorMessage() {
        return textOf(ERROR_MESSAGE);
    }
}
