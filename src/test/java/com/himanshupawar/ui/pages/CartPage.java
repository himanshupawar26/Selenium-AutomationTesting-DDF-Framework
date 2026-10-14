package com.himanshupawar.ui.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    // .inventory_item_name is reused on both the inventory and cart pages.
    // Waiting on .cart_list first — present only on the cart page — avoids a
    // race right after the SPA navigation from inventory.html to cart.html,
    // where the wait below could otherwise resolve against the outgoing
    // inventory page's own elements of the same class a split second before
    // they're torn down, producing a StaleElementReferenceException.
    private static final By CART_LIST = By.cssSelector("[data-test='cart-list']");
    private static final By CART_ITEM_NAMES = By.className("inventory_item_name");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getItemNames() {
        waitVisible(CART_LIST);
        return waitAllVisible(CART_ITEM_NAMES).stream().map(el -> el.getText()).toList();
    }
}
