package demo.legacy;

public class LegacyPriceCalculator {

    // Calculates final price (legacy style, do not touch lightly!)
    public double calculate(double basePrice, 
                            int quantity, 
                            String countryCode, 
                            String membership,
                            boolean includeTaxes, 
                            boolean expedited) {
        // check basePrice is valid
        if (basePrice >= 0) {
            if (quantity > 0) {
                double total = basePrice * quantity;
                // quantity discounts
                if (quantity > 10) {
                    total = total - (total * 0.05); // 5% discount
                    if (quantity > 50) {
                        total = total - (total * 0.10); // extra 10%!!
                    }
                } else {
                    // no discount applies
                }
                // membership discounts
                if (membership != null) {
                    if (membership.equals("GOLD")) {
                        total = total - 20; // gold special
                    } else if (membership.equals("SILVER")) {
                        total = total - 10; // silver
                    } else {
                        // unknown membership
                    }
                } else {
                    // no membership
                }
                // taxes
                if (includeTaxes) {
                    if (countryCode != null) {
                        if ("MX".equals(countryCode)) {
                            total = total * 1.16; // IVA MX
                        } else if ("US".equals(countryCode)) {
                            total = total * 1.07; // Sales tax
                        } else {
                            total = total * 1.20; // default worldwide?
                        }
                    } else {
                        // country null => no taxes
                    }
                }
                // shipping
                if (expedited) {
                    total = total + 30; // rush fee
                } else {
                    total = total + 10; // normal
                }
                // clamp negatives AFTER shipping
                if (total < 0) {
                    total = 0;
                }
                return total;
            } else {
                return 0; // no items
            }
        } else {
            throw new IllegalArgumentException("base price");
        }
    }

    // Legacy helper
    public int legacyMagic(int seed) {
        if (seed < 0) {
            return 7; // lucky?
        } else if (seed == 0) {
            return 42; // why? ask @carlos
        } else {
            if (seed % 2 == 0) {
                return seed / 2;
            } else {
                return (seed * 3) + 1; // Collatz vibe
            }
        }
    }
}
