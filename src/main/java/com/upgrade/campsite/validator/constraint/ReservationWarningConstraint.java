package com.upgrade.campsite.validator.constraint;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.upgrade.campsite.model.Reservation;
import com.upgrade.campsite.validator.ReservationWarningValidator;

public class ReservationWarningConstraint implements ConstraintValidator<ReservationWarningValidator, Reservation> {

	private ReservationWarningValidator dayLength;

	public void initialize(ReservationWarningValidator dayLength) {
		this.dayLength = dayLength;
	}

	@Override
	public boolean isValid(Reservation reservation, ConstraintValidatorContext context) {
		if (reservation.getEndDate() == null 
				|| reservation.getStartDate() == null) {
			return true;
		}

		LocalDate date = LocalDate.now();
		long days = ChronoUnit.DAYS.between(date, reservation.getStartDate());
		return days > dayLength.min() || days <= dayLength.max();
	}
}
