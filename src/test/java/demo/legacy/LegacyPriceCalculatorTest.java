
package demo.legacy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;

class LegacyPriceCalculatorTest {

    private static final int IRRELEVANT_QUANTITY = Integer.MAX_VALUE;
    private static final String IRRELEVANT_COUNTRY_CODE = "ANY_COUNTRY_CODE";
    private static final String IRRELEVANT_MEMBERSHIP = "ANY_MEMBERSHIP";
    private static final boolean IRRELEVANT_EXPEDITE = false;
    private static final double IRRELEVANT_BASE_PRICE = Double.MAX_VALUE;
    private static final String NO_MEMBERSHIP = null;
    private static final String NO_COUNTRY_CODE = null;
    private LegacyPriceCalculator calculator = new LegacyPriceCalculator();

    @Test
    // @Disabled
    void invalid_price_zero() {
        var basePrice = 0.0d;
        var quantity = IRRELEVANT_QUANTITY;
        var countryCode = IRRELEVANT_COUNTRY_CODE;
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        var error = assertThrows(IllegalArgumentException.class,
                () -> calculate(basePrice, quantity, countryCode, membership, expedited));

        assertEquals("Invalid base price", error.getMessage());
    }

    @Test
    // @Disabled
    void invalid_price_negative() {
        var basePrice = -1.0d;
        var quantity = IRRELEVANT_QUANTITY;
        var countryCode = IRRELEVANT_COUNTRY_CODE;
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        var error = assertThrows(IllegalArgumentException.class,
                () -> calculate(basePrice, quantity, countryCode, membership, expedited));

        assertEquals("Invalid base price", error.getMessage());
    }

    @Test
    // @Disabled
    void no_items() {
        var quantity = 0;
        var basePrice = IRRELEVANT_BASE_PRICE;
        var countryCode = IRRELEVANT_COUNTRY_CODE;
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        var expected = 0.0d;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    // @Disabled
    void no_discounts_no_membership() {
        var basePrice = 100.0;
        var quantity = 1;
        var membership = NO_MEMBERSHIP;
        var countryCode = IRRELEVANT_COUNTRY_CODE;
        var expedited = IRRELEVANT_EXPEDITE;

        // 100 * 1 + 50 (Charge for no membership)
        var expected = 150.0;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    // @Disabled
    void discount_ten_items_default_tax_no_country_code() {
        var basePrice = 10.00d;
        var quantity = 10;
        var countryCode = NO_COUNTRY_CODE;
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        // 10 * 10 = 100 -> partial total
        // 100 * 0.95 = 95 -> after 5% discount
        // 95 * 1.30 = 123.50 -> applying no country code tax
        var expected = 123.50;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    // @Disabled
    void discount_eleven_items_default_tax_no_country_code() {
        var basePrice = 300.0d;
        var quantity = 11;
        var countryCode = NO_COUNTRY_CODE;
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        // 300 * 11 = 3,300 -> partial total
        // 3,300 * 0.95 = 3,135 -> after 5% discount
        // 3,135 * 1.30 = 4,075.50 -> applying no country code tax
        var expected = 4075.50d;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    // @Disabled
    void discount_fifty_items_default_tax_no_country_code() {
        var basePrice = 5d;
        var quantity = 50;
        var countryCode = NO_COUNTRY_CODE;
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        // 5 * 50 = 250-> partial total
        // 250 * 0.95 = 237.50 -> after 5% discount
        // 237.50 * 1.30 = 308.75 -> applying no country code tax
        var expected = 308.75;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    // @Disabled
    void discount_greater_fifty_with_double_discount_default_tax() {
        var basePrice = 100.0d;
        var quantity = 51;
        var countryCode = NO_COUNTRY_CODE;
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        // 100 * 51 = 5100 -> base total
        // 5100 * 0.95 = 4845 -> 5% discount
        // 4845 * 0.90 = 4360.5 -> 10% discount
        // 4360.5 * 1.30 = 5668.65 -> applying no country code tax
        var expected = 5668.65;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    // @Disabled
    void discount_hundred_items_with_double_discount_default_tax() {
        var basePrice = 100d;
        var quantity = 100;
        var countryCode = NO_COUNTRY_CODE;
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        // 100 * 100 = 10,000 -> base total
        // 10,000 * 0.95 = 9,500 -> 5% discount
        // 9,500 * 0.90 = 8,550 -> 10% discount
        // 8,550 * 1.30 = 11,115 -> applying no country code tax
        var expected = 11_115.0;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    // @Disabled
    void too_many_products() {
        var quantity = 101;
        var basePrice = IRRELEVANT_BASE_PRICE;
        var countryCode = IRRELEVANT_COUNTRY_CODE;
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        var error = assertThrows(IllegalArgumentException.class,
                () -> calculate(basePrice, quantity, countryCode, membership, expedited));

        assertEquals("Too many products!", error.getMessage());
    }

    @Test
    // @Disabled
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
    // @Disabled
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
    // @Disabled
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
    // @Disabled
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
    // @Disabled
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
    // @Disabled
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
    // @Disabled
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
    // @Disabled
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
        assertEquals(expected, result, 0.1);
    }

    private double calculate(double basePrice, int quantity, String countryCode, String membership, boolean expedited) {
        return calculator.calculate(basePrice, quantity, countryCode, membership, expedited);
    }

}
