package com.himanshupawar.ui.tests;

import java.util.List;

import org.testng.annotations.Test;

import com.himanshupawar.ui.config.Config;
import com.himanshupawar.ui.pages.CartPage;
import com.himanshupawar.ui.pages.InventoryPage;
import com.himanshupawar.ui.pages.LoginPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Functional coverage beyond login: sorting and cart behavior. Deliberately
 * not data-driven — these two behaviors don't vary across rows of input the
 * way login credentials do, so a DataProvider here would just add
 * indirection without adding coverage.
 */
public class InventoryTest extends BaseTest {

    private static final String STANDARD_USER = "standard_user";
    private static final String STANDARD_PASSWORD = "secret_sauce";

    // Deliberately NOT a field written in @BeforeMethod and read in @Test:
    // with testng.xml's parallel="methods", TestNG runs both test methods
    // below on one shared InventoryTest instance. A shared instance field
    // set by one thread's @BeforeMethod can be overwritten by the other
    // thread's @BeforeMethod before the first thread's @Test reads it, so
    // each @Test method logs in and builds its own page object locally —
    // a stack-local variable per invocation, safe regardless of instance
    // sharing. (This is the same class of bug the BDD sibling repo's README
    // calls out for the old framework's shared static field.)
    private InventoryPage loginAsStandardUser() {
        LoginPage loginPage = new LoginPage(driver()).open(Config.baseUrl());
        loginPage.login(STANDARD_USER, STANDARD_PASSWORD);
        InventoryPage inventoryPage = new InventoryPage(driver());
        assertThat(inventoryPage.isLoaded()).as("precondition: standard_user login succeeded").isTrue();
        return inventoryPage;
    }

    @Test(description = "Sorting by price low-to-high orders every product ascending")
    public void sortByPriceLowToHigh_ordersProductsAscending() {
        InventoryPage inventoryPage = loginAsStandardUser();
        List<Double> prices = inventoryPage.sortBy("Price (low to high)").getProductPricesInOrder();

        assertThat(prices).isNotEmpty();
        assertThat(prices).isSortedAccordingTo(Double::compareTo);
    }

    @Test(description = "Adding products updates the cart badge count and the cart page contents")
    public void addProductsToCart_updatesBadgeAndCartContents() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        inventoryPage.addProductToCart("Sauce Labs Bike Light");

        assertThat(inventoryPage.getCartCount()).isEqualTo(2);

        CartPage cartPage = inventoryPage.goToCart();
        assertThat(cartPage.getItemNames())
                .containsExactlyInAnyOrder("Sauce Labs Backpack", "Sauce Labs Bike Light");
    }
}
