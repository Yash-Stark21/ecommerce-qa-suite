package com.ecommerce.qa.utils;

/**
 * Business rule validator for E-Commerce Cart & Checkout boundaries.
 * Tested directly via JUnit 5 (unit testing) and verified end-to-end via TestNG + Selenium.
 */
public class CheckoutBoundaryValidator {

    public static final int MIN_ALLOWED_QUANTITY = 1;
    public static final int MAX_ALLOWED_QUANTITY = 10;
    public static final int MIN_POSTAL_LENGTH = 5;
    public static final int MAX_POSTAL_LENGTH = 6;

    /**
     * Validates whether order item quantity falls within [1, 10] boundary.
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity >= MIN_ALLOWED_QUANTITY && quantity <= MAX_ALLOWED_QUANTITY;
    }

    /**
     * Validates whether shipping postal/PIN code is strictly numeric and [5, 6] digits long.
     */
    public static boolean isValidPostalCode(String postalCode) {
        if (postalCode == null || !postalCode.matches("^\\d+$")) {
            return false;
        }
        int length = postalCode.length();
        return length >= MIN_POSTAL_LENGTH && length <= MAX_POSTAL_LENGTH;
    }

    /**
     * Calculates final order total after applying optional SAVE10 (10%) promo code.
     */
    public static double calculateFinalTotal(int quantity, double unitPrice, String promoCode) {
        if (!isValidQuantity(quantity) || unitPrice < 0) {
            throw new IllegalArgumentException("Invalid quantity or unit price");
        }
        double subtotal = quantity * unitPrice;
        if ("SAVE10".equalsIgnoreCase(promoCode != null ? promoCode.trim() : "")) {
            return Math.round((subtotal * 0.90) * 100.0) / 100.0;
        }
        return Math.round(subtotal * 100.0) / 100.0;
    }
}
