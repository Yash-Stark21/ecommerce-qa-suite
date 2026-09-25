package com.ecommerce.qa.tests;

import com.ecommerce.qa.pages.CartPage;
import com.ecommerce.qa.pages.CheckoutPage;
import com.ecommerce.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * End-to-End Automated Regression Suite utilizing Selenium WebDriver, Java, TestNG, and POM.
 * Covers Functional, Negative, and Boundary Value Analysis (BVA) tests for critical checkout workflows.
 */
public class CheckoutWorkflowTest extends BaseTest {

    // =========================================================================
    // 1. FUNCTIONAL TESTS (Happy Path End-to-End Workflows)
    // =========================================================================

    @Test(priority = 1, groups = {"functional", "regression"},
          description = "TC_ECOM_001: Verify end-to-end login, cart addition, SAVE10 promo, and order checkout")
    public void testValidLoginAndEndToEndCheckoutWithPromo() {
        LoginPage loginPage = new LoginPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        // Step 1: Login as standard_user
        loginPage.loginAs("standard_user", "secret_sauce");
        Assert.assertEquals(loginPage.getGreetingText(), "Welcome, standard_user");

        // Step 2: Add 2 units ($200.00 subtotal) to Cart
        cartPage.updateQuantityAndAdd("2");
        Assert.assertEquals(cartPage.getCartBadgeCount(), "2");
        Assert.assertEquals(cartPage.getCartSubtotal(), "200.00");

        // Step 3: Proceed to Checkout & Apply SAVE10 (10% discount -> $180.00)
        cartPage.proceedToCheckout();
        checkoutPage.applyPromoCode("SAVE10");
        Assert.assertTrue(checkoutPage.getPromoSuccessMessage().contains("10% discount"));
        Assert.assertEquals(checkoutPage.getFinalPayableTotal(), "180.00");

        // Step 4: Enter valid shipping details & complete order
        checkoutPage.enterShippingDetails("Yashas", "MC", "560001");
        checkoutPage.submitOrder();

        Assert.assertTrue(checkoutPage.getOrderConfirmationMessage().contains("Thank you for your order!"));
        Assert.assertEquals(checkoutPage.getConfirmedOrderId(), "ORD-2026-8492");
        Assert.assertEquals(checkoutPage.getConfirmedAmount(), "180.00");
    }

    // =========================================================================
    // 2. NEGATIVE TESTS (Invalid Inputs, Locked Users, Missing Mandatory Fields)
    // =========================================================================

