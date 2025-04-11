package com.app.Hospital.Management.System.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.Hospital.Management.System.Services.DoctorScheduleService;
import com.app.Hospital.Management.System.entities.DoctorSchedule;
import com.app.Hospital.Management.System.entities.ScheduledId;
import com.app.Hospital.Management.System.entities.TimeSlot;
import com.app.Hospital.Management.System.repositories.DoctorScheduleRepository;

@ExtendWith(MockitoExtension.class)
public class DoctorScheduleServiceTest {

    @Mock
    private DoctorScheduleRepository repo;

    @InjectMocks
    private DoctorScheduleService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveDoctor() {
        DoctorSchedule doctor = new DoctorSchedule(1L, LocalDate.now(), new ArrayList<>());
        when(repo.save(doctor)).thenReturn(doctor);

        DoctorSchedule savedDoctor = service.saveDoctor(doctor);

        assertEquals(doctor, savedDoctor);
        verify(repo, times(1)).save(doctor);
    }

    @Test
    public void testGetAllDoctors() {
        List<DoctorSchedule> doctors = new ArrayList<>();
        doctors.add(new DoctorSchedule(1L, LocalDate.now(), new ArrayList<>()));
        when(repo.findAll()).thenReturn(doctors);

        List<DoctorSchedule> result = service.getAllDoctors();

        assertEquals(doctors, result);
        verify(repo, times(1)).findAll();
    }

    @Test
    public void testGetDoctorSchedule() {
        DoctorSchedule doctor = new DoctorSchedule(1L, LocalDate.now(), new ArrayList<>());
        when(repo.findById(new ScheduledId(1L, LocalDate.now()))).thenReturn(Optional.of(doctor));

        Optional<DoctorSchedule> result = service.getDoctorSchedule(1L, LocalDate.now());

        assertTrue(result.isPresent());
        assertEquals(doctor, result.get());
        verify(repo, times(1)).findById(new ScheduledId(1L, LocalDate.now()));
    }

    @Test
    public void testCreateAvailability() {
        Long doctorId = 1L;
        List<DoctorSchedule> previousAvailabilities = new ArrayList<>();
        when(repo.findByDoctorId(doctorId)).thenReturn(previousAvailabilities);

        String result = service.createAvailability(doctorId);

        assertEquals("Availability Created Successfully", result);
        verify(repo, times(1)).findByDoctorId(doctorId);
        verify(repo, times(1)).deleteAll(previousAvailabilities);
        verify(repo, times(7)).save(any(DoctorSchedule.class));
    }
}