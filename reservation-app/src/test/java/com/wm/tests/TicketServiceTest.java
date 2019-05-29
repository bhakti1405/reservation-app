package com.wm.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wm.app.TicketApplication;
import com.wm.dao.TicketDatabaseService;
import com.wm.model.SeatHold;
import com.wm.service.TicketService;

//@ActiveProfiles("test")
@SpringBootTest(classes = TicketApplication.class)

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketServiceTest {

	@Value("${max.seats.in.theatre}")
	private int MAX_SEATS;

	@Value("${reservation.hold.duration.in.microseconds}")
	private int HOLD_PERIOD;

	@Autowired
	private TicketService ts;

	@Autowired
	private TicketDatabaseService ds;

	@Test
	public void numSeatsAvailable() throws Exception {
		ds.reset();

		int avail = ts.numSeatsAvailable();

		assertEquals("available seats", avail, MAX_SEATS);

//		assertThat("available seats",
//				avail,
//		           lessThan(MAX_SEATS));
	}

	@Test
	public void findAndHoldSeats() throws Exception {
		ds.reset();

		int requestedSeats = 10;
		String email = "a@wm.com";
		SeatHold avail = ts.findAndHoldSeats(requestedSeats, email);

		assertEquals(requestedSeats, avail.getSeats().size());
		assertEquals(email, avail.getCustomerEmail());
		assertThat("timestamp", avail.getExpiryTime(), greaterThan(System.currentTimeMillis()));

		requestedSeats = 200;
		avail = ts.findAndHoldSeats(requestedSeats, email);

		assertNull(avail); // requested amout of seats not available now

	}
	
	
	@Test
	public void findAndHoldSeatsPartialOverLimit() throws Exception {
		ds.reset();

		int requestedSeats = 100;
		String email = "a@wm.com";
		SeatHold avail = ts.findAndHoldSeats(requestedSeats, email);

		assertEquals(requestedSeats, avail.getSeats().size());
		assertEquals(email, avail.getCustomerEmail());
		assertThat("timestamp", avail.getExpiryTime(), greaterThan(System.currentTimeMillis()));

		requestedSeats = 100;
		avail = ts.findAndHoldSeats(requestedSeats, email);

		assertNull(avail);  // requested amout of seats not available now
		
		
		Thread.sleep(HOLD_PERIOD + 1000); // Hold is cleared the next second after Max wait perion
		avail = ts.findAndHoldSeats(requestedSeats, email);

		assertEquals(requestedSeats, avail.getSeats().size());
		assertEquals(email, avail.getCustomerEmail());
		assertThat("timestamp", avail.getExpiryTime(), greaterThan(System.currentTimeMillis()));

	}
	

	@Test
	public void reserveSeats() throws Exception {
		ds.reset();

		int requestedSeats = 10;
		String email = "a@wm.com";
		SeatHold avail = ts.findAndHoldSeats(requestedSeats, email);

		assertEquals(requestedSeats, avail.getSeats().size());
		assertEquals(email, avail.getCustomerEmail());
		assertThat("timestamp", avail.getExpiryTime(), greaterThan(System.currentTimeMillis()));

		String conf = ts.reserveSeats(avail.getSeatHoldId(), avail.getCustomerEmail());
		assertTrue(conf.endsWith(avail.getCustomerEmail()));

	}

	@Test
	public void reserveSeatsFailsAfterExpiry() throws Exception {
		ds.reset();

		int requestedSeats = 10;
		String email = "a@wm.com";
		SeatHold avail = ts.findAndHoldSeats(requestedSeats, email);

		assertEquals(requestedSeats, avail.getSeats().size());
		assertEquals(email, avail.getCustomerEmail());
		assertThat("timestamp", avail.getExpiryTime(), greaterThan(System.currentTimeMillis()));

		Thread.sleep(HOLD_PERIOD + 1000); // Hold is cleared the next second after Max wait perion
		String conf = ts.reserveSeats(avail.getSeatHoldId(), avail.getCustomerEmail());
		assertNull(conf);

	}

	@Test
	public void reserveSeatsFailsWithWrongEmail() throws Exception {
		ds.reset();

		int requestedSeats = 10;
		String email1 = "a1@wm.com";
		String email2 = "a2@wm.com";
		SeatHold avail = ts.findAndHoldSeats(requestedSeats, email1);

		assertEquals(requestedSeats, avail.getSeats().size());
		assertEquals(email1, avail.getCustomerEmail());
		assertThat("timestamp", avail.getExpiryTime(), greaterThan(System.currentTimeMillis()));

		String conf = ts.reserveSeats(avail.getSeatHoldId(), email2);
		assertNull(conf);

	}

}
