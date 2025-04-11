package com.app.Hospital.Management.System.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.Hospital.Management.System.Services.MedicalHistoryService;
import com.app.Hospital.Management.System.Services.PatientProfileService;
import com.app.Hospital.Management.System.entities.MedicalHistory;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.repositories.AppointmentRepository;
import com.app.Hospital.Management.System.repositories.PatientProfileRepository;

@SpringBootTest
public class PatientProfileServiceTest {
	@InjectMocks
    private PatientProfileService patientProfileService;
	
	@InjectMocks
    private MedicalHistoryService medicalHistoryService;

    @Mock
    private PatientProfileRepository patientRepository;
    
    @Mock
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSavePatient() {
        PatientProfile patient = new PatientProfile();
        patient.setName("John Doe");
        when(patientRepository.save(patient)).thenReturn(patient);

        PatientProfile savedPatient = patientProfileService.savePatient(patient);
        assertEquals("John Doe", savedPatient.getName());
    }

    @Test
    public void testGetAllPatients() {
        PatientProfile patient1 = new PatientProfile();
        patient1.setName("John Doe");
        PatientProfile patient2 = new PatientProfile();
        patient2.setName("Jane Doe");

        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));

        List<PatientProfile> patients = patientProfileService.getAllPatients();
        assertEquals(2, patients.size());
    }
    
    @Test
    public void testGetPatientById() {
        Long patientId = 1L;
        PatientProfile patient = new PatientProfile();
        patient.setPatientId(patientId);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        Optional<PatientProfile> foundPatient = patientProfileService.getPatientById(patientId);

        assertTrue(foundPatient.isPresent());
        assertEquals(patientId, foundPatient.get().getPatientId());
    }

    @Test
    public void testGetPatientById_NotFound() {
        Long patientId = 1L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        Optional<PatientProfile> foundPatient = patientProfileService.getPatientById(patientId);

        assertFalse(foundPatient.isPresent());
    }
    
    @Test
    public void testDeletePatient() {
        Long patientId = 1L;

        patientProfileService.deletePatient(patientId);

        verify(appointmentRepository).deleteByPatientId(patientId);
        verify(patientRepository).deleteById(patientId);
    }
    
   
    
}