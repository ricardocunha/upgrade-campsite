package com.upgrade.campsite.validator.constraint;

import java.time.temporal.ChronoUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.upgrade.campsite.model.Reservation;
import com.upgrade.campsite.validator.ReservationPeriodValidator;

public class ReservationPeriodConstraint implements ConstraintValidator<ReservationPeriodValidator, Reservation> {
    
	private ReservationPeriodValidator dayLength;
	
	@Override
    public void initialize(ReservationPeriodValidator dayLength) {
        this.dayLength = dayLength;
    }

    @Override
    public boolean isValid(Reservation reservation, ConstraintValidatorContext context) {
		if (reservation.getEndDate() == null 
				|| reservation.getStartDate() == null) {
			return true;
		}
        long days = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        return days >=dayLength.min() && days <= dayLength.max();
    }
}
