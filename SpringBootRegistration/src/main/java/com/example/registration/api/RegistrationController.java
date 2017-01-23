package com.example.registration.api;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.registration.service.RegistrationService;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInformation> createUser(@RequestBody UserInformation userInformation) {
		UserInformation userInfo = registrationService.create(userInformation);
		return new ResponseEntity<UserInformation>(userInfo, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInformation> updateUser(@RequestBody UserInformation userInformation) {
		UserInformation userInfo = registrationService.update(userInformation);
		if (userInfo == null)
			return new ResponseEntity<UserInformation>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<UserInformation>(userInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/{memberId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInformation> deleteUser(@PathVariable("memberId") Integer memberId,
			@RequestBody UserInformation userInformation) {
		registrationService.delete(memberId);
		return new ResponseEntity<UserInformation>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{memberId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInformation> getUserInformation(@PathVariable("memberId") String memberId) {

		UserInformation userInfo = registrationService.findOne(Integer.parseInt(memberId));
		if (userInfo == null) {
			return new ResponseEntity<UserInformation>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UserInformation>(userInfo, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<UserInformation>> findUsers(
			@RequestParam(value = "lastName", required = false) String lastName) {

		Collection<UserInformation> userInfoList = registrationService.find(lastName);
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
