package com.upgrade.campsite.exception;

public class ReservationNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5024815397690367385L;

	public ReservationNotFoundException(String s) {
        super(s);
    }
}
