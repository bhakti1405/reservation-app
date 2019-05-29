package com.wm.model;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SeatHold {

	private static final AtomicInteger seatHoldGen = new AtomicInteger(0);

	public static int getNext() {
		return seatHoldGen.incrementAndGet();
	}

	private String customerEmail;
	private Long expiryTime;
	private int seatHoldId;
	private Set<Integer> seats = new LinkedHashSet<>();

	public SeatHold(String customerEmail, Long expiry) {
		this.seatHoldId = (getNext());
		this.customerEmail = customerEmail;
		this.expiryTime = expiry;
	}

	public void addSeat(int i) {
		seats.add(i);

	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public Long getExpiryTime() {
		return expiryTime;
	}

	public int getSeatHoldId() {
		return seatHoldId;
	}

	public Set<Integer> getSeats() {
		return seats;
	}

	public void setExpiryTime(Long expiryTime) {
		this.expiryTime = expiryTime;
	}

	@Override
	public String toString() {
		return String.format("SeatHold [seatHoldId=%s, customerEmail=%s, expiryTime=%s, seats=%s]", seatHoldId,
				customerEmail, expiryTime, seats);
	}

}
