package com.upgrade.campsite.exception;

public class ReservationOverlapException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6521588785146335770L;

	public ReservationOverlapException(String message) {
        super(message);
    }
}
