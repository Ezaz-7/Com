package com.app.Hospital.Management.System.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.app.Hospital.Management.System.Services.NotificationService;
import com.app.Hospital.Management.System.entities.Appointment;
import com.app.Hospital.Management.System.entities.DoctorSchedule;
import com.app.Hospital.Management.System.entities.Notification;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.entities.AppointmentStatus;
import com.app.Hospital.Management.System.repositories.AppointmentRepository;
import com.app.Hospital.Management.System.repositories.NotificationRepository;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNotification() {
        Notification notification = new Notification();
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.saveNotification(notification);

        assertEquals(notification, result);
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testGetAllNotifications() {
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> result = notificationService.getAllNotifications();

        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void testGetNotificationById() {
        Notification notification = new Notification();
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        Optional<Notification> result = notificationService.getNotificationById(1L);

        assertTrue(result.isPresent());
        assertEquals(notification, result.get());
        verify(notificationRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteNotificationById() {
        doNothing().when(notificationRepository).deleteById(1L);

        notificationService.deleteNotificationById(1L);

        verify(notificationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreateNotificationsForAppointment() {
        Appointment appointment = new Appointment();
        PatientProfile patient = new PatientProfile();
        patient.setName("John Doe");

        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDoctorId(101L);
        doctorSchedule.setDate(LocalDate.now().plusDays(1));

        appointment.setAppointmentId(1L);
        appointment.setPatient(patient);
        appointment.setDoctor(doctorSchedule);
        appointment.setAppointmentTime(LocalTime.now());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findByAppointmentId(1L)).thenReturn(Optional.of(appointment));
        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());

        notificationService.createNotificationsForAppointment(1L);

        verify(appointmentRepository, times(1)).findByAppointmentId(1L);
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }
}

