package com.app.Hospital.Management.System.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
 
import com.app.Hospital.Management.System.Services.MedicalHistoryService;
import com.app.Hospital.Management.System.entities.MedicalHistory;
import com.app.Hospital.Management.System.entities.PatientProfile;
import com.app.Hospital.Management.System.repositories.MedicalHistoryRepository;
 
class MedicalHistoryServiceTest {
 
    @InjectMocks
    private MedicalHistoryService medicalHistoryService;
 
    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;
 
    private MedicalHistory medicalHistory;
    private PatientProfile patientProfile;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patientProfile = new PatientProfile();
        patientProfile.setPatientId(1L);
 
        medicalHistory = new MedicalHistory();
        medicalHistory.setHistoryId(1L);
        medicalHistory.setPatient(patientProfile);
        medicalHistory.setDiagnosis("Flu");
        medicalHistory.setTreatment("Rest and hydration");
        medicalHistory.setDateOfVisit(LocalDateTime.now());
    }
 
    @Test
    void testAddMedicalHistory() {
        when(medicalHistoryRepository.save(medicalHistory)).thenReturn(medicalHistory);
 
        MedicalHistory savedHistory = medicalHistoryService.addMedicalHistory(medicalHistory);
 
        assertNotNull(savedHistory);
        assertEquals("Flu", savedHistory.getDiagnosis());
        verify(medicalHistoryRepository, times(1)).save(medicalHistory);
    }
 
    @Test
    void testViewMedicalHistory() {
        when(medicalHistoryRepository.findByPatientId(1L)).thenReturn(Arrays.asList(medicalHistory));
 
        List<MedicalHistory> historyList = medicalHistoryService.viewMedicalHistory(1L);
 
        assertNotNull(historyList);
        assertEquals(1, historyList.size());
        assertEquals("Flu", historyList.get(0).getDiagnosis());
    }
 
    @Test
    void testViewByTreatment() {
        when(medicalHistoryRepository.findByPatientId(1L)).thenReturn(Arrays.asList(medicalHistory));
 
        List<MedicalHistory> historyList = medicalHistoryService.viewByTreatment(1L, "Flu");
 
        assertNotNull(historyList);
        assertEquals(1, historyList.size());
        assertEquals("Flu", historyList.get(0).getDiagnosis());
    }
 
    @Test
    void testDeleteMedicalHistory() {
        when(medicalHistoryRepository.findByPatientId(1L)).thenReturn(Arrays.asList(medicalHistory));
 
        medicalHistoryService.deleteMedicalHistory(1L);
 
        verify(medicalHistoryRepository, times(1)).deleteAll(Arrays.asList(medicalHistory));
    }
}