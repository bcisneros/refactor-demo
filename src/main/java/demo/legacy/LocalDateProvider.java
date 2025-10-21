package demo.legacy;

import java.time.LocalDate;

@FunctionalInterface
public interface LocalDateProvider {
    LocalDate today();
}
