package com.example.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Booking;
import com.example.model.User;
import com.example.repository.BookingRepository;
import com.example.repository.UserRespository;

@RestController

public class UserController {

	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Autowired
	private UserRespository userRespository;
	

	@Autowired
	private BookingRepository bookingRepository;
	
	@PostMapping("/admin")
	public User createUser(@Valid @RequestBody User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return userRespository.save(user);
	}
	
	@GetMapping("/admin")
	public List<User> getAllUsers(){
		return userRespository.findAll();
	}
	
	@GetMapping("/user")
	public List<User> get_AllUsers(){
		return userRespository.findAll();
	}
	

	@PostMapping("/bookEvent")
	public String bookNewEvent(@Valid @RequestBody Booking booking) {
		bookingRepository.save(booking);
		return "Event Added Successfully";
	}
	
	@GetMapping("/getAllEvents")
	public List<Booking> getAllEvents(){
		return bookingRepository.findAll();
	}
	
	@DeleteMapping("/deleteEvent/{id}")
	public String deleteEventById(@PathVariable Long id) {
		bookingRepository.deleteById(id);
		return "Event Deleted Successfully";
	}
}