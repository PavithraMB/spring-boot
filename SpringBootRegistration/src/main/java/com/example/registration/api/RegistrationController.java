package com.example.registration.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.registration.model.UserInformation;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

	private static Integer nextId;
	private static Map<Integer, UserInformation> userInfoMap;

	private static UserInformation save(UserInformation userInfo) {
		if (userInfoMap == null) {
			userInfoMap = new HashMap<Integer, UserInformation>();
			nextId = 1;
		}
		userInfo.setMemberId(nextId);
		nextId = nextId + 1;
		userInfoMap.put(userInfo.getMemberId(), userInfo);
		return userInfo;
	}

	static {
		UserInformation user1 = new UserInformation();
		user1.setFirstName("John");
		user1.setLastName("Doe");
		user1.setEmailId("John.Doe@SpringBootTest.com");
		save(user1);

		UserInformation user2 = new UserInformation();
		user2.setFirstName("Jane");
		user2.setLastName("Doe");
		user2.setEmailId("Jane.Doe@SpringBootTest.com");
		save(user2);
		
		UserInformation user3 = new UserInformation();
		user3.setFirstName("Test");
		user3.setLastName("Test");
		user3.setEmailId("Test.Test@SpringBootTest.com");
		save(user3);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInformation> getUserInformation(
			@RequestParam(value = "memberId", defaultValue = "default") String memberId) {
		UserInformation userInfo = userInfoMap.get(Integer.parseInt(memberId));
		if (userInfo == null)
			return new ResponseEntity<UserInformation>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<UserInformation>(userInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "info", method = RequestMethod.GET)
	public String info() {
		return "Registration Service - V 1.0";
	}
}
