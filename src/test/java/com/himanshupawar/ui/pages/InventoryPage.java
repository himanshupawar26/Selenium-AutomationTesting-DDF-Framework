package com.himanshupawar.ui.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class InventoryPage extends BasePage {

    private static final By INVENTORY_LIST = By.className("inventory_list");
    private static final By SORT_SELECT = By.cssSelector("[data-test='product-sort-container']");
    private static final By PRODUCT_NAMES = By.className("inventory_item_name");
    private static final By PRODUCT_PRICES = By.className("inventory_item_price");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By CART_LINK = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(INVENTORY_LIST);
    }

    /** @param visibleOptionText e.g. "Price (low to high)" */
    public InventoryPage sortBy(String visibleOptionText) {
        new Select(waitVisible(SORT_SELECT)).selectByVisibleText(visibleOptionText);
        return this;
    }

    public List<Double> getProductPricesInOrder() {
        return waitAllVisible(PRODUCT_PRICES).stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", "").trim()))
                .toList();
    }

    public void addProductToCart(String productName) {
        // Each product's "Add to cart" button carries a stable data-test
        // attribute derived from its name (e.g. "Sauce Labs Backpack" ->
        // "add-to-cart-sauce-labs-backpack"), so the slug is computed
        // instead of walking the DOM from the name element — no dependency
        // on inventory_item_name's exact class string (it ships with a
        // trailing space: class="inventory_item_name ", which breaks an
        // exact-match xpath like @class='inventory_item_name').
        String slug = productName.toLowerCase().replace(" ", "-");
        click(By.cssSelector("[data-test='add-to-cart-" + slug + "']"));
    }

    public int getCartCount() {
        if (!isVisible(CART_BADGE)) {
            return 0;
        }
        return Integer.parseInt(textOf(CART_BADGE));
    }

    public CartPage goToCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    public List<String> getProductNamesInOrder() {
        return waitAllVisible(PRODUCT_NAMES).stream().map(el -> el.getText()).toList();
    }
}
