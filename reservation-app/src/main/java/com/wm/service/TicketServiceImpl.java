package com.wm.service;

import java.util.ListIterator;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.wm.dao.CleanExpiredHoldsThread;
import com.wm.dao.TicketDatabaseService;
import com.wm.model.SeatHold;

@Service
public class TicketServiceImpl implements TicketService {

	Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

	@Autowired
	TicketDatabaseService db;

	@Value("${max.seats.in.theatre}")
	private int MAX_SEATS;

	@Value("${reservation.hold.duration.in.microseconds}")
	private int HOLD_PERIOD;

	@PostConstruct
    private void postConstruct() {
		logger.info("Init TicketServiceImpl with max seats:" + MAX_SEATS);
		new CleanExpiredHoldsThread(db).start();
    }
	
	

	@Override
	public int numSeatsAvailable() {

		logger.info("Called numSeatsAvailable");
		return MAX_SEATS - db.reservedSeatCount();
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		logger.info("Called findAndHoldSeats");

		if (numSeatsAvailable() < numSeats) {
			logger.info("Shortage of these many seats: " + (numSeats - numSeatsAvailable()));
			return null;
		}

		SeatHold sh = new SeatHold(customerEmail, (System.currentTimeMillis() + HOLD_PERIOD));

		for (int i = 1; i <= MAX_SEATS && numSeats > 0; i++) {
			if (db.getTheatreSeats().putIfAbsent(i, sh) == null) {
				sh.addSeat(i);
				numSeats--;
			}

		}

		if (numSeats > 0)
			sh.setExpiryTime(0L);
		else
			db.getHolds().add(sh);

		logger.info("Holding seats for: " + sh);

		return sh;

	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		logger.info("Called reserveSeats");

		ListIterator<SeatHold> iter = db.getHolds().listIterator();
		while (iter.hasNext()) {
			SeatHold sh = iter.next();
			if (sh.getSeatHoldId() == seatHoldId && Objects.equals(customerEmail, sh.getCustomerEmail())) {
				logger.info("Confirming seats : " + sh);
				iter.remove();
				return generateConfCode(seatHoldId, customerEmail);
			}
		}
		logger.info("No Match found to reserveSeats :" + seatHoldId);

		return null;

	}

	private static String generateConfCode(int seatHoldId, String customerEmail) {
		return seatHoldId + "-" + customerEmail;
	}

}