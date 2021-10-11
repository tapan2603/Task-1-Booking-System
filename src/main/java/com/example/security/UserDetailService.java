package com.example.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.repository.UserRespository;

@Service
public class UserDetailService implements UserDetailsService{

	private UserRespository userRespository;
	
	public UserDetailService(UserRespository userRespository) {
		this.userRespository = userRespository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRespository.findByUsername(username);
		UserDetail userDetail = new UserDetail(user);
		return userDetail;
	}

}
