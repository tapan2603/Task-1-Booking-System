package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.model.Booking;

public interface BookingRepository extends MongoRepository<Booking, Long> {

}
