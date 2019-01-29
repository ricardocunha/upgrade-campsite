package com.upgrade.campsite.util;

public enum ReservationEnum {
	MAX_PERIOD_AHEAD(31), MIN(1), MAX(3);

	private final int value;

	ReservationEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