    @Test(priority = 2, groups = {"negative", "regression"},
          description = "TC_ECOM_002: Verify locked_out_user is blocked from logging in")
    public void testLockedOutUserLoginDenied() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs("locked_out_user", "secret_sauce");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"));
    }

    @Test(priority = 3, groups = {"negative", "regression"},
          description = "TC_ECOM_003: Verify invalid password displays authentication error")
    public void testInvalidCredentialsLoginDenied() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs("standard_user", "wrong_password_123");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Invalid username or password"));
    }

    @Test(priority = 4, groups = {"negative", "regression"},
          description = "TC_ECOM_004: Verify user cannot proceed to checkout with an empty cart (0 items)")
    public void testEmptyCartCannotProceedToCheckout() {
        LoginPage loginPage = new LoginPage(driver);
        CartPage cartPage = new CartPage(driver);

        loginPage.loginAs("standard_user", "secret_sauce");
        cartPage.proceedToCheckout();
        Assert.assertTrue(cartPage.getCartErrorMessage().contains("add at least 1 item"));
    }

    @Test(priority = 5, groups = {"negative", "regression"},
          description = "TC_ECOM_005: Verify checkout fails when mandatory First Name or Last Name is blank")
    public void testCheckoutMissingRequiredNameFields() {
        LoginPage loginPage = new LoginPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        loginPage.loginAs("standard_user", "secret_sauce");
        cartPage.updateQuantityAndAdd("1");
        cartPage.proceedToCheckout();

        checkoutPage.enterShippingDetails("", "MC", "560001");
        checkoutPage.submitOrder();
        Assert.assertTrue(checkoutPage.getCheckoutErrorMessage().contains("First Name and Last Name are required"));
    }

    @Test(priority = 6, groups = {"negative", "regression"},
          description = "TC_ECOM_006: Verify invalid coupon code is rejected and does not alter payable total")
    public void testInvalidPromoCodeRejected() {
        LoginPage loginPage = new LoginPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        loginPage.loginAs("standard_user", "secret_sauce");
        cartPage.updateQuantityAndAdd("3");
        cartPage.proceedToCheckout();

        checkoutPage.applyPromoCode("EXPIRED99");
        Assert.assertTrue(checkoutPage.getCheckoutErrorMessage().contains("Invalid or expired promo code"));
        Assert.assertEquals(checkoutPage.getFinalPayableTotal(), "300.00");
    }

    // =========================================================================
    // 3. BOUNDARY VALUE ANALYSIS (BVA) TESTS WITH TESTNG @DataProvider
    // =========================================================================

    @DataProvider(name = "quantityBoundaryData")
    public Object[][] quantityBoundaryData() {
        return new Object[][] {
            // { quantityInput, expectedValid, expectedResultSubstring }
            { "0",  false, "Quantity must be at least 1" },                  // Lower Invalid Boundary (Min - 1)
            { "1",  true,  "100.00" },                                       // Lower Valid Boundary (Min)
            { "10", true,  "1000.00" },                                      // Upper Valid Boundary (Max)
            { "11", false, "Maximum allowed quantity per order is 10" }      // Upper Invalid Boundary (Max + 1)
        };
    }

    @Test(priority = 7, dataProvider = "quantityBoundaryData", groups = {"boundary", "regression"},
          description = "TC_ECOM_007 to TC_ECOM_010: Cart Quantity Boundary Value Analysis (0, 1, 10, 11)")
    public void testCartQuantityBoundaryValues(String qty, boolean expectedValid, String expectedOutcome) {
        LoginPage loginPage = new LoginPage(driver);
        CartPage cartPage = new CartPage(driver);

        loginPage.loginAs("standard_user", "secret_sauce");
        cartPage.updateQuantityAndAdd(qty);

        if (expectedValid) {
            Assert.assertEquals(cartPage.getCartBadgeCount(), qty);
            Assert.assertEquals(cartPage.getCartSubtotal(), expectedOutcome);
        } else {
            Assert.assertTrue(cartPage.getCartErrorMessage().contains(expectedOutcome),
                    "Expected boundary error message for qty=" + qty);
        }
    }

    @DataProvider(name = "postalCodeBoundaryData")
    public Object[][] postalCodeBoundaryData() {
        return new Object[][] {
            // { postalCodeInput, expectedValid }
            { "5600",    false }, // 4 digits: Lower Invalid Boundary (Min - 1)
            { "56001",   true  }, // 5 digits: Lower Valid Boundary (Min)
            { "560001",  true  }, // 6 digits: Upper Valid Boundary (Max)
            { "5600019", false }  // 7 digits: Upper Invalid Boundary (Max + 1)
        };
    }

    @Test(priority = 8, dataProvider = "postalCodeBoundaryData", groups = {"boundary", "regression"},
          description = "TC_ECOM_011 to TC_ECOM_014: Checkout Postal Code Length Boundary Value Analysis (4, 5, 6, 7 digits)")
    public void testPostalCodeBoundaryValues(String postalCode, boolean expectedValid) {
        LoginPage loginPage = new LoginPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        loginPage.loginAs("standard_user", "secret_sauce");
        cartPage.updateQuantityAndAdd("1");
        cartPage.proceedToCheckout();

        checkoutPage.enterShippingDetails("Yashas", "MC", postalCode);
        checkoutPage.submitOrder();

        if (expectedValid) {
            Assert.assertTrue(checkoutPage.getOrderConfirmationMessage().contains("Thank you for your order!"));
        } else {
            Assert.assertTrue(checkoutPage.getCheckoutErrorMessage().contains("between 5 and 6 numeric digits"),
                    "Expected postal code boundary validation error for input: " + postalCode);
        }
    }
}
