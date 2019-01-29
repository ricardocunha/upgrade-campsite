package com.upgrade.campsite.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.upgrade.campsite.validator.constraint.ReservationWarningConstraint;

@Constraint(validatedBy = ReservationWarningConstraint.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReservationWarningValidator {
    
	public static final String MSG = "The campsite period of reservation needs to start in the next day and finishes until 31 days in advance";

	String message() default MSG;

	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default { };

    int max() default 31;
    
    int min() default 1;
}
