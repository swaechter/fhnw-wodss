package ch.fhnw.wodss.webapplication.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "The dates must be in the range YYYY/MM/DD [1900/01/01, 2999/12/31] and the end date must be after the start date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
