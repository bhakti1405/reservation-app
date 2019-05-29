package com.wm.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.wm.model.SeatHold;

/*
 * TODO This can be replaced with a persistance store impl
 */
@Repository
public class TicketDatabaseService {

	private Map<Integer, SeatHold> theatreSeats;
	private List<SeatHold> holds;

	TicketDatabaseService() {
		reset();
	}

	public Map<Integer, SeatHold> getTheatreSeats() {
		return theatreSeats;
	}

	public List<SeatHold> getHolds() {
		return holds;
	}

	public synchronized void reset() {
		theatreSeats = new ConcurrentHashMap<>();
		holds = new LinkedList<>();

	}

	public int reservedSeatCount() {
		return theatreSeats.size();
	}

}
