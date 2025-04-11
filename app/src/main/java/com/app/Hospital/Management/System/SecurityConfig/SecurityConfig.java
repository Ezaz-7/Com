package com.app.Hospital.Management.System.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
public class SecurityConfig {
	
	private final SecurityConfigPatient patientDetails;
	public SecurityConfig(SecurityConfigPatient patientDetails) {
		this.patientDetails=patientDetails;
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		.csrf(cust->cust.disable())
		.authorizeHttpRequests(auth->auth
				.requestMatchers("/api/hospital/patients/put/**","/api/hospital/patients/get/**","/api/hospital/patients/del/**","/api/hospital/appointments/**","/api/hospital/appointments/**").hasRole("PATIENT")
				.requestMatchers("/api/hospital/doctors/**","/api/hospital/doctors/create/**","/api/hospital/patients/findall","/api/hospital/history/save").hasRole("DOCTOR")
				.requestMatchers("/api/register/**").permitAll()
				.anyRequest().authenticated()
				).formLogin(withDefaults())
				.httpBasic(withDefaults())
				.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();	
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
