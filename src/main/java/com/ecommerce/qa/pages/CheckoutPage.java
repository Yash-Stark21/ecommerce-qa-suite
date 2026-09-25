package com.ecommerce.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object Model (POM) for Checkout, Promo Code & Order Confirmation (Epic ECOM-EP-03).
 * Handles Postal Code Boundary Value Analysis (5 to 6 digits) and Negative Field Validations.
 */
public class CheckoutPage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By promoCodeInput = By.id("promo-code");
    private final By applyPromoButton = By.id("apply-promo-btn");
    private final By placeOrderButton = By.id("place-order-btn");

    private final By checkoutErrorBanner = By.id("checkout-error");
    private final By promoSuccessBanner = By.id("promo-msg");
    private final By finalTotalText = By.id("final-total");
    private final By confirmationBanner = By.id("confirmation-banner");
    private final By orderIdText = By.id("order-id");
    private final By confirmedAmountText = By.id("confirmed-amount");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void enterShippingDetails(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
    }

    public void applyPromoCode(String promoCode) {
        type(promoCodeInput, promoCode);
        click(applyPromoButton);
    }

    public void submitOrder() {
        click(placeOrderButton);
    }

    public String getCheckoutErrorMessage() {
        return getText(checkoutErrorBanner);
    }

    public String getPromoSuccessMessage() {
        return getText(promoSuccessBanner);
    }

    public String getFinalPayableTotal() {
        return getText(finalTotalText);
    }

    public String getOrderConfirmationMessage() {
        return getText(confirmationBanner);
    }

    public String getConfirmedOrderId() {
        return getText(orderIdText);
    }

    public String getConfirmedAmount() {
        return getText(confirmedAmountText);
    }
}
