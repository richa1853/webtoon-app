package com.example.demo.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.business.UserBusiness;
import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;

@Service
public class UserBusinessImpl implements UserBusiness{
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
        return "user added to system ";
    }
}
