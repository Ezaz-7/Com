package com.app.Hospital.Management.System.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.app.Hospital.Management.System.entities.DoctorSchedule;
import com.app.Hospital.Management.System.entities.ScheduledId;
import com.app.Hospital.Management.System.entities.TimeSlot;
import com.app.Hospital.Management.System.repositories.DoctorScheduleRepository;

import jakarta.transaction.Transactional;
@Service
public class DoctorScheduleService {
	
	@Autowired
	private DoctorScheduleRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public DoctorSchedule saveDoctor(DoctorSchedule doctor) {
		
        return repo.save(doctor);
    }
	
	public List<DoctorSchedule> getAllDoctors(){
		return repo.findAll();
	}
	
	 public Optional<DoctorSchedule> getDoctorSchedule(Long doctorId, LocalDate date) {
	        return repo.findById(new ScheduledId(doctorId, date));
	  }
	 
	 public String createAvailability(Long doctorId) {
			
			List<DoctorSchedule> previousAvailabilities=repo.findByDoctorId(doctorId);
			repo.deleteAll(previousAvailabilities);
			 
			LocalDate startDate=LocalDate.now();
			LocalDate endDate=startDate.plusDays(6);
			
			List<TimeSlot> timeSlots=new ArrayList<>();
			timeSlots.add(new TimeSlot(LocalTime.of(9, 0),false));
			timeSlots.add(new TimeSlot(LocalTime.of(10, 0),false));
			timeSlots.add(new TimeSlot(LocalTime.of(11, 0),false));
			timeSlots.add(new TimeSlot(LocalTime.of(13, 0),false));
			timeSlots.add(new TimeSlot(LocalTime.of(14, 0),false));
			timeSlots.add(new TimeSlot(LocalTime.of(15, 0),false));
			
			for(LocalDate date=startDate;!date.isAfter(endDate);date=date.plusDays(1)) {
				
				DoctorSchedule sc=new DoctorSchedule(
						doctorId,date,new ArrayList<>(timeSlots));
				
				repo.save(sc);
			}
			return "Availability Created Successfully";
		}
	 
	 
}
