package com.ecommerce.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object Model (POM) for Product Catalog & Shopping Cart (Epic ECOM-EP-02).
 * Handles item quantity Boundary Value Analysis (Min = 1, Max = 10) and Cart Subtotal checks.
 */
public class CartPage extends BasePage {

    private final By quantityInput = By.id("item-qty");
    private final By updateCartButton = By.id("update-cart-btn");
    private final By proceedToCheckoutButton = By.id("proceed-checkout-btn");
    private final By cartBadge = By.id("cart-badge");
    private final By cartSubtotal = By.id("cart-subtotal");
    private final By cartErrorBanner = By.id("cart-error");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void updateQuantityAndAdd(String quantity) {
        type(quantityInput, quantity);
        click(updateCartButton);
    }

    public void proceedToCheckout() {
        click(proceedToCheckoutButton);
    }

    public String getCartBadgeCount() {
        return getText(cartBadge);
    }

    public String getCartSubtotal() {
        return getText(cartSubtotal);
    }

    public String getCartErrorMessage() {
        return getText(cartErrorBanner);
    }
}
