package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.repository.UserRespository;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailService userDetailService;
	
	private UserRespository userRespository;	
	
	public SecurityConfiguration(UserDetailService userDetailService, UserRespository userRespository) {
		this.userDetailService = userDetailService;
		this.userRespository = userRespository;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
		System.out.println("Configure 1 Ended");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		http
		.csrf().disable()//csrf is for form based authentication. Since we are dealing with token based authentication we are disabling crsf()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//session management is used in form based authentication. Since we are dealing with token based authentication we are do not need session management 
		.and()
		//add jwt filters
		.addFilter(new JWTAuthenticationFilter(authenticationManager()))
		.addFilter(new JWTAuthorizationFilter(authenticationManager(), userRespository))
		.authorizeRequests()
		//configure access rules
		.antMatchers(HttpMethod.POST, "/login").permitAll()
		.antMatchers(HttpMethod.POST,"/admin").hasRole("ADMIN")
		.antMatchers(HttpMethod.GET,"/admin").hasAnyRole("ADMIN")
		.antMatchers(HttpMethod.GET,"/user").hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.POST,"/bookEvent").hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.GET,"/getAllEvents").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE,"/deleteEvent/*").hasRole("ADMIN");
		
		System.out.println("Configure 2 Ended");
	}
		
	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userDetailService);
		System.out.println("Dao Auth Ended");
		return daoAuthenticationProvider;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); 
	}
}