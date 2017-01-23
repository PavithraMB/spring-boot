package com.example.registration.service;

import java.util.Collection;

import com.example.registration.model.UserInformation;

public interface RegistrationService {

    Collection<UserInformation> find(String lastname);

    UserInformation findOne(Integer memberId);

    UserInformation create(UserInformation userInformation);

    UserInformation update(UserInformation userInformation);

    void delete(Integer memberId);

}
