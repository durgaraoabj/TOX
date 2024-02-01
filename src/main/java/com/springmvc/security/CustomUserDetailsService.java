package com.springmvc.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.covide.enums.StudyStatus;
import com.springmvc.dao.StudyDao;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StatusMaster;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	public static boolean dbcheck = true;
	static final Logger logger = Logger.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserService userService;
	@Autowired
	StudyService studyService;
	@Override
//	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		LoginUsers user = userService.findByActiveUsername(username);
		logger.info("User : {}"+ user);
		if (user == null) {
			logger.info("User not found");
			throw new UsernameNotFoundException("Username not found");
			
		}
		boolean accountExpried = false;
		if(user.getAccountexprie().after(new Date()))
			accountExpried = true;
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isAccountNotDisable(), accountExpried,
				true, user.isAccountNotLock(), getGrantedAuthorities(user));
	}

	private List<GrantedAuthority> getGrantedAuthorities(LoginUsers user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRole()));
		logger.info("authorities : {}"+ authorities);
		return authorities;
	}

}
