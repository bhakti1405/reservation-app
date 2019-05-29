package com.wm.controller;

import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wm.model.SeatHold;
import com.wm.service.TicketServiceImpl;

@Validated
@RestController
public class TicketRestController {

	@Autowired
	private TicketServiceImpl studentService;

	@GetMapping("/hello")
	public String hello() {
		return "Hello!";
	}

	// Example: send GET to http://localhost:8080/numSeatsAvailable
	@GetMapping("/numSeatsAvailable")
	public int retrieveCoursesForStudent() {
		return studentService.numSeatsAvailable();
	}

	// Example: send POST to
	// http://localhost:8080/hold?numSeats=10&customerEmail=mal@sdf.com
	@PostMapping("/hold")
	public SeatHold findAndHoldSeats(@RequestParam int numSeats, @Email @RequestParam String customerEmail) {
		return studentService.findAndHoldSeats(numSeats, customerEmail);
	}

	// Example: send POST to
	// http://localhost:8080/reserve?seatHoldId=21&customerEmail=mal@asd.com

	@PostMapping("/reserve")
	public String holdSeats(@RequestParam int seatHoldId, @Email @RequestParam String customerEmail) {
		return studentService.reserveSeats(seatHoldId, customerEmail);
	}

}
