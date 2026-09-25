package com.ecommerce.qa.tests;

import com.ecommerce.qa.utils.CheckoutBoundaryValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 Unit Test Suite for fast validation of E-Commerce Cart & Checkout boundary logic.
 * Complements the Selenium + TestNG UI regression suite.
 */
public class CheckoutBoundaryValidatorJUnitTest {

    @Test
    @DisplayName("TC_ECOM_015 (JUnit): Verify Cart Quantity Boundary [Min=1, Max=10, Invalid=0,11]")
    public void testQuantityBoundaryPartitions() {
        Assertions.assertFalse(CheckoutBoundaryValidator.isValidQuantity(0), "0 is below minimum boundary (1)");
        Assertions.assertTrue(CheckoutBoundaryValidator.isValidQuantity(1), "1 is valid minimum boundary");
        Assertions.assertTrue(CheckoutBoundaryValidator.isValidQuantity(10), "10 is valid maximum boundary");
        Assertions.assertFalse(CheckoutBoundaryValidator.isValidQuantity(11), "11 exceeds maximum boundary (10)");
    }

    @Test
    @DisplayName("TC_ECOM_016 (JUnit): Verify Postal Code Digit Length Boundary [4, 5, 6, 7 digits]")
    public void testPostalCodeBoundaryPartitions() {
        Assertions.assertFalse(CheckoutBoundaryValidator.isValidPostalCode("5600"), "4-digit PIN should fail");
        Assertions.assertTrue(CheckoutBoundaryValidator.isValidPostalCode("56001"), "5-digit ZIP should pass");
        Assertions.assertTrue(CheckoutBoundaryValidator.isValidPostalCode("560001"), "6-digit PIN should pass");
        Assertions.assertFalse(CheckoutBoundaryValidator.isValidPostalCode("5600019"), "7-digit PIN should fail");
        Assertions.assertFalse(CheckoutBoundaryValidator.isValidPostalCode("56AB01"), "Alphanumeric PIN should fail");
    }

    @Test
    @DisplayName("TC_ECOM_017 (JUnit): Verify SAVE10 Discount Calculation & Negative Price Protection")
    public void testPromoCalculationAndNegativeGuard() {
        Assertions.assertEquals(180.00,
                CheckoutBoundaryValidator.calculateFinalTotal(2, 100.00, "SAVE10"), 0.001);
        Assertions.assertEquals(200.00,
                CheckoutBoundaryValidator.calculateFinalTotal(2, 100.00, "INVALID"), 0.001);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> CheckoutBoundaryValidator.calculateFinalTotal(0, 100.00, "SAVE10"));
    }
}
