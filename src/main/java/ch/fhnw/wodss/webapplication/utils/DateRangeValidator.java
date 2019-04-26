package ch.fhnw.wodss.webapplication.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRange> {

    private static final LocalDate MIN = LocalDate.of(1900, 1, 1);

    private static final LocalDate MAX = LocalDate.of(2999, 12, 31);

    public void initialize(ValidDateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(DateRange dateRange, ConstraintValidatorContext context) {
        if (dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
            return false;
        }

        return dateRange.getStartDate().isAfter(MIN) && dateRange.getEndDate().isBefore(MAX) && dateRange.getEndDate().isAfter(dateRange.getStartDate());
    }
}
