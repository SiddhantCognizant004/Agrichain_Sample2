//package com.cts.agrichain.subsidy.service;
//
//import com.cts.agrichain.subsidy.dao.DisbursementRepo;
//import com.cts.agrichain.subsidy.dao.SubsidyProgramRepo;
//import com.cts.agrichain.subsidy.dto.DisbursementDTO;
//import com.cts.agrichain.subsidy.dto.FarmerDTO;
//import com.cts.agrichain.subsidy.entity.Disbursement;
//import com.cts.agrichain.subsidy.entity.SubsidyProgram;
//import com.cts.agrichain.subsidy.enums.DisbursementStatus;
//import com.cts.agrichain.subsidy.enums.SubsidyStatus;
//import com.cts.agrichain.subsidy.feign.FarmerClient;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class DisbursementServiceTest {
//
//    @Mock
//    private DisbursementRepo disbursementRepo;
//
//    @Mock
//    private SubsidyProgramRepo subsidyProgramRepo;
//
//    @Mock
//    private FarmerClient farmerClient;
//
//    @InjectMocks
//    private DisbursementService disbursementService;
//
//    private SubsidyProgram program;
//    private DisbursementDTO dto;
//    private FarmerDTO farmer;
//    private Disbursement disbursement;
//
//    @BeforeEach
//    void setUp() {
//        program = new SubsidyProgram();
//        program.setProgramID(1);
//        program.setTitle("Test Program");
//        program.setAllottedBudget(10000.0);
//        program.setConsumedBudget(0.0);
//        program.setSubsidyStatus(SubsidyStatus.PENDING);
//        program.setStartDate(LocalDate.now());
//        program.setEndDate(LocalDate.now().plusMonths(6));
//
//        dto = new DisbursementDTO();
//        dto.setFarmerId(101L);
//        dto.setProgramId(1);
//        dto.setDisbursementAmount(2000.0);
//
//        farmer = new FarmerDTO();
//        farmer.setFarmerId(101L);
//        farmer.setFullName("Test Farmer");
//
//        disbursement = new Disbursement();
//        disbursement.setDisbursementID(1);
//        disbursement.setFarmerId(101L);
//        disbursement.setSubsidyProgram(program);
//        disbursement.setDisbursementAmount(2000.0);
//        disbursement.setDisbursementDate(LocalDate.now());
//        disbursement.setDisbursementStatus(DisbursementStatus.PENDING);
//    }
//
//    // ─── applyForSubsidy ────────────────────────────────────────────────────────
//
//    @Test
//    void applyForSubsidy_Success() {
//        when(farmerClient.getFarmerById(101L)).thenReturn(farmer);
//        when(subsidyProgramRepo.findById(1)).thenReturn(Optional.of(program));
//        when(disbursementRepo.existsByFarmerIdAndSubsidyProgram_ProgramID(101L, 1)).thenReturn(false);
//        when(disbursementRepo.save(any(Disbursement.class))).thenReturn(disbursement);
//
//        Disbursement result = disbursementService.applyForSubsidy(dto);
//
//        assertNotNull(result);
//        assertEquals(DisbursementStatus.PENDING, result.getDisbursementStatus());
//        assertEquals(101L, result.getFarmerId());
//        verify(disbursementRepo).save(any(Disbursement.class));
//    }
//
//    @Test
//    void applyForSubsidy_FarmerNull_ThrowsException() {
//        when(farmerClient.getFarmerById(101L)).thenReturn(null);
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> disbursementService.applyForSubsidy(dto));
//        assertTrue(ex.getMessage().contains("Farmer Service is currently unavailable"));
//    }
//
//    @Test
//    void applyForSubsidy_FarmerIdMinusOne_ThrowsException() {
//        FarmerDTO badFarmer = new FarmerDTO();
//        badFarmer.setFarmerId(-1L);
//        when(farmerClient.getFarmerById(101L)).thenReturn(badFarmer);
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> disbursementService.applyForSubsidy(dto));
//        assertTrue(ex.getMessage().contains("Farmer Service is currently unavailable"));
//    }
//
//    @Test
//    void applyForSubsidy_ProgramNotFound_ThrowsException() {
//        when(farmerClient.getFarmerById(101L)).thenReturn(farmer);
//        when(subsidyProgramRepo.findById(1)).thenReturn(Optional.empty());
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> disbursementService.applyForSubsidy(dto));
//        assertTrue(ex.getMessage().contains("Subsidy Program not found"));
//    }
//
//    @Test
//    void applyForSubsidy_ProgramValidated_ThrowsException() {
//        program.setSubsidyStatus(SubsidyStatus.VALIDATED);
//        when(farmerClient.getFarmerById(101L)).thenReturn(farmer);
//        when(subsidyProgramRepo.findById(1)).thenReturn(Optional.of(program));
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> disbursementService.applyForSubsidy(dto));
//        assertTrue(ex.getMessage().contains("budget is fully consumed"));
//    }
//
//    @Test
//    void applyForSubsidy_DuplicateApplication_ThrowsException() {
//        when(farmerClient.getFarmerById(101L)).thenReturn(farmer);
//        when(subsidyProgramRepo.findById(1)).thenReturn(Optional.of(program));
//        when(disbursementRepo.existsByFarmerIdAndSubsidyProgram_ProgramID(101L, 1)).thenReturn(true);
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> disbursementService.applyForSubsidy(dto));
//        assertTrue(ex.getMessage().contains("already applied"));
//    }
//
//    // ─── getById ────────────────────────────────────────────────────────────────
//
//    @Test
//    void getById_Success() {
//        when(disbursementRepo.findById(1)).thenReturn(Optional.of(disbursement));
//
//        Disbursement result = disbursementService.getById(1);
//
//        assertNotNull(result);
//        assertEquals(1, result.getDisbursementID());
//    }
//
//    @Test
//    void getById_NotFound_ThrowsException() {
//        when(disbursementRepo.findById(99)).thenReturn(Optional.empty());
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> disbursementService.getById(99));
//        assertTrue(ex.getMessage().contains("Disbursement not found"));
//    }
//
//    // ─── getAll ─────────────────────────────────────────────────────────────────
//
//    @Test
//    void getAll_ReturnsList() {
//        when(disbursementRepo.findAll()).thenReturn(List.of(disbursement));
//
//        List<Disbursement> result = disbursementService.getAll();
//
//        assertEquals(1, result.size());
//    }
//
//    // ─── reviewDisbursement ─────────────────────────────────────────────────────
//
//    @Test
//    void reviewDisbursement_Approved_UpdatesConsumedBudget() {
//        when(disbursementRepo.findById(1)).thenReturn(Optional.of(disbursement));
//        when(disbursementRepo.save(any())).thenReturn(disbursement);
//        when(subsidyProgramRepo.save(any())).thenReturn(program);
//
//        Disbursement result = disbursementService.reviewDisbursement(1, DisbursementStatus.COMPLETED);
//
//        assertEquals(DisbursementStatus.COMPLETED, result.getDisbursementStatus());
//        assertEquals(2000.0, program.getConsumedBudget());
//        verify(subsidyProgramRepo).save(program);
//    }
//
//    @Test
//    void reviewDisbursement_Approved_SetsValidatedWhenBudgetFull() {
//        program.setAllottedBudget(2000.0);
//        program.setConsumedBudget(0.0);
//        disbursement.setDisbursementAmount(2000.0);
//
//        when(disbursementRepo.findById(1)).thenReturn(Optional.of(disbursement));
//        when(disbursementRepo.save(any())).thenReturn(disbursement);
//        when(subsidyProgramRepo.save(any())).thenReturn(program);
//
//        disbursementService.reviewDisbursement(1, DisbursementStatus.COMPLETED);
//
//        assertEquals(SubsidyStatus.VALIDATED, program.getSubsidyStatus());
//    }
//
//    @Test
//    void reviewDisbursement_Approved_BudgetExceeded_AutoRejects() {
//        program.setAllottedBudget(1000.0);
//        program.setConsumedBudget(500.0);
//        disbursement.setDisbursementAmount(2000.0);
//
//        when(disbursementRepo.findById(1)).thenReturn(Optional.of(disbursement));
//        when(disbursementRepo.save(any())).thenReturn(disbursement);
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> disbursementService.reviewDisbursement(1, DisbursementStatus.COMPLETED));
//        assertTrue(ex.getMessage().contains("Budget exceeded"));
//    }
//
//    @Test
//    void reviewDisbursement_Rejected_DoesNotUpdateBudget() {
//        when(disbursementRepo.findById(1)).thenReturn(Optional.of(disbursement));
//        when(disbursementRepo.save(any())).thenReturn(disbursement);
//
//        Disbursement result = disbursementService.reviewDisbursement(1, DisbursementStatus.REJECTED);
//
//        assertEquals(DisbursementStatus.REJECTED, result.getDisbursementStatus());
//        verify(subsidyProgramRepo, never()).save(any());
//    }
//
//    @Test
//    void reviewDisbursement_NotFound_ThrowsException() {
//        when(disbursementRepo.findById(99)).thenReturn(Optional.empty());
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> disbursementService.reviewDisbursement(99, DisbursementStatus.COMPLETED));
//        assertTrue(ex.getMessage().contains("Disbursement not found"));
//    }
//
//    // ─── getByFarmerId / getByProgramId ─────────────────────────────────────────
//
//    @Test
//    void getByFarmerId_ReturnsList() {
//        when(disbursementRepo.findByFarmerId(101L)).thenReturn(List.of(disbursement));
//
//        List<Disbursement> result = disbursementService.getByFarmerId(101L);
//
//        assertEquals(1, result.size());
//        assertEquals(101L, result.get(0).getFarmerId());
//    }
//
//    @Test
//    void getByProgramId_ReturnsList() {
//        when(disbursementRepo.findBySubsidyProgram_ProgramID(1)).thenReturn(List.of(disbursement));
//
//        List<Disbursement> result = disbursementService.getByProgramId(1);
//
//        assertEquals(1, result.size());
//    }
//}
