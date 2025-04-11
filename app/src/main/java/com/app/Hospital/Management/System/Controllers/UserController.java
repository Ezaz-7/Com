package com.app.Hospital.Management.System.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Hospital.Management.System.entities.User;
import com.app.Hospital.Management.System.repositories.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
@Validated
@RestController
@RequestMapping("/api/register")
public class UserController {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepo;
	@Transactional
	@PostMapping("/")
	public ResponseEntity<?>  register(@Valid @RequestBody User user)
	{ 
		if (userRepo.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Duplicate entry for the field email", HttpStatus.BAD_REQUEST);
        }
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepo.save(user);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
		
	}
	
}
