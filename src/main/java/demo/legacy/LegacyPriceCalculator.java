package demo.legacy;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class LegacyPriceCalculator {

    private final LocalDateProvider localDateProvider;

    public LegacyPriceCalculator() {
        this.localDateProvider = () -> LocalDate.now();
    }

    public LegacyPriceCalculator(LocalDateProvider localDateProvider) {
        this.localDateProvider = localDateProvider;
    }

    // Calculates final price (legacy style, do not touch lightly!)
    public double calculate(
            double basePrice,
            int quantity,
            String countryCode,
            String membership,
            boolean expedited) {
        // check basePrice is valid
        if (basePrice > 0) {
            if (quantity > 0) {
                double total = basePrice * quantity;
                // quantity discounts
                if (quantity >= 10) {
                    total = total - (total * 0.05); // 5% discount
                    if (quantity > 50 && quantity <= 100) {
                        total = total - (total * 0.10); // extra 10%!!
                    } else if (quantity > 100) {
                        throw new IllegalArgumentException("Too many products!");
                    }
                    if (countryCode != null) {
                        if ("MX".equals(countryCode)) {
                            total = total * 1.16; // IVA MX
                        } else if ("US".equals(countryCode)) {
                            total = total * 1.07; // Sales tax
                        } else {
                            total = total * 1.20; // default worldwide?
                        }
                    } else {
                        total = total * 1.30;
                    }
                } else {
                    // membership discounts
                    if (membership != null) {
                        if (membership.equals("GOLD")) {
                            total = total - 20; // gold special
                            // you receive other adjustment on friday's as gold
                            if (localDateProvider.today().getDayOfWeek() == DayOfWeek.FRIDAY) {
                                total = total * 0.95;
                            }
                        } else if (membership.equals("SILVER")) {
                            total = total - 10; // silver
                        } else {
                            // unknown membership
                            // shipping
                            if (expedited) {
                                total = total + 30; // rush fee
                            } else {
                                total = total + 10; // normal
                            }
                        }
                    } else {
                        // no membership
                        total = total + 50;

                    }
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
            throw new IllegalArgumentException("Invalid base price");
        }
    }

}
