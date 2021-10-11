package com.example.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "booking")
public class Booking {
	
	@Id
	@Generated(GenerationTime.ALWAYS)
	private Long id; 
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@NotNull(message = "Phone number must not be null")
	@Digits(fraction = 0, integer = 10, message = "Phone number cannot be greater than 10 digits")
	@DecimalMin(value = "999999999", message = "Must be of 10 digits")
	@DecimalMax(value = "9999999999", message = "Must be of 10 digits")
	@Positive(message = "Phone number cannot be negative or zero")
	private Long mobileNumber;
	
	private String emailId;
	
	@NotBlank
	private String eventType;
	
	@NotBlank
	private String eventDate;
	
	@NotBlank
	private String eventContactPerson;
	
	private String eventStartTime;
	private String eventEndTime;	
}
