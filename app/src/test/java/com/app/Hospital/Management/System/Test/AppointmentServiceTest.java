package com.app.Hospital.Management.System.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.app.Hospital.Management.System.Services.AppointmentService;
import com.app.Hospital.Management.System.Services.NotificationService;
import com.app.Hospital.Management.System.entities.Appointment;
import com.app.Hospital.Management.System.entities.AppointmentStatus;
import com.app.Hospital.Management.System.entities.DoctorSchedule;
import com.app.Hospital.Management.System.entities.ScheduledId;
import com.app.Hospital.Management.System.entities.TimeSlot;
import com.app.Hospital.Management.System.repositories.AppointmentRepository;
import com.app.Hospital.Management.System.repositories.DoctorScheduleRepository;
//import.Hospital.Management.System.repositories.DoctorScheduleRepository;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testBookAppointment() {
        // Initialize the Appointment object with a valid DoctorSchedule
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDoctorId(1L); // Set a valid doctor ID
        doctorSchedule.setDate(LocalDate.now());
        doctorSchedule.setAvailableTimeSlots(Arrays.asList(new TimeSlot(LocalTime.of(10, 30), false)));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctorSchedule); // Set the doctor field
        appointment.setAppointmentTime(LocalTime.of(10, 30));

        when(doctorScheduleRepository.findById(any(ScheduledId.class))).thenReturn(Optional.of(doctorSchedule));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        String result = appointmentService.bookAppointment(appointment);
        assertEquals("Appointment booked successfully.", result);
    }

    @Test
    void testCancelAppointment() {
        // Initialize the Appointment object with a valid DoctorSchedule
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDoctorId(1L); // Set a valid doctor ID
        doctorSchedule.setDate(LocalDate.now());
        doctorSchedule.setAvailableTimeSlots(Arrays.asList(new TimeSlot(LocalTime.of(10, 30), true)));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctorSchedule); // Set the doctor field
        appointment.setAppointmentTime(LocalTime.of(10, 30));

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorScheduleRepository.findByDoctorIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(Optional.of(doctorSchedule));

        String result = appointmentService.cancelAppointment(1L);
        assertEquals("Appointment cancelled successfully", result);
    }

    @Test
    void testRescheduleAppointment() {
        // Initialize the Appointment object with a valid DoctorSchedule
        DoctorSchedule currentSchedule = new DoctorSchedule();
        currentSchedule.setDoctorId(1L); // Set a valid doctor ID
        currentSchedule.setDate(LocalDate.now());
        currentSchedule.setAvailableTimeSlots(new ArrayList<>(Arrays.asList(new TimeSlot(LocalTime.of(10, 30), true))));

        Appointment appointment = new Appointment();
        appointment.setDoctor(currentSchedule); // Set the doctor field
        appointment.setAppointmentTime(LocalTime.of(10, 30));

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorScheduleRepository.findByDoctorIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(Optional.of(currentSchedule));
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(currentSchedule);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        String result = appointmentService.rescheduleAppointment(1L, LocalDate.now().plusDays(1), LocalTime.of(11, 30));
        assertEquals("Appointment rescheduled successfully.", result);
    }

    // Add more tests for other business logic methods as needed
}