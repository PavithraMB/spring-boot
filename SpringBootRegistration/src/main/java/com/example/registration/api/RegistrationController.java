package com.example.registration.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
		if (userInfo.getMemberId() != null) {
			UserInformation userInformationModel = userInfoMap.get(userInfo.getMemberId());
			if (userInformationModel == null)
				return null;
			userInfoMap.remove(userInfo.getMemberId());
			userInfoMap.put(userInfo.getMemberId(), userInfo);
			return userInfo;
		}
		userInfo.setMemberId(nextId);
		nextId = nextId + 1;
		userInfoMap.put(userInfo.getMemberId(), userInfo);
		return userInfo;
	}

	private static boolean delete(Integer id) {
		UserInformation userInfo = userInfoMap.remove(id);
		if (userInfo == null)
			return false;
		return true;
	}

	private static Collection<UserInformation> findUsersByLastName(String lastName) {
		Collection<UserInformation> userInfoList = new ArrayList<UserInformation>();
		Collection<UserInformation> users = userInfoMap.values();
		for(UserInformation user: users) {
			if (user.getLastName().toUpperCase().indexOf(lastName.toUpperCase()) == 0 )
			{
				userInfoList.add(user);
			}
		}
		return userInfoList;
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

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInformation> createUser(@RequestBody UserInformation userInformation) {

		UserInformation userInfo = save(userInformation);

		return new ResponseEntity<UserInformation>(userInfo, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInformation> updateUser(@RequestBody UserInformation userInformation) {
		UserInformation userInfo = save(userInformation);
		if (userInfo == null)
			return new ResponseEntity<UserInformation>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<UserInformation>(userInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/{memberId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInformation> deleteUser(@PathVariable("memberId") Integer memberId,
			@RequestBody UserInformation userInformation) {
		boolean deleted = delete(memberId);
		if (!deleted)
			return new ResponseEntity<UserInformation>(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<UserInformation>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{memberId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInformation> getUserInformation(@PathVariable("memberId") String memberId) {

		UserInformation userInfo = userInfoMap.get(Integer.parseInt(memberId));
		if (userInfo == null) {
			return new ResponseEntity<UserInformation>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UserInformation>(userInfo, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<UserInformation>> findUsers(@RequestParam(value="lastName",  required = false) String lastName) {

		Collection<UserInformation> userInfoList = null;
		if (lastName == null)
			userInfoList = userInfoMap.values();
		else
			userInfoList = findUsersByLastName(lastName);
		if (userInfoList == null || userInfoList.isEmpty()) {
			return new ResponseEntity<Collection<UserInformation>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<UserInformation>>(userInfoList, HttpStatus.OK);
	}

	@RequestMapping(value = "info", method = RequestMethod.GET)
	public String info() {
		return "Registration Service - V 1.0";
	}
}
