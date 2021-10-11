package com.example.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.example.model.JWTModel;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	//Trigger when we request POST method
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

			//Get Credential Form JWTModel
			JWTModel jwtModel = null;
			try {
				jwtModel = new ObjectMapper().readValue(request.getInputStream(), JWTModel.class);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("---------------------------------------------");
				System.out.println("JWT Authentication Filter Error: "+e.getMessage());
			}
			
			//Creating Token
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(
							jwtModel.getUsername(),
							jwtModel.getPassword(),
							new ArrayList<>());
			
			//Authenticate User
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			return authentication;
			
		
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		//Get Principal
		UserDetail userDetail = (UserDetail) authResult.getPrincipal();
		
		//Create JWT Token
		String token = JWT.create()
				.withSubject(userDetail.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWTProperties.expiration_time))
				.sign(HMAC512(JWTProperties.secrert.getBytes()));
		
		//Add token to response
		
		response.addHeader(JWTProperties.header_string, JWTProperties.token_prefix + token);
	}
	
}
