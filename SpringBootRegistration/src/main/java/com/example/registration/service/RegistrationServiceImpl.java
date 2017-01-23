package com.example.registration.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.registration.model.UserInformation;
import com.example.registration.repository.UserRepository;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	UserRepository userRepository;

	public Collection<UserInformation> find(String lastname) {
		Collection<UserInformation> userInfoList = null;
		if (lastname == null)
			userInfoList = userRepository.findAll();
		else
			userInfoList = userRepository.findByLastName(lastname);
		return userInfoList;
	}

	public UserInformation findOne(Integer memberId) {
		if (memberId == null)
			return null;
		return userRepository.findOne(memberId);
	}

	public UserInformation create(UserInformation userInformation) {
		if (userInformation.getMemberId() != null)
			return null;
		return userRepository.save(userInformation);
	}

	public UserInformation update(UserInformation userInformation) {
		if (userInformation.getMemberId() == null)
			return null;
		UserInformation userInfoUpdate = findOne(userInformation.getMemberId());
		if (userInfoUpdate == null)
			return null;
		userInfoUpdate.setEmailId(userInformation.getEmailId());
		userInfoUpdate.setFirstName(userInformation.getFirstName());
		userInfoUpdate.setLastName(userInformation.getLastName());

		return userRepository.save(userInfoUpdate);
	}

	public void delete(Integer memberId) {
		userRepository.delete(memberId);
	}

}
