package com.example.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.model.User;
import com.example.repository.UserRespository;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	private UserRespository userRespository;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
			UserRespository userRespository) {
		super(authenticationManager);
		this.userRespository=userRespository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// Read the Authorization header, where the JWT token should be
		String header = request.getHeader(JWTProperties.header_string);

		// If header does not contain BEARER or is null delegate to Spring impl and exit
		if(header==null || !header.startsWith(JWTProperties.token_prefix)) {
			chain.doFilter(request, response);
			return;
		}

		// If header is present, try grab user principal from database and perform authorization
		Authentication authentication = getUsernamePasswordAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		//Continue filter execution
		chain.doFilter(request, response);
	}

	private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {

		String token = request.getHeader(JWTProperties.header_string);
		if(token!=null) {
			// parse the token and validate it
			String userName = JWT.require(Algorithm.HMAC512(JWTProperties.secrert.getBytes()))
					.build()
					.verify(token.replace(JWTProperties.token_prefix, ""))
					.getSubject();
		
			// Search in the DB if we find the user by token subject (username)
			// If so, then grab user details and create spring auth token using username, pass, authorities/roles
		
			if(userName != null) {
				User user = userRespository.findByUsername(userName);
				UserDetail userDetail = new UserDetail(user);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						userName, null, userDetail.getAuthorities());
				return auth;
			}
			
			return null;
		}
		return null;
	}
}
