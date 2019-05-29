package com.wm.dao;

import java.util.Date;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wm.model.SeatHold;

/**
 * 
 * This thread performs the cleaning operation of expired reservation holds
 * 
 *
 */
public class CleanExpiredHoldsThread extends Thread {

	Logger logger = LoggerFactory.getLogger(CleanExpiredHoldsThread.class);
	private TicketDatabaseService db;

	public CleanExpiredHoldsThread(TicketDatabaseService db) {
		this.db = db;
	}

	@Override
	public void run() {
		while (true) {
			logger.info("Cleanup holds");
			cleanMap();
			try {
				Thread.sleep(1000 * 1); // run every second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void cleanMap() {
		long currentTime = new Date().getTime();

		synchronized (db) {
			ListIterator<SeatHold> iter = db.getHolds().listIterator();
			while (iter.hasNext()) {
				SeatHold sh = iter.next();
				if (sh.getExpiryTime() < currentTime) {
					for (int s : sh.getSeats()) {
						db.getTheatreSeats().remove(s);
					}
					logger.info("Removing Hold :" + sh);

					iter.remove();
				}
			}
		}

	}
}