
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
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
    @Disabled
    void mx_country_code_tax() {
        var basePrice = 30;
        var quantity = 20;
        var countryCode = "MX";
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        var expected = 661.19;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void us_country_code_tax() {
        var basePrice = 30.0;
        var quantity = 20;
        var countryCode = "US";
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        var expected = 609.90d;


        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void other_country_code_tax() {
        var basePrice = 30;
        var quantity = 20;
        var countryCode = "NO_MX_OR_US_CODE";
        var membership = IRRELEVANT_MEMBERSHIP;
        var expedited = IRRELEVANT_EXPEDITE;

        var expected = 684.0d;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void gold_membership() {
        var basePrice = 100.0;
        var quantity = 1;
        var membership = "GOLD";
        var countryCode = IRRELEVANT_COUNTRY_CODE;
        var expedited = IRRELEVANT_EXPEDITE;

        var expected = 80.0d;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void silver_membership() {
        var basePrice = 20.0d;
        int quantity = 5;
        String membership = "SILVER";
        String countryCode = IRRELEVANT_COUNTRY_CODE;
        boolean expedited = IRRELEVANT_EXPEDITE;

        var expected = 90.0d;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void other_membership_no_rush() {
        var basePrice = 25.0d;
        var quantity = 4;
        var membership = "OTHER";
        var expedited = false;
        var countryCode = IRRELEVANT_COUNTRY_CODE;

        var expected = 110.0d;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void other_membership_rushing() {
        var basePrice = 12.5d;
        var quantity = 8;
        var membership = "OTHER";
        var expedited = true;
        var countryCode = IRRELEVANT_COUNTRY_CODE;

        var expected = 130.0d;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

    @Test
    @Disabled
    void gold_membership_free_price() {
        var basePrice = 19.9d;
        var quantity = 1;
        var membership = "GOLD";
        var expedited = IRRELEVANT_EXPEDITE;
        var countryCode = IRRELEVANT_COUNTRY_CODE;

        double expected = 0.0d;

        var result = calculate(basePrice, quantity, countryCode, membership, expedited);

        validate(expected, result);
    }

     @Test
    @Disabled
    void silver_membership_free_price() {
        var basePrice = 9.9d;
        var quantity = 1;
        var membership = "SILVER";
        var expedited = IRRELEVANT_EXPEDITE;
        var countryCode = IRRELEVANT_COUNTRY_CODE;

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
