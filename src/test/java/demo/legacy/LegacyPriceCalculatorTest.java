
package demo.legacy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;

class LegacyPriceCalculatorTest {

    private LegacyPriceCalculator calculator = new LegacyPriceCalculator();

    @Test
    @Disabled
    void invalid_price_zero() {
        double basePrice = 0.0d;
        int quantity = 0;
        String countryCode = null;
        String membership = null;
        boolean expedited = false;

        var error = assertThrows(IllegalArgumentException.class,
                () -> calculate(basePrice, quantity, countryCode, membership, expedited));

        assertEquals("Invalid base price", error.getMessage());
    }

    @Test
    @Disabled
    void invalid_price_negative() {
        double basePrice = -1.0d;
        int quantity = 0;
        String countryCode = null;
        String membership = null;
        boolean expedited = false;

        var error = assertThrows(IllegalArgumentException.class,
                () -> calculate(basePrice, quantity, countryCode, membership, expedited));

        assertEquals("Invalid base price", error.getMessage());
    }

    @Test
    @Disabled
    void no_items() {
        double basePrice = 4567.0d;
        int quantity = 0;
        String countryCode = null;
        String membership = null;
        boolean expedited = false;

        double expected = 0.0d;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void no_discounts_no_membership() {
        double basePrice = 234.0d;
        int quantity = 1;
        String countryCode = null;
        String membership = null;
        boolean expedited = false;

        double expected = basePrice * quantity + 50.0;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void discount_ten_items_default_tax() {
        double basePrice = 658.0d;
        int quantity = 10;
        String countryCode = null;
        String membership = null;
        boolean expedited = false;

        double baseTotal = basePrice * quantity;
        double discounts = baseTotal * 0.05;
        double expected = (baseTotal - discounts) * 1.30;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void discount_eleven_items_default_tax() {
        double basePrice = 823.56d;
        int quantity = 11;
        String countryCode = null;
        String membership = null;
        boolean expedited = false;

        double baseTotal = basePrice * quantity;
        double discounts = baseTotal * 0.05;
        double expected = (baseTotal - discounts) * 1.30;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void discount_fifty_items_default_tax() {
        double basePrice = 10.12d;
        int quantity = 50;
        String countryCode = null;
        String membership = null;
        boolean expedited = false;

        double baseTotal = basePrice * quantity;
        double discounts = baseTotal * 0.05;
        double expected = (baseTotal - discounts) * 1.30;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void discount_greater_fifty_with_double_discount_default_tax() {
        double basePrice = 763.99d;
        int quantity = 51;
        String countryCode = null;
        String membership = null;
        boolean expedited = false;

        double baseTotal = basePrice * quantity;
        double discounts = baseTotal * 0.05;
        double subtotal = baseTotal - discounts;
        double extraDiscount = subtotal * 0.10;
        double expected = (subtotal - extraDiscount) * 1.30;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void discount_hundred_items_with_double_discount_default_tax() {
        double basePrice = 123.098d;
        int quantity = 100;
        String countryCode = null;
        String membership = null;
        boolean expedited = false;

        double baseTotal = basePrice * quantity;
        double discounts = baseTotal * 0.05;
        double subtotal = baseTotal - discounts;
        double extraDiscount = subtotal * 0.10;
        double expected = (subtotal - extraDiscount) * 1.30;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void too_many_products() {
        double basePrice = 783d;
        int quantity = 101;
        String countryCode = null;
        String membership = null;
        boolean expedited = false;

        var error = assertThrows(IllegalArgumentException.class,
                () -> calculate(basePrice, quantity, countryCode, membership, expedited));

        assertEquals("Too many products!", error.getMessage());
    }

    @Test
    @Disabled
    void ten_items_mx_tax() {
        double basePrice = 763.99d;
        int quantity = 10;
        String countryCode = "MX";
        String membership = null;
        boolean expedited = false;

        // double expected = 8419.16d;
        double baseTotal = basePrice * quantity;
        double discounts = baseTotal * 0.05;
        double subtotal = baseTotal - discounts;
        double extraDiscount = 0;
        double expected = (subtotal - extraDiscount) * 1.16;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void fifteen_items_us_tax() {
        double basePrice = 200.876d;
        int quantity = 15;
        String countryCode = "US";
        String membership = null;
        boolean expedited = false;

        double baseTotal = basePrice * quantity;
        double discounts = baseTotal * 0.05;
        double subtotal = baseTotal - discounts;
        double extraDiscount = 0;
        double expected = (subtotal - extraDiscount) * 1.07;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void thirty_items_other_country_tax() {
        double basePrice = 345.21d;
        int quantity = 30;
        String countryCode = "BR";
        String membership = null;
        boolean expedited = false;

        double baseTotal = basePrice * quantity;
        double discounts = baseTotal * 0.05;
        double subtotal = baseTotal - discounts;
        double extraDiscount = 0;
        double expected = (subtotal - extraDiscount) * 1.20;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void membership_gold() {
        double basePrice = 12.0d;
        int quantity = 9;
        String countryCode = null;
        String membership = "GOLD";
        boolean expedited = false;

        double baseTotal = basePrice * quantity;
        double discounts = 20;
        double subtotal = baseTotal - discounts;
        double extraDiscount = 0;
        double expected = subtotal - extraDiscount;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void membership_silver() {
        double basePrice = 15.0d;
        int quantity = 7;
        String countryCode = null;
        String membership = "SILVER";
        boolean expedited = false;

        double baseTotal = basePrice * quantity;
        double discounts = 10;
        double subtotal = baseTotal - discounts;
        double extraDiscount = 0;
        double expected = subtotal - extraDiscount;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void membership_other_no_rush() {
        double basePrice = 20.0d;
        int quantity = 3;
        String countryCode = null;
        String membership = "OTHER";
        boolean expedited = false;

        double baseTotal = basePrice * quantity;
        double discounts = 0;
        double subtotal = baseTotal - discounts;
        double extraDiscount = 0;
        double charges = 10;
        double expected = (subtotal - extraDiscount) + charges;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void membership_other_rush() {
        double basePrice = 30.0d;
        int quantity = 5;
        String countryCode = null;
        String membership = "OTHER";
        boolean expedited = true;

        double baseTotal = basePrice * quantity;
        double discounts = 0;
        double subtotal = baseTotal - discounts;
        double extraDiscount = 0;
        double charges = 30;
        double expected = (subtotal - extraDiscount) + charges;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void membership_total_zero() {
        double basePrice = 19.0d;
        int quantity = 1;
        String countryCode = null;
        String membership = "GOLD";
        boolean expedited = true;

        double expected = 0.0d;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    private void validate(double expected, double result) {
        assertEquals(expected, result, 2);
    }

    private double calculate(double basePrice, int quantity, String countryCode, String membership, boolean expedited) {
        return calculator.calculate(basePrice, quantity, countryCode, membership, expedited);
    }

}
