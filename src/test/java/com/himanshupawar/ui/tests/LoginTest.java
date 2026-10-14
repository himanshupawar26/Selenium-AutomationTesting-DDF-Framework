package com.himanshupawar.ui.tests;

import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.himanshupawar.ui.config.Config;
import com.himanshupawar.ui.pages.InventoryPage;
import com.himanshupawar.ui.pages.LoginPage;
import com.himanshupawar.ui.utils.CsvDataReader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Data-driven login coverage: every row in {@code testdata/login-data.csv} is
 * an independent TestNG test case (own row in the report, own pass/fail),
 * not one method with a hardcoded username/password like the old
 * {@code LoginTest.test1} plus two empty placeholder methods.
 */
public class LoginTest extends BaseTest {

    @DataProvider(name = "loginCases")
    public Object[][] loginCases() {
        return CsvDataReader.readAsMaps("testdata/login-data.csv");
    }

    @Test(dataProvider = "loginCases", description = "Login outcome matches the expected result for each credential case")
    public void loginOutcomeMatchesExpectedResult(Map<String, String> testCase) {
        LoginPage loginPage = new LoginPage(driver()).open(Config.baseUrl());
        loginPage.login(testCase.get("username"), testCase.get("password"));

        if ("success".equals(testCase.get("expectedResult"))) {
            assertThat(new InventoryPage(driver()).isLoaded())
                    .as("case '%s': expected login to land on the inventory page", testCase.get("caseName"))
                    .isTrue();
        } else {
            assertThat(loginPage.hasError())
                    .as("case '%s': expected a login error to be shown", testCase.get("caseName"))
                    .isTrue();
            assertThat(loginPage.getErrorMessage())
                    .as("case '%s': error message content", testCase.get("caseName"))
                    .containsIgnoringCase(testCase.get("expectedMessageContains"));
        }
    }
}
