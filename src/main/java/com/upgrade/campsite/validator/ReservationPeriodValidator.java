package com.upgrade.campsite.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.upgrade.campsite.validator.constraint.ReservationPeriodConstraint;

@Constraint(validatedBy = ReservationPeriodConstraint.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReservationPeriodValidator {

	public static final String MSG = "The campsite period of reservation is minimum of 1 and maximum of 3 days";
	
    String message() default MSG;

	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default { };

    int max() default 3;
    
    int min() default 1;


}
