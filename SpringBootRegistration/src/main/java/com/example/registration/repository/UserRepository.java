package com.example.registration.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.registration.model.UserInformation;

@Repository
public interface UserRepository extends JpaRepository<UserInformation, Integer> {
	Collection<UserInformation> findByLastName(String lastName);
}
