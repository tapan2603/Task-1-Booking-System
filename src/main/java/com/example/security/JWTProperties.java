package com.example.security;

public class JWTProperties {
	public static final String secrert = "Tapan";
	public static final int expiration_time = 60000; // 1 minute
	public static final String token_prefix = "Bearer ";
	public static final String header_string = "Authorization";
	
}